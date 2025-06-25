package com.gestioneventos.application;

import com.gestioneventos.domain.Asistente;
import com.gestioneventos.domain.Organizador;
import com.gestioneventos.domain.Usuario;
import com.gestioneventos.infrastructure.StorageAdapter;

import java.util.List;
import java.util.stream.Collectors;

// Servicio para recuperar la lista completa de usuarios registrados
public class ListarUsuariosService {

    private final StorageAdapter storage;

    public ListarUsuariosService(StorageAdapter storage) {
        this.storage = storage;
    }

    // Devuelve la lista completa de usuarios
    public List<Usuario> executeByUsuarios() {
        return storage.loadUsuarios();
    }

    // Devuelve los usuarios que son organizadores
    public List<Organizador> executeByOrganizadores() {
        return storage.loadUsuarios().stream()
                .filter(u -> u instanceof Organizador)
                .map(u -> (Organizador) u)
                .collect(Collectors.toList());
    }

    // Devuelve los usuarios que son asistentes
    public List<Asistente> executeByAsistentes() {
        return storage.loadUsuarios().stream()
                .filter(u -> u instanceof Asistente)
                .map(u -> (Asistente) u)
                .collect(Collectors.toList());
    }
}
