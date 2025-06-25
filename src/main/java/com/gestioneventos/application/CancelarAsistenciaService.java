package com.gestioneventos.application;

import com.gestioneventos.domain.Usuario;
import com.gestioneventos.domain.Asistente;
import com.gestioneventos.domain.Evento;
import com.gestioneventos.infrastructure.StorageAdapter;

import java.util.List;

// Elimina el registro de un asistente en un evento
// y persiste la información
public class CancelarAsistenciaService {
    private final StorageAdapter storage;

    public CancelarAsistenciaService(StorageAdapter storage) {
        this.storage = storage;
    }

    public Asistente execute(String eventoId, String asistenteId) {
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

        if (!asistente.estaRegistradoEn(evento)) {
            throw new IllegalArgumentException("El asistente no está registrado en el evento");
        }

        asistente.cancelarRegistroEn(evento);
        storage.saveUsuarios(usuarios);
        storage.saveEventos(eventos);
        return asistente;
    }
}
