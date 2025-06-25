package com.gestioneventos.application;

import com.gestioneventos.domain.Evento;
import com.gestioneventos.domain.Organizador;
import com.gestioneventos.domain.Asistente;
import com.gestioneventos.infrastructure.StorageAdapter;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Comparator;

// Construye un nuevo evento, validandolo y guardandolo
public class CrearEventoService {

    private final StorageAdapter storage;

    public CrearEventoService(StorageAdapter storage) {
        this.storage = storage;
    }

    public Evento execute(String titulo, LocalDate fecha, String ubicacion,
            String descripcion, List<Organizador> organizadores, List<Asistente> asistentes) {

        // Carga eventos existentes para determinar la siguiente id disponible
        List<Evento> eventos = Objects.requireNonNull(storage.loadEventos(),
                "La lista de eventos no puede ser null");

        int nextId = eventos.stream()
                .map(Evento::getId)
                .map(idStr -> {
                    try {
                        return Integer.parseInt(idStr);
                    } catch (NumberFormatException e) {
                        return 0; // ignora IDs no numericos
                    }
                })
                .max(Comparator.naturalOrder())
                .orElse(0) + 1;

        String id = String.valueOf(nextId);

        Evento nuevoEvento = new Evento(
                id,
                titulo,
                fecha,
                ubicacion,
                descripcion,
                organizadores,
                asistentes);

        eventos.add(nuevoEvento);
        storage.saveEventos(eventos);

        return nuevoEvento;
    }
}
