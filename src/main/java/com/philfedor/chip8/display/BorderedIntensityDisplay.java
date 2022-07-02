package com.philfedor.chip8.display;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BorderedIntensityDisplay extends BaseDisplay implements ImageDisplay {
    private int pixelSize;
    private int border;
    private Color colorOn;
    private Color colorOff;
    private double[][] intensity;
    private int gain;
    private int loss;

    public BorderedIntensityDisplay(int width, int height, int pixelSize, int border, int gain, int loss, Color colorOn, Color colorOff) {
        super(width, height);

        this.pixelSize = pixelSize;
        this.border = border;
        this.colorOn = colorOn;
        this.colorOff = colorOff;
        this.gain = gain;
        this.loss = loss;

        this.intensity = new double[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                this.intensity[x][y] = 0;
            }
        }
    }

    public BufferedImage getFrame() {
        BufferedImage bufferedImage = new BufferedImage(width * (pixelSize + (border * 2)), height * (pixelSize + (border * 2)), BufferedImage.TYPE_INT_RGB);

        boolean[][] disp = this.getPixels();

        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                if (disp[x][y]) {
                    this.intensity[x][y] = Math.min(this.intensity[x][y] + gain, 100);
                } else {
                    this.intensity[x][y] = Math.max(this.intensity[x][y] - loss, 0) ;
                }

                int red = (int) ((this.colorOn.getRed() - this.colorOff.getRed()) * (this.intensity[x][y] / 100.0)) + this.colorOff.getRed();
                int green = (int) ((this.colorOn.getGreen() - this.colorOff.getGreen()) * (this.intensity[x][y] / 100.0)) + this.colorOff.getGreen();
                int blue = (int) ((this.colorOn.getBlue() - this.colorOff.getBlue()) * (this.intensity[x][y] / 100.0)) + this.colorOff.getBlue();

                this.drawPixel(x, y, new Color(red, green, blue), bufferedImage);
            }
        }

        return bufferedImage;
    }

    private void drawPixel(int x, int y, Color color, BufferedImage bufferedImage) {
        for (int i = 0; i < this.pixelSize + (this.border * 2); i++) {
            for (int j = 0; j < this.pixelSize + (this.border * 2); j++) {
                if ((i < this.border) || (i > (this.pixelSize + this.border)) || (j < this.border) || (j > (this.pixelSize + this.border))) {
                    bufferedImage.setRGB(x * (pixelSize + (this.border * 2)) + i, y * (pixelSize + (this.border * 2)) + j, this.colorOff.getRGB());
                } else {
                    bufferedImage.setRGB(x * (pixelSize + (this.border * 2)) + i, y * (pixelSize + (this.border * 2)) + j, color.getRGB());
                }
            }
        }
    }

}
