package com.gestioneventos.domain;

// Usuario que puede inscribirse como asistente a eventos
public class Asistente extends Usuario {

    public Asistente() {
        super();
    }

    public Asistente(String id, String nombre, String email, String passwordHash) {
        super(id, nombre, email, passwordHash);
    }

    // Determina si este asistente ya está registrado en el evento

    public boolean estaRegistradoEn(Evento evento) {
        return evento.getAsistentes()
                .stream()
                .anyMatch(a -> a.getId().equals(this.getId()));
    }

    // Se registra como asistente en el evento, si aún no lo está
    public void registrarseEn(Evento evento) {
        if (!estaRegistradoEn(evento)) {
            evento.agregarAsistente(this);
        }
    }

    // Remueve la inscripción de este asistente en el evento, si lo está
    public void cancelarRegistroEn(Evento evento) {
        if (estaRegistradoEn(evento)) {
            evento.removerAsistente(this);
        }
    }

    @Override
    public String toString() {
        return super.getNombre();
    }
}
