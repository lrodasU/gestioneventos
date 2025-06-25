package com.gestioneventos.application;

import com.gestioneventos.domain.Usuario;
import com.gestioneventos.domain.Asistente;
import com.gestioneventos.domain.Evento;
import com.gestioneventos.infrastructure.StorageAdapter;

import java.util.List;
import java.util.Objects;

// Carga las colecciones de usuarios y eventos, los busca por id,
// registra al asistente en el evento y persiste la lista de eventos
public class RegistrarAsistenciaService {
    private final StorageAdapter storage;

    public RegistrarAsistenciaService(StorageAdapter storage) {
        this.storage = storage;
    }

    // Registra al asistente en el evento y persiste la lista de eventos
    // Devuelve el evento modificado
    public Evento execute(String eventoId, String asistenteId) {
        Objects.requireNonNull(eventoId, "El ID del evento no puede ser null");
        Objects.requireNonNull(asistenteId, "El ID del asistente no puede ser null");

        List<Evento> eventos = storage.loadEventos();

        // Busca el evento por ID
        Evento evento = null;
        for (Evento e : eventos) {
            if (e.getId().equals(eventoId)) {
                evento = e;
                break;
            }
        }
        if (evento == null) {
            throw new IllegalArgumentException("No existe evento con ID = " + eventoId);
        }

        List<Usuario> usuarios = storage.loadUsuarios();

        // Busca el asistente por ID
        Asistente asistente = null;
        for (Usuario u : usuarios) {
            if (u instanceof Asistente && u.getId().equals(asistenteId)) {
                asistente = (Asistente) u;
                break;
            }
        }

        if (asistente == null) {
            throw new IllegalArgumentException("No existe asistente con ID = " + asistenteId);
        }

        if (asistente.estaRegistradoEn(evento)) {
            throw new IllegalArgumentException("El asistente ya está registrado en el evento");
        }

        asistente.registrarseEn(evento);

        storage.saveEventos(eventos);

        storage.saveUsuarios(usuarios);

        return evento;
    }
}
