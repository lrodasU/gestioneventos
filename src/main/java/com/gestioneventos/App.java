package com.gestioneventos;

import com.gestioneventos.application.AuthService;
import com.gestioneventos.application.CancelarAsistenciaService;
import com.gestioneventos.application.CrearEventoService;
import com.gestioneventos.application.EliminarEventoService;
import com.gestioneventos.application.ListarEventosService;
import com.gestioneventos.application.ListarUsuariosService;
import com.gestioneventos.application.ModificarEventoService;
import com.gestioneventos.application.NotificarService;
import com.gestioneventos.application.RegistrarAsistenciaService;
import com.gestioneventos.infrastructure.JsonStorageAdapter;
import com.gestioneventos.infrastructure.EmailAdapter;
import com.gestioneventos.ui.MainFrame;
import com.gestioneventos.ui.view.LoginView;
import com.gestioneventos.ui.presenter.LoginPresenter;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        JsonStorageAdapter storage = new JsonStorageAdapter(
                "gestioneventos/data/usuarios.json",
                "gestioneventos/data/eventos.json");

        EmailAdapter dummyMailer = new EmailAdapter() {
            @Override
            public void sendEmail(String to, String subject, String body) {
                // No-op para pruebas tempranas
            }
        };

        AuthService authService = new AuthService(storage);
        ListarEventosService listarEventosService = new ListarEventosService(storage);
        ListarUsuariosService listarUsuariosService = new ListarUsuariosService(storage);
        CrearEventoService crearService = new CrearEventoService(storage);
        ModificarEventoService modificarService = new ModificarEventoService(storage);
        EliminarEventoService eliminarService = new EliminarEventoService(storage);
        RegistrarAsistenciaService registrarService = new RegistrarAsistenciaService(storage);
        CancelarAsistenciaService cancelarService = new CancelarAsistenciaService(storage);
        NotificarService notificarService = new NotificarService(dummyMailer);

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();

            LoginView loginView = new LoginView();
            new LoginPresenter(
                    authService,
                    loginView,
                    mainFrame,
                    listarEventosService,
                    listarUsuariosService,
                    crearService,
                    modificarService,
                    eliminarService,
                    registrarService,
                    cancelarService,
                    notificarService);

            mainFrame.showPanel(loginView);
            mainFrame.setVisible(true);
        });
    }
}
