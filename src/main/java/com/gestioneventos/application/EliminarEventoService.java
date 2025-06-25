package com.gestioneventos.application;

import com.gestioneventos.domain.Evento;
import com.gestioneventos.infrastructure.StorageAdapter;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

// Carga la coleccion completa, busca el evento por id,
// si lo encuentra lo elimina y persiste la lista nueva
public class EliminarEventoService {
    private final StorageAdapter storage;

    public EliminarEventoService(StorageAdapter storage) {
        this.storage = storage;
    }

    // Elimina un evento existente por id
    public void execute(String eventoId) {
        Objects.requireNonNull(eventoId, "El ID del evento no puede ser null");

        // Carga la lista de eventos completa
        List<Evento> eventos = storage.loadEventos();

        // Busca y elimina el evento por id
        boolean eliminado = false;
        Iterator<Evento> iterator = eventos.iterator();
        while (iterator.hasNext()) {
            Evento e = iterator.next();
            if (e.getId().equals(eventoId)) {
                iterator.remove();
                eliminado = true;
                break;
            }
        }

        if (!eliminado) {
            throw new IllegalArgumentException("No existe evento con ID = " + eventoId);
        }

        // Persiste la lista actualizada
        storage.saveEventos(eventos);
    }
}
