package com.gestioneventos.infrastructure;

import com.gestioneventos.domain.Evento;
import com.gestioneventos.domain.Usuario;

import java.util.List;

// Abstracción de la capa de persistencia
// Permite cargar y guardar colecciones de dominio sin exponer detalles de I/O
public interface StorageAdapter {
    List<Usuario> loadUsuarios();

    List<Evento> loadEventos();

    void saveUsuarios(List<Usuario> usuarios);

    void saveEventos(List<Evento> eventos);
}
