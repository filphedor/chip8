package com.philfedor.chip8.display;

public class ConsoleDisplay extends BaseDisplay {
    public ConsoleDisplay(int width, int height) {
        super(width, height);
    }

    public String getScreen() {
        String screen = "";

        boolean[][] disp = this.getPixels();

        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                if (disp[j][i]) {
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
