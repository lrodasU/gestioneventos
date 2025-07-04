package com.gestioneventos.ui.view;

import com.gestioneventos.ui.component.CustomTable;
import com.gestioneventos.ui.component.RoundedButton;
import com.gestioneventos.ui.util.UIConstants;
import com.gestioneventos.domain.Evento;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;

// Vista para mostrar detalle de un evento.
public class DetalleEventoView extends JPanel {
    private final JButton backButton;
    private final JLabel tituloLabel = new JLabel();
    private final JLabel fechaLabel = new JLabel();
    private final JLabel ubicacionLabel = new JLabel();
    private final JLabel descripcionLabel = new JLabel();
    private final CustomTable asistenciaTable;
    private final DefaultTableModel asistenciaModel;
    private final CustomTable organizadoresTable;
    private final DefaultTableModel organizadoresModel;
    private final RoundedButton modificarButton = new RoundedButton("Modificar");
    private final RoundedButton eliminarButton = new RoundedButton("Eliminar");
    private final RoundedButton cancelarButton = new RoundedButton("Darse de baja");

    public DetalleEventoView() {
        setLayout(new BorderLayout());
        setBackground(UIConstants.BACKGROUND_COLOR);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setPreferredSize(new Dimension(0, UIConstants.TOPBAR_HEIGHT));
        topBar.setBackground(UIConstants.TOPBAR_COLOR);
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        backButton = new RoundedButton("Volver");
        topBar.add(backButton, BorderLayout.WEST);
        JLabel headerLabel = new JLabel("Detalle Evento", SwingConstants.CENTER);
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setFont(headerLabel.getFont().deriveFont(Font.BOLD, UIConstants.TITLE_FONT_SIZE));
        topBar.add(headerLabel, BorderLayout.CENTER);
        topBar.add(Box.createHorizontalStrut(backButton.getPreferredSize().width), BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(UIConstants.BACKGROUND_COLOR);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel datosPanel = new JPanel();
        datosPanel.setLayout(new BoxLayout(datosPanel, BoxLayout.Y_AXIS));
        datosPanel.setBackground(UIConstants.BACKGROUND_COLOR);

        tituloLabel.setFont(tituloLabel.getFont().deriveFont(Font.BOLD, UIConstants.LABEL_FONT_SIZE));
        fechaLabel.setFont(fechaLabel.getFont().deriveFont(UIConstants.LABEL_FONT_SIZE));
        ubicacionLabel.setFont(ubicacionLabel.getFont().deriveFont(UIConstants.LABEL_FONT_SIZE));
        descripcionLabel.setFont(descripcionLabel.getFont().deriveFont(UIConstants.LABEL_FONT_SIZE));

        datosPanel.add(tituloLabel);
        datosPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        datosPanel.add(fechaLabel);
        datosPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        datosPanel.add(ubicacionLabel);
        datosPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        datosPanel.add(descripcionLabel);
        datosPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        content.add(datosPanel, BorderLayout.NORTH);

        asistenciaModel = new DefaultTableModel(new String[] { "#", "Nombre" }, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        asistenciaTable = new CustomTable(asistenciaModel);
        asistenciaTable.setBackground(UIConstants.BACKGROUND_COLOR);
        asistenciaTable.setGridColor(UIConstants.BACKGROUND_COLOR);
        JScrollPane asisScroll = new JScrollPane(asistenciaTable);
        asisScroll.setBorder(BorderFactory.createTitledBorder("Asistentes"));
        TableColumnModel asisCols = asistenciaTable.getColumnModel();
        asisCols.getColumn(0).setMaxWidth(50);
        asisCols.getColumn(1).setPreferredWidth(asisScroll.getPreferredSize().width - 50);

        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) asistenciaTable.getTableHeader()
                .getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        organizadoresModel = new DefaultTableModel(new String[] { "#", "Nombre" }, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        organizadoresTable = new CustomTable(organizadoresModel);
        organizadoresTable.setBackground(UIConstants.BACKGROUND_COLOR);
        organizadoresTable.setGridColor(UIConstants.BACKGROUND_COLOR);
        JScrollPane orgScroll = new JScrollPane(organizadoresTable);
        orgScroll.setBorder(BorderFactory.createTitledBorder("Organizadores"));

        TableColumnModel orgCols = organizadoresTable.getColumnModel();
        orgCols.getColumn(0).setMaxWidth(50);
        orgCols.getColumn(1).setPreferredWidth(orgScroll.getPreferredSize().width - 50);

        headerRenderer = (DefaultTableCellRenderer) organizadoresTable.getTableHeader()
                .getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel tablasPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        tablasPanel.setBackground(UIConstants.BACKGROUND_COLOR);
        tablasPanel.add(asisScroll);
        tablasPanel.add(orgScroll);

        content.add(tablasPanel, BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttons.add(modificarButton);
        buttons.add(eliminarButton);
        buttons.add(cancelarButton);
        add(buttons, BorderLayout.SOUTH);
    }

    public void onBack(Runnable handler) {
        backButton.addActionListener(e -> handler.run());
    }

    public void onModificar(Runnable handler) {
        modificarButton.addActionListener(e -> handler.run());
    }

    public void onEliminar(Runnable handler) {
        eliminarButton.addActionListener(e -> handler.run());
    }

    public void onCancelar(Runnable handler) {
        cancelarButton.addActionListener(e -> handler.run());
    }

    public void setBotonesOrganizador(boolean isOrg) {
        modificarButton.setVisible(isOrg);
        eliminarButton.setVisible(isOrg);
    }

    public void setBotonesAsistente(boolean isAsist) {
        cancelarButton.setVisible(isAsist);
    }

    public void setEvento(Evento e) {
        tituloLabel.setText("Título: " + e.getTitulo());
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        fechaLabel.setText("Fecha: " + e.getFecha().format(fmt));
        ubicacionLabel.setText("Ubicación: " + e.getUbicacion());
        descripcionLabel.setText(e.getDescripcion());

        asistenciaModel.setRowCount(0);
        int ai = 1;
        for (var a : e.getAsistentes()) {
            asistenciaModel.addRow(new Object[] { ai++, a.getNombre() });
        }
        organizadoresModel.setRowCount(0);
        int oi = 1;
        for (var o : e.getOrganizadores()) {
            organizadoresModel.addRow(new Object[] { oi++, o.getNombre() });
        }
    }
}
