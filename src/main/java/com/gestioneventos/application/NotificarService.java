package com.gestioneventos.application;

import com.gestioneventos.domain.Asistente;
import com.gestioneventos.domain.Evento;
import com.gestioneventos.infrastructure.EmailAdapter;

import java.util.Objects;

/**
 * Servicio que notifica por correo:
 * - a un asistente nuevo cuando se registra en un evento
 * - a todos los asistentes cuando un evento cambia
 * - o a todos los asistentes cuando un evento se cancela
 * 
 * Utiliza sobrecarga para determinar cuando debe mandar un solo mail
 * y cuando multiples
 */
public class NotificarService {

    private final EmailAdapter emailAdapter;

    public NotificarService(EmailAdapter emailAdapter) {
        this.emailAdapter = Objects.requireNonNull(emailAdapter, "EmailAdapter no puede ser null");
    }

    // Notifica al asistente recién registrado en el evento
    public void execute(Evento evento, TipoNotificacion tipo, Asistente nuevoAsistente) {
        Objects.requireNonNull(evento, "El evento no puede ser null");
        Objects.requireNonNull(tipo, "El tipo de notificación no puede ser null");
        Objects.requireNonNull(nuevoAsistente, "El asistente no puede ser null");

        if (tipo != TipoNotificacion.CREACION) {
            throw new IllegalArgumentException(
                    "Este método solo admite CREACION, se recibió: " + tipo);
        }

        String subject = "Confirmación de registro en: " + evento.getTitulo();
        String template = "Hola %s,%n%n" +
                "Te has registrado correctamente en el evento \"%s\" (ID=%s).%n" +
                "Detalles:%n" +
                " • Fecha: %s%n" +
                " • Ubicación: %s%n%n" +
                "¡Te esperamos!%nEquipo GestiónEventos";

        String body = String.format(
                template,
                nuevoAsistente.getNombre(),
                evento.getTitulo(),
                evento.getId(),
                evento.getFecha(),
                evento.getUbicacion());
        emailAdapter.sendEmail(nuevoAsistente.getEmail(), subject, body);
    }

    // Notifica a todos los asistentes cuando se modifica o elimina un evento
    public void execute(Evento evento, TipoNotificacion tipo) {
        Objects.requireNonNull(evento, "El evento no puede ser null");
        Objects.requireNonNull(tipo, "El tipo de notificación no puede ser null");

        switch (tipo) {
            case MODIFICACION:
                sendModificationMails(evento);
                break;
            case ELIMINACION:
                sendCancellationMails(evento);
                break;
            default:
                throw new IllegalArgumentException(
                        "Este método no admite " + tipo + ", solo MODIFICACION o ELIMINACION");
        }
    }

    private void sendModificationMails(Evento evento) {
        String subject = "Actualización en el evento: " + evento.getTitulo();
        String template = "Hola %s,%n%n" +
                "El evento \"%s\" (ID=%s) ha sufrido cambios:%n" +
                " • Fecha: %s%n" +
                " • Ubicación: %s%n%n" +
                "Por favor revisa los detalles actualizados.%nEquipo GestiónEventos";

        for (Asistente a : evento.getAsistentes()) {
            String body = String.format(
                    template,
                    a.getNombre(),
                    evento.getTitulo(),
                    evento.getId(),
                    evento.getFecha(),
                    evento.getUbicacion());
            emailAdapter.sendEmail(a.getEmail(), subject, body);
        }
    }

    private void sendCancellationMails(Evento evento) {
        String subject = "Cancelación del evento: " + evento.getTitulo();
        String template = "Hola %s,%n%n" +
                "Lamentamos informarte que el evento \"%s\" (ID=%s) ha sido cancelado.%n%n" +
                "Sentimos las molestias, esperamos verte en futuros eventos.%nEquipo GestiónEventos";

        for (Asistente a : evento.getAsistentes()) {
            String body = String.format(
                    template,
                    a.getNombre(),
                    evento.getTitulo(),
                    evento.getId());
            emailAdapter.sendEmail(a.getEmail(), subject, body);
        }
    }
}
