package com.gestioneventos.domain;

import java.util.List;

public class EventoDto {
    public String id;
    public String titulo;
    public String fecha; // en formato ISO yyyy-MM-dd
    public String ubicacion;
    public String descripcion;
    public List<String> organizadores; // ids
    public List<String> asistentes; // ids
}
