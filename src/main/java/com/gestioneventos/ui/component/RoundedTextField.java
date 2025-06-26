package com.gestioneventos.ui.component;

import com.gestioneventos.ui.util.UIConstants;
import javax.swing.*;
import javax.swing.plaf.basic.BasicTextFieldUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

// JTextField con esquinas redondeadas y fondo personalizado.
public class RoundedTextField extends JTextField {

    public RoundedTextField(int columns) {
        super(columns);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(
                UIConstants.FIELD_VERTICAL_PADDING,
                UIConstants.FIELD_HORIZONTAL_PADDING,
                UIConstants.FIELD_VERTICAL_PADDING,
                UIConstants.FIELD_HORIZONTAL_PADDING));
        setFont(getFont().deriveFont(UIConstants.LABEL_FONT_SIZE));
        setUI(new BasicTextFieldUI());
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
