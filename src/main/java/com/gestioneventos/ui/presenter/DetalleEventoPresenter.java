package com.gestioneventos.ui.presenter;

import com.gestioneventos.application.AuthService;
import com.gestioneventos.application.CancelarAsistenciaService;
import com.gestioneventos.application.CrearEventoService;
import com.gestioneventos.application.EliminarEventoService;
import com.gestioneventos.application.ListarEventosService;
import com.gestioneventos.application.ListarUsuariosService;
import com.gestioneventos.application.ModificarEventoService;
import com.gestioneventos.application.NotificarService;
import com.gestioneventos.application.RegistrarAsistenciaService;
import com.gestioneventos.domain.Asistente;
import com.gestioneventos.domain.Evento;
import com.gestioneventos.domain.Organizador;
import com.gestioneventos.domain.Usuario;
import com.gestioneventos.ui.MainFrame;
import com.gestioneventos.ui.view.DetalleEventoView;
import com.gestioneventos.ui.view.CrearEventoView;
import com.gestioneventos.ui.view.DashboardView;

import java.util.List;
import java.util.stream.Collectors;

//Presenter para la pantalla de detalle de un evento
public class DetalleEventoPresenter {

    private final AuthService authService;
    private final ListarEventosService listarService;
    private final ListarUsuariosService listarUsuariosService;
    private final CrearEventoService crearService;
    private final ModificarEventoService modificarService;
    private final EliminarEventoService eliminarService;
    private final RegistrarAsistenciaService registrarService;
    private final CancelarAsistenciaService cancelarService;
    private final NotificarService notificarService;
    private final MainFrame mainFrame;
    private final DetalleEventoView view;
    private final Usuario usuario;
    private Evento evento;

    public DetalleEventoPresenter(MainFrame mainFrame,
            AuthService authService,
            ListarEventosService listarService,
            ListarUsuariosService listarUsuariosService,
            CrearEventoService crearService,
            ModificarEventoService modificarService,
            EliminarEventoService eliminarService,
            RegistrarAsistenciaService registrarService,
            CancelarAsistenciaService cancelarService,
            NotificarService notificarService,
            DetalleEventoView view,
            Usuario usuario,
            Evento evento) {
        this.mainFrame = mainFrame;
        this.authService = authService;
        this.listarService = listarService;
        this.listarUsuariosService = listarUsuariosService;
        this.crearService = crearService;
        this.modificarService = modificarService;
        this.eliminarService = eliminarService;
        this.registrarService = registrarService;
        this.cancelarService = cancelarService;
        this.notificarService = notificarService;
        this.view = view;
        this.usuario = usuario;
        this.evento = evento;

        inicializar();
        bindActions();
    }

    private void inicializar() {
        List<Asistente> asistentes = listarUsuariosService.executeByAsistentes();
        List<Asistente> asistentesEvento = asistentes.stream()
                .filter(a -> evento.getAsistentes()
                        .stream()
                        .anyMatch(ea -> ea.getId().equals(a.getId())))
                .collect(Collectors.toList());
        evento.setAsistentes(asistentesEvento);
        view.setEvento(evento);
        view.setBotonesOrganizador(usuario instanceof Organizador);
        view.setBotonesAsistente(usuario instanceof Asistente);
        mainFrame.showPanel(view);
    }

    private void bindActions() {
        view.onBack(this::goBack);
        view.onModificar(this::goToModificar);
        view.onEliminar(this::handleEliminar);

        if (usuario instanceof Asistente) {
            view.onCancelar(() -> {
                cancelarService.execute(evento.getId(), usuario.getId());
                // recargo el evento actualizado
                Evento actualizado = listarService.execute(usuario.getId())
                        .stream()
                        .filter(e -> e.getId().equals(evento.getId()))
                        .findFirst()
                        .orElse(evento);
                this.evento = actualizado;
                view.setEvento(actualizado);
                goBack();
            });
        }
    }

    private void goBack() {
        DashboardView dashView = new DashboardView();
        List<Evento> eventos = listarService.execute(usuario.getId());
        dashView.setEventos(eventos);
        new DashboardPresenter(mainFrame,
                authService,
                listarService,
                listarUsuariosService,
                crearService,
                modificarService,
                eliminarService,
                registrarService,
                cancelarService,
                notificarService,
                dashView,
                usuario);
        mainFrame.showPanel(dashView);
    }

    private void goToModificar() {
        CrearEventoView crearView = new CrearEventoView();
        crearView.setModoModificar();
        new CrearEventoPresenter(mainFrame,
                authService,
                listarService,
                listarUsuariosService,
                crearService,
                modificarService,
                eliminarService,
                registrarService,
                cancelarService,
                notificarService,
                crearView,
                usuario,
                evento);
        mainFrame.showPanel(crearView);
    }

    private void handleEliminar() {
        eliminarService.execute(evento.getId());
        goBack();
    }
}
