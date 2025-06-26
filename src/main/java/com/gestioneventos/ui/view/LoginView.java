package com.gestioneventos.ui.view;

import com.gestioneventos.ui.component.*;
import com.gestioneventos.ui.util.UIConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

// Panel de login, lee email y contraseña y usa un listener
// para el boton de login
public class LoginView extends JPanel {
    private final RoundedTextField emailField = new RoundedTextField(20);
    private final RoundedPasswordField passwordField = new RoundedPasswordField(20);
    private final JButton loginButton = new RoundedButton("Iniciar sesión");
    private final JLabel errorLabel = new JLabel(" ");

    public LoginView() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel = new JLabel("Bienvenido");
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 40f));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(emailLabel.getFont().deriveFont(Font.PLAIN, 22f));
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        emailField.setFont(emailField.getFont().deriveFont(22f));
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setFont(passwordLabel.getFont().deriveFont(Font.PLAIN, 22f));
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField.setFont(passwordField.getFont().deriveFont(22f));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);

        loginButton.setFont(loginButton.getFont().deriveFont(Font.PLAIN, 22f));
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.setMaximumSize(loginButton.getPreferredSize());

        errorLabel.setForeground(UIConstants.ERROR_COLOR);
        errorLabel.setFont(errorLabel.getFont().deriveFont(Font.PLAIN, UIConstants.ERROR_FONT_SIZE));
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        content.add(titleLabel);
        content.add(Box.createRigidArea(new Dimension(0, 25)));
        content.add(emailLabel);
        content.add(Box.createRigidArea(new Dimension(0, 5)));
        content.add(emailField);
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(passwordLabel);
        content.add(Box.createRigidArea(new Dimension(0, 5)));
        content.add(passwordField);
        content.add(Box.createRigidArea(new Dimension(0, 25)));
        content.add(loginButton);
        content.add(Box.createRigidArea(new Dimension(0, 15)));
        content.add(errorLabel);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(UIConstants.BACKGROUND_COLOR);
        wrapper.add(content);

        add(wrapper, BorderLayout.CENTER);

        emailField.addActionListener(e -> loginButton.doClick());
        passwordField.addActionListener(e -> loginButton.doClick());
    }

    public void onLogin(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public String getEmail() {
        return emailField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void mostrarError(String message) {
        errorLabel.setText(message);
    }

    public void limpiarError() {
        errorLabel.setText(" ");
    }
}
