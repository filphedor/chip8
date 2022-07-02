package com.philfedor.chip8.display;

import java.awt.image.BufferedImage;

public interface ImageDisplay extends Chip8Display {
    BufferedImage getFrame();
}
