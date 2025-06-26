package com.gestioneventos.ui.component;

import com.gestioneventos.ui.util.UIConstants;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedList<E> extends JList<E> {

    public RoundedList(ListModel<E> model) {
        super(model);
        setOpaque(false);
        setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected,
                    boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                lbl.setOpaque(isSelected);
                return lbl;
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(UIConstants.FIELD_BACKGROUND_COLOR);
        g2.fill(new RoundRectangle2D.Float(
                0, 0,
                getWidth() - 1,
                getHeight() - 1,
                UIConstants.FIELD_RADIUS,
                UIConstants.FIELD_RADIUS));
        super.paintComponent(g2);
        g2.dispose();
    }
}
