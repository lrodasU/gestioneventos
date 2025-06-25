package com.gestioneventos.ui.view;

import com.gestioneventos.ui.component.RoundedButton;
import com.gestioneventos.ui.util.UIConstants;
import com.gestioneventos.domain.Evento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;

//Vista para mostrar detalle de un evento.
public class DetalleEventoView extends JPanel {
    private final JLabel tituloLabel = new JLabel();
    private final JLabel fechaLabel = new JLabel();
    private final JLabel ubicacionLabel = new JLabel();
    private final JTextArea descripcionArea = new JTextArea();
    private final JTable asistenciaTable;
    private final DefaultTableModel tableModel;
    private final RoundedButton modificarButton = new RoundedButton("Modificar");
    private final RoundedButton eliminarButton = new RoundedButton("Eliminar");

    public DetalleEventoView() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.LIGHT_GRAY);
        JButton backButton = new RoundedButton("Volver");
        topBar.add(backButton, BorderLayout.WEST);
        JLabel header = new JLabel("Detalle Evento", SwingConstants.CENTER);
        header.setFont(header.getFont().deriveFont(Font.BOLD, UIConstants.TITLE_FONT_SIZE));
        topBar.add(header, BorderLayout.CENTER);
        add(topBar, BorderLayout.NORTH);

        JPanel detallesPanel = new JPanel();
        detallesPanel.setLayout(new BoxLayout(detallesPanel, BoxLayout.Y_AXIS));
        detallesPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        tituloLabel.setFont(tituloLabel.getFont().deriveFont(Font.BOLD, UIConstants.LABEL_FONT_SIZE));
        fechaLabel.setFont(fechaLabel.getFont().deriveFont(UIConstants.LABEL_FONT_SIZE));
        ubicacionLabel.setFont(ubicacionLabel.getFont().deriveFont(UIConstants.LABEL_FONT_SIZE));

        descripcionArea.setFont(descripcionArea.getFont().deriveFont(UIConstants.LABEL_FONT_SIZE));
        descripcionArea.setLineWrap(true);
        descripcionArea.setWrapStyleWord(true);
        descripcionArea.setEditable(false);
        JScrollPane descScroll = new JScrollPane(descripcionArea);
        descScroll.setBorder(BorderFactory.createEmptyBorder());

        detallesPanel.add(tituloLabel);
        detallesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        detallesPanel.add(fechaLabel);
        detallesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        detallesPanel.add(ubicacionLabel);
        detallesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        detallesPanel.add(descScroll);
        detallesPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        String[] cols = { "#", "Nombre" };
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        asistenciaTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(asistenciaTable);
        tableScroll.setPreferredSize(new Dimension(300, 150));

        detallesPanel.add(new JLabel("Asistentes:"));
        detallesPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        detallesPanel.add(tableScroll);

        add(detallesPanel, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttons.add(modificarButton);
        buttons.add(eliminarButton);
        add(buttons, BorderLayout.SOUTH);

        this.backButton = backButton;
    }

    private final JButton backButton;

    public void onBack(Runnable handler) {
        backButton.addActionListener(e -> handler.run());
    }

    public void setEvento(Evento e) {
        tituloLabel.setText("Título: " + e.getTitulo());
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        fechaLabel.setText("Fecha: " + e.getFecha().format(fmt));
        ubicacionLabel.setText("Ubicación: " + e.getUbicacion());
        descripcionArea.setText(e.getDescripcion());

        tableModel.setRowCount(0);
        int idx = 1;
        for (var a : e.getAsistentes()) {
            tableModel.addRow(new Object[] { idx++, a.getNombre() });
        }
    }

    public void onModificar(Runnable handler) {
        modificarButton.addActionListener(e -> handler.run());
    }

    public void onEliminar(Runnable handler) {
        eliminarButton.addActionListener(e -> handler.run());
    }
}
