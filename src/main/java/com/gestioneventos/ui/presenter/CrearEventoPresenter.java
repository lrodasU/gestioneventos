package com.gestioneventos.ui.presenter;

import com.gestioneventos.application.AuthService;
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private final NotificarService notificarSvc;
    private final List<Organizador> organizadores;
    private final List<Asistente> asistentes;
    private final CrearEventoView view;
    private final Usuario usuario;
    private final Evento eventoOriginal;

    public CrearEventoPresenter(
            MainFrame mainFrame,
            AuthService authService,
            ListarEventosService listarSvc,
            ListarUsuariosService listarUsuariosService,
            CrearEventoService crearSvc,
            ModificarEventoService modificarSvc,
            EliminarEventoService eliminarSvc,
            RegistrarAsistenciaService registrarSvc,
            NotificarService notificarSvc,
            List<Organizador> organizadores,
            List<Asistente> asistentes,
            CrearEventoView view,
            Usuario usuario,
            Evento eventoACambiar // null = modo crear / no null = modo editar
    ) {
        this.mainFrame = mainFrame;
        this.authService = authService;
        this.listarSvc = listarSvc;
        this.listarUsuariosService = listarUsuariosService;
        this.crearSvc = crearSvc;
        this.modificarSvc = modificarSvc;
        this.eliminarSvc = eliminarSvc;
        this.registrarSvc = registrarSvc;
        this.notificarSvc = notificarSvc;
        this.organizadores = organizadores;
        this.asistentes = asistentes;
        this.view = view;
        this.usuario = usuario;
        this.eventoOriginal = eventoACambiar;

        inicializar();
        bind();
    }

    private void inicializar() {
        List<Organizador> todosOrgs = listarUsuariosService.executeByOrganizadores();
        List<Asistente> todosAsis = listarUsuariosService.executeByAsistentes();

        organizadores.clear();
        organizadores.addAll(todosOrgs);
        asistentes.clear();
        asistentes.addAll(todosAsis);

        view.setOrganizadores(organizadores);
        view.setAsistentes(asistentes);

        if (eventoOriginal != null) {
            view.loadEvento(eventoOriginal);
        }
    }

    private void bind() {
        view.onSave(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardar();
            }
        });
        view.onCancel(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navergarAlDashboard();
            }
        });
    }

    private void guardar() {
        // Filtrar seleccionados
        List<Organizador> orgs = organizadores.stream()
                .filter(o -> view.getSelectedOrganizadores().contains(o))
                .collect(Collectors.toList());
        List<Asistente> asis = asistentes.stream()
                .filter(a -> view.getSelectedAsistentes().contains(a))
                .collect(Collectors.toList());

        if (eventoOriginal == null) {
            // Crear nuevo evento
            crearSvc.execute(
                    view.getTitulo(),
                    view.getFecha(),
                    view.getUbicacion(),
                    view.getDescripcion(),
                    orgs,
                    asis);
        } else {
            // Modificar evento existente
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
                notificarSvc,
                dashView,
                usuario);
        mainFrame.showPanel(dashView);
    }
}
