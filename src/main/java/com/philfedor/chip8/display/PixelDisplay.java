package com.philfedor.chip8.display;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PixelDisplay extends BaseDisplay implements ImageDisplay {
    private int pixelSize;
    private Color colorOn;
    private Color colorOff;

    public PixelDisplay(int width, int height, int pixelSize, Color colorOn, Color colorOff) {
        super(width, height);

        this.pixelSize = pixelSize;
        this.colorOn = colorOn;
        this.colorOff = colorOff;
    }

    public BufferedImage getFrame() {
        BufferedImage bufferedImage = new BufferedImage(width * pixelSize, height * pixelSize, BufferedImage.TYPE_INT_RGB);

        boolean[][] disp = this.getPixels();

        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                if (disp[x][y]) {
                    this.drawPixel(x, y, this.colorOn, bufferedImage);
                } else {
                    this.drawPixel(x, y, this.colorOff, bufferedImage);
                }
            }
        }

        return bufferedImage;
    }

    private void drawPixel(int x, int y, Color color, BufferedImage bufferedImage) {
        for (int i = 0; i < this.pixelSize; i++) {
            for (int j = 0; j < this.pixelSize; j++) {
                bufferedImage.setRGB(x * pixelSize + i, y* pixelSize + j, color.getRGB());
            }
        }
    }
}
