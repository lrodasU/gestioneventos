package com.gestioneventos.domain;

public class UsuarioDto {
    public String id;
    public String nombre;
    public String passwordHash;
    public String email;
    public String type;

    public UsuarioDto() {
        this.id = null;
        this.nombre = null;
        this.passwordHash = null;
        this.email = null;
        this.type = null;
    }

    public UsuarioDto(String id, String nombre, String passwordHash, String email, String type) {
        this.id = id;
        this.nombre = nombre;
        this.passwordHash = passwordHash;
        this.email = email;
        this.type = type;
    }

}
