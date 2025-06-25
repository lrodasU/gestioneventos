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
import com.gestioneventos.domain.Evento;
import com.gestioneventos.domain.Organizador;
import com.gestioneventos.domain.Asistente;
import com.gestioneventos.domain.Usuario;
import com.gestioneventos.ui.MainFrame;
import com.gestioneventos.ui.view.CrearEventoView;
import com.gestioneventos.ui.view.DashboardView;

import java.util.List;
import java.util.stream.Collectors;

// Presenter de la vista CrearEventoView
public class CrearEventoPresenter {
    private final MainFrame mainFrame;
    private final AuthService authService;
    private final ListarEventosService listarSvc;
    private final ListarUsuariosService listarUsuariosService;
    private final CrearEventoService crearSvc;
    private final ModificarEventoService modificarSvc;
    private final EliminarEventoService eliminarSvc;
    private final RegistrarAsistenciaService registrarSvc;
    private final CancelarAsistenciaService cancelarService;
    private final NotificarService notificarSvc;
    private final CrearEventoView view;
    private final Usuario usuario;
    private final Evento eventoOriginal;

    public CrearEventoPresenter(
            MainFrame mainFrame,
            AuthService authService,
            ListarEventosService listarService,
            ListarUsuariosService listarUsuariosService,
            CrearEventoService crearService,
            ModificarEventoService modificarService,
            EliminarEventoService eliminarService,
            RegistrarAsistenciaService registrarService,
            CancelarAsistenciaService cancelarService,
            NotificarService notificarService,
            CrearEventoView view,
            Usuario usuario,
            Evento eventoACambiar // null = modo crear / no null = modo editar
    ) {
        this.mainFrame = mainFrame;
        this.authService = authService;
        this.listarSvc = listarService;
        this.listarUsuariosService = listarUsuariosService;
        this.crearSvc = crearService;
        this.modificarSvc = modificarService;
        this.eliminarSvc = eliminarService;
        this.registrarSvc = registrarService;
        this.cancelarService = cancelarService;
        this.notificarSvc = notificarService;
        this.view = view;
        this.usuario = usuario;
        this.eventoOriginal = eventoACambiar;

        inicializar();
        bind();
    }

    private void inicializar() {
        List<Organizador> orgs = listarUsuariosService.executeByOrganizadores();
        List<Asistente> asis = listarUsuariosService.executeByAsistentes();

        view.setOrganizadores(orgs);
        view.setAsistentes(asis);

        if (eventoOriginal != null) {
            view.loadEvento(eventoOriginal);
        }
    }

    private void bind() {
        view.onSave(() -> guardar());
        view.onCancel(() -> navergarAlDashboard());
        view.onVolver(() -> navergarAlDashboard());
    }

    private void guardar() {
        List<Organizador> todosOrgs = listarUsuariosService.executeByOrganizadores();
        List<Asistente> todosAsis = listarUsuariosService.executeByAsistentes();

        List<Organizador> orgs = todosOrgs.stream()
                .filter(o -> view.getSelectedOrganizadores().contains(o))
                .collect(Collectors.toList());
        List<Asistente> asis = todosAsis.stream()
                .filter(a -> view.getSelectedAsistentes().contains(a))
                .collect(Collectors.toList());

        if (eventoOriginal == null) {
            crearSvc.execute(
                    view.getTitulo(),
                    view.getFecha(),
                    view.getUbicacion(),
                    view.getDescripcion(),
                    orgs,
                    asis);
        } else {
            modificarSvc.execute(
                    eventoOriginal.getId(),
                    view.getTitulo(),
                    view.getFecha(),
                    view.getUbicacion(),
                    view.getDescripcion(),
                    orgs,
                    asis);
        }
        navergarAlDashboard();
    }

    private void navergarAlDashboard() {
        DashboardView dashView = new DashboardView();
        new DashboardPresenter(
                mainFrame,
                authService,
                listarSvc,
                listarUsuariosService,
                crearSvc,
                modificarSvc,
                eliminarSvc,
                registrarSvc,
                cancelarService,
                notificarSvc,
                dashView,
                usuario);
        mainFrame.showPanel(dashView);
    }
}
