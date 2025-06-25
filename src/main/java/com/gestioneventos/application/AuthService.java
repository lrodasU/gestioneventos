package com.gestioneventos.application;

import com.gestioneventos.domain.Usuario;
import com.gestioneventos.infrastructure.StorageAdapter;

import java.util.List;
import java.util.Objects;

// Carga la coleccion de usuarios, busca por email y
// valida las credenciales
public class AuthService {
    private final StorageAdapter storage;

    public AuthService(StorageAdapter storage) {
        this.storage = storage;
    }

    // Intenta auntenticar un usuario por email y contraseña
    public Usuario execute(String email, String password) {
        Objects.requireNonNull(email, "El email no puede ser null");
        Objects.requireNonNull(password, "La contraseña no puede ser null");

        List<Usuario> usuarios = storage.loadUsuarios();

        // Busca el usuario por email
        Usuario usuario = null;
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email)) {
                usuario = u;
                break;
            }
        }
        if (usuario == null) {
            throw new IllegalArgumentException("Usuario no encontrado: " + email);
        }

        // Valida la contraseña
        if (!usuario.validarCredenciales(password)) {
            throw new IllegalArgumentException("Credenciales inválidas para el usuario: " + email);
        }

        return usuario;
    }
}
