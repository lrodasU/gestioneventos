package com.gestioneventos.ui.view;

import com.gestioneventos.ui.component.CustomTable;
import com.gestioneventos.ui.component.RoundedButton;
import com.gestioneventos.ui.util.UIConstants;
import com.gestioneventos.domain.Evento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

// Pantalla principal de la aplicación
public class DashboardView extends JPanel {
    private final JLabel usuarioLabel = new JLabel();
    private final RoundedButton logoutButton = new RoundedButton("Cerrar Sesión");
    private final RoundedButton crearButton = new RoundedButton("Crear Evento");
    private final RoundedButton detallesButton = new RoundedButton("Ver Detalles");
    private final JTable eventosTable;
    private final DefaultTableModel tableModel;
    private Consumer<Integer> doubleClickHandler;
    private Consumer<Integer> detallesHandler;

    public DashboardView() {
        setLayout(new BorderLayout());

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setPreferredSize(new Dimension(0, UIConstants.TOPBAR_HEIGHT));
        topBar.setBackground(UIConstants.TOPBAR_COLOR);
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        usuarioLabel.setFont(usuarioLabel.getFont().deriveFont(UIConstants.LABEL_FONT_SIZE));
        topBar.add(usuarioLabel, BorderLayout.WEST);
        topBar.add(logoutButton, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        JPanel actionBar = new JPanel();
        actionBar.setLayout(new BoxLayout(actionBar, BoxLayout.Y_AXIS));
        actionBar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        Dimension btnSize = crearButton.getPreferredSize();
        crearButton.setPreferredSize(btnSize);
        crearButton.setMaximumSize(btnSize);
        crearButton.setMinimumSize(btnSize);
        detallesButton.setMaximumSize(btnSize);
        detallesButton.setMinimumSize(btnSize);

        actionBar.add(crearButton);
        actionBar.add(Box.createRigidArea(new Dimension(0, 10)));
        actionBar.add(detallesButton);
        btnSize = crearButton.getPreferredSize();
        detallesButton.setPreferredSize(btnSize);
        add(actionBar, BorderLayout.WEST);

        String[] cols = { "Título", "Fecha", "Ubicación", "Estado" };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        eventosTable = new CustomTable(tableModel);
        eventosTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(eventosTable);

        eventosTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && !e.isConsumed()) {
                    int row = eventosTable.rowAtPoint(e.getPoint());
                    if (row >= 0 && doubleClickHandler != null) {
                        doubleClickHandler.accept(row);
                    }
                }
            }
        });

        add(scrollPane, BorderLayout.CENTER);

        detallesButton.addActionListener(e -> {
            int sel = eventosTable.getSelectedRow();
            if (sel >= 0 && detallesHandler != null) {
                detallesHandler.accept(sel);
            }
        });
    }

    public void setUser(String nombreUsuario) {
        usuarioLabel.setText("Hola, " + nombreUsuario);
    }

    public void setEventos(List<Evento> eventos) {
        tableModel.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Evento e : eventos) {
            String estado;
            if (e.getFecha().isBefore(java.time.LocalDate.now())) {
                estado = "Completado";
            } else if (e.getFecha().isEqual(java.time.LocalDate.now())) {
                estado = "Hoy";
            } else {
                estado = "Próximo";
            }
            tableModel.addRow(new Object[] {
                    e.getTitulo(),
                    e.getFecha().format(fmt),
                    e.getUbicacion(),
                    estado
            });
        }
    }

    public void setCrearVisible(boolean visible) {
        crearButton.setVisible(visible);
    }

    public void onCrearEvent(Runnable handler) {
        crearButton.addActionListener(e -> handler.run());
    }

    public void onLogout(Runnable handler) {
        logoutButton.addActionListener(e -> handler.run());
    }

    public void onEventoDobleClick(Consumer<Integer> handler) {
        this.doubleClickHandler = handler;
    }

    public void onVerDetalles(Consumer<Integer> handler) {
        this.detallesHandler = handler;
    }
}
