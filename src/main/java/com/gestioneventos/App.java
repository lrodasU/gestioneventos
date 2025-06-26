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
        JsonStorageAdapter storageAdapter = new JsonStorageAdapter(
                "gestioneventos/data/usuarios.json",
                "gestioneventos/data/eventos.json");

        EmailAdapter emailAdapterPrueba = new EmailAdapter() {
            // Dado el contexto de prueba, no se enviarán correos reales.
            // Se implementó una clase para encargarse de enviar correos reales
            // de forma que pueda ser utilizado en un contexto real.
            // Como todos los usuarios tienen emails falsos, esta clase imprime 
            // el contenido del mail en consola.
            @Override
            public void sendEmail(String to, String subject, String body) {
                System.out.println("Email enviado para: " + to + " - " + subject + "\n" + body);
            }
        };

        AuthService authService = new AuthService(storageAdapter);
        ListarEventosService listarEventosService = new ListarEventosService(storageAdapter);
        ListarUsuariosService listarUsuariosService = new ListarUsuariosService(storageAdapter);
        CrearEventoService crearService = new CrearEventoService(storageAdapter);
        ModificarEventoService modificarService = new ModificarEventoService(storageAdapter);
        EliminarEventoService eliminarService = new EliminarEventoService(storageAdapter);
        RegistrarAsistenciaService registrarService = new RegistrarAsistenciaService(storageAdapter);
        CancelarAsistenciaService cancelarService = new CancelarAsistenciaService(storageAdapter);
        NotificarService notificarService = new NotificarService(emailAdapterPrueba);

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
