package com.philfedor.chip8.display;

public class BaseDisplay implements Chip8Display {
    protected boolean[][] pixels;
    protected int width;
    protected int height;

    public BaseDisplay(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new boolean[width][height];
    }

    public void clear() {
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                this.pixels[i][j] = false;
            }
        }
    }

    public boolean getPixel(int i, int j) {
        int w = i % this.width;
        int h = j % this.height;

        return this.pixels[w][h];
    }

    public void setPixel(int i, int j, boolean pixel) {
        int w = i % this.width;
        int h = j % this.height;

        this.pixels[w][h] = pixel;
    }
}
