package com.gestioneventos.domain;

// Usuario con permiso de crear, editar y eliminar eventos.

public class Organizador extends Usuario {

    public Organizador() {
        super();
    }

    public Organizador(String id, String nombre, String email, String passwordHash) {
        super(id, nombre, email, passwordHash);
    }

    public boolean puedeGestionar(Evento evento) {
        return evento.esOrganizador(this);
    }

    @Override
    public String toString() {
        return super.getNombre();
    }
}
