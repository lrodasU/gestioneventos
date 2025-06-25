package com.gestioneventos.ui;

import javax.swing.*;
import java.awt.*;

// Ventana principal que contiene y muestra las distintas vistas
// usando un CardLayout para navegar entre ellas
public class MainFrame extends JFrame {
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel container = new JPanel(cardLayout);

    public MainFrame() {
        super("Gestión de Eventos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1440, 810);
        setLocationRelativeTo(null);

        setContentPane(container);
    }

    // Agrega una nueva vista al contenedor y la muestra
    public void showPanel(JPanel view) {
        String name = view.getClass().getSimpleName();
        if (container.getComponentCount() == 0 ||
                SwingUtilities.getAncestorOfClass(view.getClass(), container) == null) {
            container.add(view, name);
        }
        cardLayout.show(container, name);
    }
}
