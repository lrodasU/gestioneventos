package com.gestioneventos.application;

import com.gestioneventos.domain.Asistente;
import com.gestioneventos.domain.Evento;
import com.gestioneventos.domain.Organizador;
import com.gestioneventos.infrastructure.StorageAdapter;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

// Carga la lista completa, busca el evento por su ID, actualiza sus datos,
// vuelve a validar reglas de negocio y persiste la colección
public class ModificarEventoService {

    private final StorageAdapter storage;

    public ModificarEventoService(StorageAdapter storage) {
        this.storage = storage;
    }

    public Evento execute(String eventoId,
            String nuevoTitulo,
            LocalDate nuevaFecha,
            String nuevaUbicacion,
            String nuevaDescripcion,
            List<Organizador> nuevosOrganizadores,
            List<Asistente> nuevosAsistentes) {

        Objects.requireNonNull(eventoId, "El ID del evento no puede ser null");
        Objects.requireNonNull(nuevosOrganizadores, "La lista de organizadores no puede ser null");
        if (nuevosOrganizadores.isEmpty()) {
            throw new IllegalArgumentException("Debe haber al menos un organizador");
        }

        // Carga todos los eventos
        List<Evento> eventos = storage.loadEventos();

        // Encuentra el evento por ID
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

        // Actualiza los campos y valida
        evento.setTitulo(Objects.requireNonNull(nuevoTitulo, "Título no puede ser null"));
        evento.setFecha(Objects.requireNonNull(nuevaFecha, "Fecha no puede ser null"));
        evento.setUbicacion(Objects.requireNonNull(nuevaUbicacion, "Ubicación no puede ser null"));
        evento.setDescripcion(Objects.requireNonNull(nuevaDescripcion, "Descripción no puede ser null"));

        // Reemplaza los organizadores
        evento.setOrganizadores(nuevosOrganizadores);

        // Reemplaza los asistentes
        evento.setAsistentes(nuevosAsistentes);

        // Persiste todos los eventos
        storage.saveEventos(eventos);

        return evento;
    }
}
