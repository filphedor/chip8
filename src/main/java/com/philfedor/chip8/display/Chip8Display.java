package com.philfedor.chip8.display;

public interface Chip8Display {
    void clear();

    boolean getPixel(int i, int j);

    void setPixel(int i, int j, boolean pixel);
}
