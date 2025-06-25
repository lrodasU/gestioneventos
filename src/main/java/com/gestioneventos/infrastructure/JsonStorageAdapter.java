package com.gestioneventos.infrastructure;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gestioneventos.domain.Asistente;
import com.gestioneventos.domain.Evento;
import com.gestioneventos.domain.EventoDto;
import com.gestioneventos.domain.Organizador;
import com.gestioneventos.domain.Usuario;
import com.gestioneventos.domain.UsuarioDto;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JsonStorageAdapter implements StorageAdapter {

    private final Path usuariosPath;
    private final Path eventosPath;
    private final ObjectMapper mapper;

    public JsonStorageAdapter(String usuariosFile, String eventosFile) {
        this.usuariosPath = Paths.get(usuariosFile);
        this.eventosPath = Paths.get(eventosFile);
        this.mapper = new ObjectMapper();
        this.mapper.findAndRegisterModules();
    }

    @Override
    public List<Usuario> loadUsuarios() {
        if (!Files.exists(usuariosPath)) {
            throw new RuntimeException("Archivo de usuarios no encontrado: " + usuariosPath);
        }
        try {
            List<UsuarioDto> dtos = mapper.readValue(
                    Files.newBufferedReader(usuariosPath),
                    new TypeReference<List<UsuarioDto>>() {
                    });
            List<Usuario> usuarios = new ArrayList<>();
            for (UsuarioDto dto : dtos) {
                Usuario u = "Organizador".equals(dto.type)
                        ? new Organizador(dto.id, dto.nombre, dto.email, dto.passwordHash)
                        : new Asistente(dto.id, dto.nombre, dto.email, dto.passwordHash);
                usuarios.add(u);
            }
            return usuarios;
        } catch (Exception e) {
            throw new RuntimeException("Error al leer usuarios JSON", e);
        }
    }

    @Override
    public List<Evento> loadEventos() {
        if (!Files.exists(eventosPath)) {
            throw new RuntimeException("Archivo de eventos no encontrado: " + eventosPath);
        }
        try {
            List<EventoDto> dtos = mapper.readValue(
                    Files.newBufferedReader(eventosPath),
                    new TypeReference<List<EventoDto>>() {
                    });

            List<Usuario> allUsers = loadUsuarios();

            List<Evento> lista = new ArrayList<>();
            for (EventoDto d : dtos) {
                LocalDate fecha = LocalDate.parse(d.fecha);

                List<Organizador> orgs = allUsers.stream()
                        .filter(u -> d.organizadores.contains(u.getId()))
                        .filter(u -> u instanceof Organizador)
                        .map(u -> (Organizador) u)
                        .collect(Collectors.toList());

                List<Asistente> asis = allUsers.stream()
                        .filter(u -> d.asistentes.contains(u.getId()))
                        .filter(u -> u instanceof Asistente)
                        .map(u -> (Asistente) u)
                        .collect(Collectors.toList());

                Evento e = new Evento(
                        d.id,
                        d.titulo,
                        fecha,
                        d.ubicacion,
                        d.descripcion,
                        orgs,
                        asis);
                lista.add(e);
            }
            return lista;
        } catch (IOException e) {
            throw new RuntimeException("Error al leer eventos JSON", e);
        }
    }

    @Override
    public void saveUsuarios(List<Usuario> usuarios) {
        try {
            Files.createDirectories(usuariosPath.getParent());

            List<UsuarioDto> dtos = new ArrayList<>();
            for (Usuario u : usuarios) {
                UsuarioDto dto = new UsuarioDto();
                dto.id = u.getId();
                dto.nombre = u.getNombre();
                dto.email = u.getEmail();
                dto.passwordHash = u.getPasswordHash();
                dto.type = u instanceof Organizador ? "Organizador" : "Asistente";
                dtos.add(dto);
            }
            try (BufferedWriter writer = Files.newBufferedWriter(usuariosPath)) {
                mapper.writerWithDefaultPrettyPrinter().writeValue(writer, dtos);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error al escribir usuarios JSON", e);
        }
    }

    @Override
    public void saveEventos(List<Evento> eventos) {
        try {
            Files.createDirectories(eventosPath.getParent());
            List<EventoDto> dtos = eventos.stream().map(e -> {
                EventoDto d = new EventoDto();
                d.id = e.getId();
                d.titulo = e.getTitulo();
                d.fecha = e.getFecha().toString();
                d.ubicacion = e.getUbicacion();
                d.descripcion = e.getDescripcion();
                d.organizadores = e.getOrganizadores().stream()
                        .map(Usuario::getId)
                        .collect(Collectors.toList());
                d.asistentes = e.getAsistentes().stream()
                        .map(Usuario::getId)
                        .collect(Collectors.toList());
                return d;
            }).collect(Collectors.toList());

            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(eventosPath.toFile(), dtos);
        } catch (IOException ex) {
            throw new RuntimeException("Error al escribir eventos JSON", ex);
        }
    }
}
