package com.philfedor.chip8;

public class Display {
    private boolean[][] pixels;
    private int WIDTH = 64;
    private int HEIGHT = 32;

    public Display() {
        this.pixels = new boolean[WIDTH][HEIGHT];
    }

    public void clear() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                this.pixels[i][j] = false;
            }
        }
    }
    public boolean getPixel(int i, int j) {
        int w = i % WIDTH;
        int h = j % HEIGHT;
        return this.pixels[w][h];
    }

    public void setPixel(int i, int j, boolean pixel) {
        int w = i % WIDTH;
        int h = j % HEIGHT;

        this.pixels[w][h] = pixel;
    }

    public boolean[][] getPixels() {
        return this.pixels;
    }

    public String getScreen() {
        String screen = "";
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (this.pixels[j][i]) {
                    screen += "0";
                } else {
                    screen += " ";
                }
            }

            screen += "\n";
        }

        return screen;
    }
}
