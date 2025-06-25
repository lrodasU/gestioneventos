package com.gestioneventos;

import com.gestioneventos.application.AuthService;
import com.gestioneventos.application.CrearEventoService;
import com.gestioneventos.application.EliminarEventoService;
import com.gestioneventos.application.ListarEventosService;
import com.gestioneventos.application.ListarUsuariosService;
import com.gestioneventos.application.ModificarEventoService;
import com.gestioneventos.application.NotificarService;
import com.gestioneventos.application.RegistrarAsistenciaService;
import com.gestioneventos.domain.Usuario;
import com.gestioneventos.infrastructure.JsonStorageAdapter;
import com.gestioneventos.infrastructure.EmailAdapter;
import com.gestioneventos.ui.MainFrame;
import com.gestioneventos.ui.view.LoginView;
import com.gestioneventos.ui.presenter.LoginPresenter;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {

        // 1. Configurar el adaptador de persistencia
        JsonStorageAdapter storage = new JsonStorageAdapter(
                "gestioneventos/data/usuarios.json",
                "gestioneventos/data/eventos.json");

        // 2. Configurar un EmailAdapter de prueba (no envía mails)
        EmailAdapter dummyMailer = new EmailAdapter() {
            @Override
            public void sendEmail(String to, String subject, String body) {
                // No-op para pruebas tempranas
            }
        };

        // 3. Crear los servicios
        AuthService authService = new AuthService(storage);
        ListarEventosService listarEventosService = new ListarEventosService(storage);
        ListarUsuariosService listarUsuariosService = new ListarUsuariosService(storage);
        CrearEventoService createService = new CrearEventoService(storage);
        ModificarEventoService updateService = new ModificarEventoService(storage);
        EliminarEventoService deleteService = new EliminarEventoService(storage);
        RegistrarAsistenciaService regService = new RegistrarAsistenciaService(storage);
        NotificarService notifService = new NotificarService(dummyMailer);

        // 4. Iniciar Swing en el hilo de eventos
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();

            // 5. Crear la vista y el presenter de login
            LoginView loginView = new LoginView();
            new LoginPresenter(
                    authService,
                    loginView,
                    mainFrame,
                    listarEventosService,
                    listarUsuariosService,
                    createService,
                    updateService,
                    deleteService,
                    regService,
                    notifService);

            // 6. Mostrar la vista de login
            mainFrame.showPanel(loginView);
            mainFrame.setVisible(true);
        });
    }
}
