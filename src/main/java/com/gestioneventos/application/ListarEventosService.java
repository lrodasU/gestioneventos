package com.gestioneventos.application;

import com.gestioneventos.domain.Evento;
import com.gestioneventos.infrastructure.StorageAdapter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

// Permite recuperar todos los eventos
// o los eventos vinculados a un usuario
public class ListarEventosService {
    private final StorageAdapter storage;

    public ListarEventosService(StorageAdapter storage) {
        this.storage = Objects.requireNonNull(storage);
    }

    // Devuelve la lista de eventos vinculados a un usuario
    public List<Evento> execute(String usuarioId) {
        Objects.requireNonNull(usuarioId, "El id de usuario no puede ser null");

        return storage.loadEventos().stream()
                .filter(evento -> evento.getAsistentes().stream()
                        .anyMatch(a -> a.getId().equals(usuarioId))
                        || evento.getOrganizadores().stream()
                                .anyMatch(o -> o.getId().equals(usuarioId)))
                .collect(Collectors.toList());
    }
}
