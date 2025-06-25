package com.gestioneventos.ui.component;

import com.gestioneventos.ui.util.UIConstants;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

//JButton con esquinas redondeadas y efecto hover.
public class RoundedButton extends JButton {

    private boolean hover;

    public RoundedButton(String text) {
        super(text);
        this.hover = false;

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setForeground(Color.BLACK);
        setFont(getFont().deriveFont(UIConstants.BUTTON_FONT_SIZE));
        setMargin(new Insets(
                UIConstants.BUTTON_VERTICAL_PADDING,
                UIConstants.BUTTON_HORIZONTAL_PADDING,
                UIConstants.BUTTON_VERTICAL_PADDING,
                UIConstants.BUTTON_HORIZONTAL_PADDING));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        Color fill = hover
                ? UIConstants.HOVER_COLOR
                : UIConstants.PRIMARY_COLOR;
        g2.setColor(fill);
        int width = getWidth();
        int height = getHeight();
        RoundRectangle2D.Float rr = new RoundRectangle2D.Float(
                0, 0,
                width - 1,
                height - 1,
                UIConstants.BUTTON_RADIUS,
                UIConstants.BUTTON_RADIUS);
        g2.fill(rr);

        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        int padW = UIConstants.BUTTON_RADIUS * 2
                + UIConstants.BUTTON_HORIZONTAL_PADDING * 2;
        int padH = UIConstants.BUTTON_RADIUS * 2
                + UIConstants.BUTTON_VERTICAL_PADDING * 2;
        d.width = Math.max(d.width, padW);
        d.height = Math.max(d.height, padH);
        return d;
    }
}
