package com.gestioneventos.ui.presenter;

import com.gestioneventos.application.AuthService;
import com.gestioneventos.application.CancelarAsistenciaService;
import com.gestioneventos.application.ListarEventosService;
import com.gestioneventos.application.ListarUsuariosService;
import com.gestioneventos.application.CrearEventoService;
import com.gestioneventos.application.ModificarEventoService;
import com.gestioneventos.application.EliminarEventoService;
import com.gestioneventos.application.RegistrarAsistenciaService;
import com.gestioneventos.application.NotificarService;
import com.gestioneventos.domain.Usuario;
import com.gestioneventos.ui.view.DashboardView;
import com.gestioneventos.ui.view.LoginView;
import com.gestioneventos.ui.MainFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Es el Presenter del login, usa AuthService para validar el login
// y dirige al dashboard si el login es correcto
public class LoginPresenter {
    private final AuthService authService;
    private final LoginView view;
    private final MainFrame mainFrame;

    private final ListarEventosService listarEventosService;
    private final ListarUsuariosService listarUsuariosService;
    private final CrearEventoService crearEventoService;
    private final ModificarEventoService modificarEventoService;
    private final EliminarEventoService eliminarEventoService;
    private final RegistrarAsistenciaService registrarAsistenciaService;
    private final CancelarAsistenciaService cancelarService;
    private final NotificarService notificarService;

    public LoginPresenter(AuthService authService,
            LoginView view,
            MainFrame mainFrame,
            ListarEventosService listarEventosService,
            ListarUsuariosService listarUsuariosService,
            CrearEventoService crearEventoService,
            ModificarEventoService modificarEventoService,
            EliminarEventoService eliminarEventoService,
            RegistrarAsistenciaService registrarAsistenciaService,
            CancelarAsistenciaService cancelarService,
            NotificarService notificarService) {
        this.authService = authService;
        this.view = view;
        this.mainFrame = mainFrame;
        this.listarEventosService = listarEventosService;
        this.listarUsuariosService = listarUsuariosService;
        this.crearEventoService = crearEventoService;
        this.modificarEventoService = modificarEventoService;
        this.eliminarEventoService = eliminarEventoService;
        this.registrarAsistenciaService = registrarAsistenciaService;
        this.cancelarService = cancelarService;
        this.notificarService = notificarService;

        // Registra el listener de login
        this.view.onLogin(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
    }

    // Maneja la logica del login
    private void login() {
        view.limpiarError();
        // Intenta autenticar el email y contraseña
        try {
            String email = view.getEmail();
            String password = view.getPassword();
            Usuario usuario = authService.execute(email, password);
            DashboardView dashboardView = new DashboardView();
            new DashboardPresenter(mainFrame,
                    authService,
                    listarEventosService,
                    listarUsuariosService,
                    crearEventoService,
                    modificarEventoService,
                    eliminarEventoService,
                    registrarAsistenciaService,
                    cancelarService,
                    notificarService,
                    dashboardView,
                    usuario);
            mainFrame.showPanel(dashboardView);
        } catch (Exception e) {
            // Login incorrecto
            view.mostrarError(e.getMessage());
        }
    }
}