# Sistema de Gestión de Eventos

Este proyecto fue desarrollado como Trabajo Práctico Obligatorio (TPO) para la materia **Paradigma Orientado a Objetos** de la Universidad Argentina de la Empresa (UADE). El objetivo fue construir una aplicación completa utilizando **Java** y la biblioteca **Swing**, integrando conocimientos aprendidos en la cursada con experiencia previa profesional, y abordándolo como una pieza clave para mi portfolio.

## 📌 Introducción

Decidí extender el alcance del proyecto más allá de lo solicitado para que el resultado no solo cumpliera con las consignas, sino que también pudiera integrarse a mi portfolio profesional. La aplicación fue desarrollada con una arquitectura escalable, aplicando principios **SOLID** y patrones de diseño como **MVP** y **Adapter**, permitiendo su evolución sin reescrituras profundas.

## 🎯 Propósito General

El sistema permite a distintos tipos de usuarios (organizadores y asistentes) gestionar eventos, visualizar información relevante y mantener un historial de eventos. Incluye funcionalidades como:

- Autenticación de usuarios
- Creación, edición y eliminación de eventos
- Registro y visualización de asistentes
- Notificaciones por cambios en eventos

## 🧱 Arquitectura

El sistema se organiza en **4 capas**:

1. **Presentación**: Swing (Views) + Presenters (MVP)
2. **Aplicación / Servicio**: Casos de uso
3. **Dominio**: Entidades y lógica de negocio
4. **Persistencia**: Adaptadores de almacenamiento en JSON

### Patrones Utilizados

- **MVP (Model-View-Presenter)**: separación clara entre UI y lógica
- **Adapter / Strategy**: desacopla la lógica de negocio del mecanismo de almacenamiento

## 📚 Casos de uso

Algunos de los casos de uso implementados:

- Crear / Modificar / Eliminar evento
- Registrar asistencia
- Listar eventos pasados y futuros
- Autenticar usuario
- Notificar asistentes sobre cambios o cancelaciones

## 🧪 Guía de prueba

Para ingresar a la aplicación, se deben utilizar las siguientes credenciales:

**Organizador**
- Email: `ana.perez@example.com`
- Contraseña: `hola123`

**Asistente**
- Email: `juan.gomez@example.com`
- Contraseña: `hola123`

Las contraseñas están hasheadas en el archivo `usuarios.json` ubicado en la carpeta `/data`.

## 🛠️ Mejoras futuras

- Persistencia con base de datos (reemplazando JSON mediante nuevos StorageAdapter)
- Envío real de notificaciones por email (implementación de `SmtpEmailAdapter`)
- Estilización avanzada de UI
- Control de permisos dinámico por evento

## 🧾 Conclusión

Este proyecto representó una oportunidad concreta para integrar conocimientos, resolver problemas reales y adoptar prácticas profesionales como el uso de GitHub para versionado y documentación. Comparando tecnologías como **Swing**, **React** y **Unity**, logré enriquecer mi enfoque de desarrollo y sumar una pieza valiosa a mi portfolio profesional.

---

**Lucas Rodas**  
Legajo: 1199266  
Licenciatura en Gestión de Tecnología de la Información  
Universidad Argentina De La Empresa
