package com.gestioneventos.application;

import com.gestioneventos.domain.Asistente;
import com.gestioneventos.domain.Evento;
import com.gestioneventos.infrastructure.EmailAdapter;

import java.util.*;
import java.util.stream.Collectors;

// Servicio que notifica por correo:
// - a un asistente nuevo cuando se registra en un evento
// - a todos los asistentes cuando un evento cambia
// - o a todos los asistentes cuando un evento se cancela
public class NotificarService {

    private final EmailAdapter emailAdapter;

    public NotificarService(EmailAdapter emailAdapter) {
        this.emailAdapter = Objects.requireNonNull(emailAdapter, "EmailAdapter no puede ser null");
    }

    // Notifica según el tipo:
    // - CREACION: se envía a todos asistentes de eventoActual
    // - ELIMINACION: se envía a todos asistentes de eventoAnterior
    // - MODIFICACION: se comparan asistentes entre eventoAnterior y
    // eventoActual
    public void execute(Evento eventoAnterior, Evento eventoActual, TipoNotificacion tipo) {
        Objects.requireNonNull(tipo, "El tipo de notificación no puede ser null");

        switch (tipo) {
            case CREACION:
                validar(eventoActual);
                enviarCreacion(eventoActual);
                break;

            case ELIMINACION:
                validar(eventoAnterior);
                enviarEliminacion(eventoAnterior);
                break;

            case MODIFICACION:
                validar(eventoAnterior);
                validar(eventoActual);
                enviarModificacion(eventoAnterior, eventoActual);
                break;

            default:
                throw new IllegalArgumentException("Tipo no soportado: " + tipo);
        }
    }

    private void validar(Evento evento) {
        if (evento == null) {
            throw new IllegalArgumentException("Evento para notificar no puede ser null");
        }
    }

    private void enviarCreacion(Evento evento) {
        String subject = "Nuevo evento: " + evento.getTitulo();
        String template = "Hola %s,\n\n" +
                "Estás invitado a un nuevo evento: '%s' (ID=%s).\n" +
                "Fecha: %s, Ubicación: %s\n\n" +
                "¡No te lo pierdas!\n";

        for (Asistente a : evento.getAsistentes()) {
            String body = String.format(template,
                    a.getNombre(),
                    evento.getTitulo(),
                    evento.getId(),
                    evento.getFecha(),
                    evento.getUbicacion());
            emailAdapter.sendEmail(a.getEmail(), subject, body);
        }
    }

    private void enviarEliminacion(Evento evento) {
        String subject = "Evento cancelado: " + evento.getTitulo();
        String template = "Hola %s,\n\n" +
                "El evento '%s' (ID=%s) ha sido cancelado.\n\n" +
                "Lo sentimos mucho.\n";

        for (Asistente a : evento.getAsistentes()) {
            String body = String.format(template,
                    a.getNombre(),
                    evento.getTitulo(),
                    evento.getId());
            emailAdapter.sendEmail(a.getEmail(), subject, body);
        }
    }

    private void enviarModificacion(Evento original, Evento modificado) {
        Set<String> idsOriginal = original.getAsistentes().stream()
                .map(Asistente::getId).collect(Collectors.toSet());
        Set<String> idsModificado = modificado.getAsistentes().stream()
                .map(Asistente::getId).collect(Collectors.toSet());

        Set<String> nuevos = new HashSet<>(idsModificado);
        nuevos.removeAll(idsOriginal);
        Set<String> removidos = new HashSet<>(idsOriginal);
        removidos.removeAll(idsModificado);

        Map<String, Asistente> mapaAnt = original.getAsistentes().stream()
                .collect(Collectors.toMap(Asistente::getId, a -> a));
        Map<String, Asistente> mapaAct = modificado.getAsistentes().stream()
                .collect(Collectors.toMap(Asistente::getId, a -> a));

        String templateNuevo = "Hola %s,\n\n" +
                "Estás invitado a un nuevo evento: '%s' (ID=%s).\n" +
                "Fecha: %s, Ubicación: %s\n\n" +
                "¡No te lo pierdas!\n";

        // Notificar solo cambios en asistentes
        for (String id : nuevos) {
            Asistente a = mapaAct.get(id);
            emailAdapter.sendEmail(a.getEmail(),
                    "Nuevo evento: " + modificado.getTitulo(),
                    String.format(templateNuevo,
                            a.getNombre(),
                            modificado.getTitulo(),
                            modificado.getId(),
                            modificado.getFecha(),
                            modificado.getUbicacion()));
        }
        for (String id : removidos) {
            Asistente a = mapaAnt.get(id);
            emailAdapter.sendEmail(a.getEmail(),
                    "Baja de evento: " + original.getTitulo(),
                    String.format("Hola %s,\n\nHas sido dado de baja del evento '%s' (ID=%s).\n", a.getNombre(),
                            original.getTitulo(), original.getId()));
        }

        // Si no se detectan cambios en los detalles del evento, no se notifica a los
        // asistentes anteriores
        if (Objects.equals(original.getTitulo(), modificado.getTitulo())
                || Objects.equals(original.getDescripcion(), modificado.getDescripcion())
                || Objects.equals(original.getFecha(), modificado.getFecha())
                || Objects.equals(original.getUbicacion(), modificado.getUbicacion())) {
            return;
        }

        String subject = "Actualización evento: " + modificado.getTitulo();
        String templateActualizado = "Hola %s,\n\n" +
                "El evento '%s' (ID=%s) ha sido actualizado:\n" +
                " • Fecha: %s\n" +
                " • Ubicación: %s\n\n";
        for (String id : idsModificado) {
            // Solo asistentes actuales
            Asistente a = mapaAct.get(id);
            String body = String.format(templateActualizado,
                    a.getNombre(), modificado.getTitulo(), modificado.getId(),
                    modificado.getFecha(), modificado.getUbicacion());
            emailAdapter.sendEmail(a.getEmail(), subject, body);
        }
    }
}
