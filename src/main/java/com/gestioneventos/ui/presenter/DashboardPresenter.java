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
import com.gestioneventos.domain.Usuario;
import com.gestioneventos.ui.MainFrame;
import com.gestioneventos.ui.view.DashboardView;
import com.gestioneventos.ui.view.DetalleEventoView;
import com.gestioneventos.ui.view.LoginView;
import com.gestioneventos.ui.view.CrearEventoView;

import java.util.List;

/**
 * Presenter para la pantalla de Dashboard.
 */
public class DashboardPresenter {
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
    private final DashboardView view;
    private final Usuario usuario;
    private List<Evento> eventos;

    public DashboardPresenter(MainFrame mainFrame,
            AuthService authService,
            ListarEventosService listarService,
            ListarUsuariosService listarUsuariosService,
            CrearEventoService crearService,
            ModificarEventoService modificarService,
            EliminarEventoService eliminarService,
            RegistrarAsistenciaService registrarService,
            CancelarAsistenciaService cancelarService,
            NotificarService notificarService,
            DashboardView view,
            Usuario usuario) {
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

        inicializar();
        bindActions();
        loadEventos();
    }

    private void inicializar() {
        view.setUser(usuario.getNombre());
        view.setCrearVisible(usuario instanceof Organizador);
    }

    private void bindActions() {
        view.onLogout(() -> {
            // Volver a pantalla de login
            LoginView loginView = new LoginView();
            new LoginPresenter(
                    authService,
                    loginView,
                    mainFrame,
                    listarService,
                    listarUsuariosService,
                    crearService,
                    modificarService,
                    eliminarService,
                    registrarService,
                    cancelarService,
                    notificarService);
            mainFrame.showPanel(loginView);
        });

        if (usuario instanceof Organizador) {
            view.onCrearEvent(() -> {
                // Navegar a creación de evento
                CrearEventoView crearView = new CrearEventoView();
                crearView.setModoCrear();
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
                        null);
                mainFrame.showPanel(crearView);
            });
        }

        view.onEventoDobleClick(row -> navegarADetalles(row));

        view.onVerDetalles(row -> navegarADetalles(row));
    }

    private void navegarADetalles(int row) {
        Evento e = eventos.get(row);
        DetalleEventoView detalleView = new DetalleEventoView();
        new DetalleEventoPresenter(mainFrame,
                authService,
                listarService,
                listarUsuariosService,
                crearService,
                modificarService,
                eliminarService,
                registrarService,
                cancelarService,
                notificarService,
                detalleView,
                usuario,
                e);
        mainFrame.showPanel(detalleView);
    }

    private void loadEventos() {
        eventos = listarService.execute(usuario.getId());
        view.setEventos(eventos);
    }
}
