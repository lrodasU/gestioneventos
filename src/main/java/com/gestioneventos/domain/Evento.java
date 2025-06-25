package com.gestioneventos.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

// Representa un evento con sus datos, organizadores y lista de asistentes.
public class Evento {
    private final String id;
    private String titulo;
    private LocalDate fecha;
    private String ubicacion;
    private String descripcion;
    private final List<Organizador> organizadores = new ArrayList<>();
    private final List<Asistente> asistentes = new ArrayList<>();

    public Evento() {
        this.id = "";
    }

    public Evento(String id,
            String titulo,
            LocalDate fecha,
            String ubicacion,
            String descripcion,
            List<Organizador> organizadoresIniciales,
            List<Asistente> asistentesIniciales) {
        this.id = Objects.requireNonNull(id, "id no puede ser null");
        this.titulo = Objects.requireNonNull(titulo, "título no puede ser null");
        this.fecha = Objects.requireNonNull(fecha, "fecha no puede ser null");
        this.ubicacion = Objects.requireNonNull(ubicacion, "ubicación no puede ser null");
        this.descripcion = Objects.requireNonNull(descripcion, "descripción no puede ser null");

        Objects.requireNonNull(organizadoresIniciales, "organizadores no puede ser null");
        Objects.requireNonNull(asistentesIniciales, "asistentes no puede ser null");

        if (organizadoresIniciales.isEmpty()) {
            throw new IllegalArgumentException("Debe haber al menos un organizador");
        }

        this.organizadores.addAll(organizadoresIniciales);
        this.asistentes.addAll(asistentesIniciales);
    }

    public List<Organizador> getOrganizadores() {
        return Collections.unmodifiableList(organizadores);
    }

    public void agregarOrganizador(Organizador o) {
        if (!organizadores.contains(o)) {
            organizadores.add(o);
        }
    }

    public void removerOrganizador(Organizador o) {
        organizadores.remove(o);
        if (organizadores.isEmpty()) {
            throw new IllegalStateException("Debe quedar al menos un organizador");
        }
    }

    public void agregarAsistente(Asistente a) {
        if (!asistentes.contains(a)) {
            asistentes.add(a);
        }
    }

    public void removerAsistente(Asistente a) {
        asistentes.remove(a);
    }

    public List<Asistente> getAsistentes() {
        return Collections.unmodifiableList(asistentes);
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = Objects.requireNonNull(titulo);
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = Objects.requireNonNull(fecha);
        validarFechaFutura();
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = Objects.requireNonNull(ubicacion);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = Objects.requireNonNull(descripcion);
    }

    public void setOrganizadores(List<Organizador> organizadores) {
        if (organizadores.isEmpty() || organizadores == null) {
            throw new IllegalArgumentException("Debe haber al menos un organizador");
        }
        this.organizadores.clear();
        this.organizadores.addAll(Objects.requireNonNull(organizadores));
    }

    public void setAsistentes(List<Asistente> asistentes) {
        if (asistentes == null) {
            asistentes = new ArrayList<>();
        }
        this.asistentes.clear();
        this.asistentes.addAll(Objects.requireNonNull(asistentes));
    }

    // Lanza excepción si la fecha es anterior a hoy
    public void validarFechaFutura() {
        if (fecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha del evento debe ser hoy o futura.");
        }
    }

    public boolean esOrganizador(Usuario u) {
        return organizadores.stream()
                .anyMatch(o -> o.getId().equals(u.getId()));
    }

    public boolean esAsistente(Usuario u) {
        return asistentes.stream()
                .anyMatch(a -> a.getId().equals(u.getId()));
    }
}
