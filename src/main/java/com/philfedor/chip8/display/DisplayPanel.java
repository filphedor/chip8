package com.philfedor.chip8.display;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DisplayPanel extends JPanel {
    private ImageDisplay display;

    public DisplayPanel(ImageDisplay display) {
        super();
        this.display = display;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D g2d = (Graphics2D) g;

        BufferedImage frame = display.getFrame();
        g2d.drawImage(frame, 0, 0, null);
    }
}
