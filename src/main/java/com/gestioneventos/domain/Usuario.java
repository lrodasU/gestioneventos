package com.gestioneventos.domain;

import java.util.Objects;

// Usuario base, modela credenciales y rol genérico.

public abstract class Usuario {
    private final String id;
    private final String nombre;
    private final String email;
    private final String passwordHash;

    protected Usuario() {
        this.id = null;
        this.nombre = null;
        this.passwordHash = null;
        this.email = null;
    }

    public Usuario(String id, String nombre, String email, String passwordHash) {
        this.id = Objects.requireNonNull(id, "id no puede ser null");
        this.nombre = Objects.requireNonNull(nombre, "nombre no puede ser null");
        this.email = Objects.requireNonNull(email, "email no puede ser null");
        this.passwordHash = Objects.requireNonNull(passwordHash, "passwordHash no puede ser null");
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean validarCredenciales(String password) {
        return Objects.equals(passwordHash, hash(password));
    }

    // Mock de algoritmo de hashing
    private String hash(String raw) {
        return Integer.toHexString(raw.hashCode());
    }
}
