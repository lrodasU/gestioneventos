package com.gestioneventos.ui.component;

import com.gestioneventos.ui.util.UIConstants;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedSpinner extends JSpinner {

    public RoundedSpinner(SpinnerModel model) {
        super(model);
        setOpaque(false);
        // eliminar border por defecto si es necesario
        setBorder(BorderFactory.createEmptyBorder(
                UIConstants.FIELD_VERTICAL_PADDING,
                UIConstants.FIELD_HORIZONTAL_PADDING,
                UIConstants.FIELD_VERTICAL_PADDING,
                UIConstants.FIELD_HORIZONTAL_PADDING));
        // Alineamos fuente
        ((JSpinner.DefaultEditor) getEditor()).getTextField()
                .setFont(getFont().deriveFont(UIConstants.LABEL_FONT_SIZE));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(UIConstants.BACKGROUND_COLOR);
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
