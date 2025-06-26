package com.gestioneventos.ui.component;

import com.gestioneventos.ui.util.UIConstants;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

// JTable estilizada para ajustarse al look & feel de la aplicación:
// - Filas más altas
// - Fuente consistente
// - Grid visible
// - Encabezados centrados
// - Celdas no editables
public class CustomTable extends JTable {
    public CustomTable(DefaultTableModel model) {
        super(model);
        setRowHeight(UIConstants.TABLE_ROW_HEIGHT);
        setFont(getFont().deriveFont(UIConstants.LABEL_FONT_SIZE));

        setShowGrid(true);
        setGridColor(UIConstants.GRID_COLOR);
        setBackground(Color.WHITE);

        getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

        JTableHeader header = getTableHeader();
        header.setFont(header.getFont().deriveFont(Font.BOLD, UIConstants.LABEL_FONT_SIZE));

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setHorizontalAlignment(DefaultTableCellRenderer.LEFT);
        setDefaultRenderer(Object.class, cellRenderer);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
