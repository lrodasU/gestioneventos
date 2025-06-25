package com.gestioneventos.ui.view;

import com.gestioneventos.ui.component.RoundedButton;
import com.gestioneventos.ui.util.UIConstants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;
import com.gestioneventos.domain.Evento;

// Pantalla principal de la aplicacion
public class DashboardView extends JPanel {
    private final JLabel userLabel = new JLabel();
    private final RoundedButton logoutButton = new RoundedButton("Cerrar Sesión");
    private final RoundedButton createButton = new RoundedButton("Crear Evento");
    private final JTable eventsTable;
    private final DefaultTableModel tableModel;

    public DashboardView() {
        setLayout(new BorderLayout());

        // Barra superior
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.LIGHT_GRAY);
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        userLabel.setFont(userLabel.getFont().deriveFont(UIConstants.LABEL_FONT_SIZE));
        topBar.add(userLabel, BorderLayout.WEST);
        topBar.add(logoutButton, BorderLayout.EAST);

        // Botón crear evento
        JPanel actionBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        actionBar.add(createButton);

        // Tabla de eventos
        String[] cols = { "Título", "Fecha", "Ubicación", "Estado" };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        eventsTable = new JTable(tableModel);
        eventsTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(eventsTable);

        // Detección de doble click en fila
        eventsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && !e.isConsumed()) {
                    int row = eventsTable.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        fireEventDoubleClick(row);
                    }
                }
            }
        });

        // Agregar todo al layout
        add(topBar, BorderLayout.NORTH);
        add(actionBar, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
    }

    /** Establece el usuario autenticado para mostrarlo. */
    public void setUser(String nombreUsuario) {
        userLabel.setText("Hola, " + nombreUsuario);
    }

    /** Añade la lista de eventos a la tabla, calculando su estado. */
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

    /** Registra listener para creación de evento. */
    public void onCreateEvent(Runnable handler) {
        createButton.addActionListener(e -> handler.run());
    }

    /** Registra listener para logout. */
    public void onLogout(Runnable handler) {
        logoutButton.addActionListener(e -> handler.run());
    }

    /** Obtiene el índice de la fila seleccionada. */
    public int getSelectedIndex() {
        return eventsTable.getSelectedRow();
    }

    /** Callback interno para doble click. */
    private Consumer<Integer> doubleClickHandler;

    /** Registra listener para doble click en un evento (fila). */
    public void onEventDoubleClick(Consumer<Integer> handler) {
        this.doubleClickHandler = handler;
    }

    private void fireEventDoubleClick(int row) {
        if (doubleClickHandler != null) {
            doubleClickHandler.accept(row);
        }
    }
}
