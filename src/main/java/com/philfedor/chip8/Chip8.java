package com.philfedor.chip8;

import com.philfedor.chip8.display.ConsoleDisplay;
import com.philfedor.chip8.display.PixelDisplay;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Chip8 {
    public static void main(String[] args) {
        String fileName = args[0];

        Path path = Paths.get("roms/" + fileName);

        byte[] romBytes;

        try {
            romBytes = Files.readAllBytes(path);
        } catch (Exception e) {
            romBytes = new byte[0];
            System.out.println("Error reading rom:");
            e.printStackTrace();
        }

        Rom rom = new Rom(romBytes, 512);

        HashMap<Integer, Integer> keys = new HashMap<>();
        keys.put(96, 0); // 0
        keys.put(97, 1);
        keys.put(98, 2);
        keys.put(99, 3);
        keys.put(100, 4);
        keys.put(101, 5);
        keys.put(102, 6);
        keys.put(103, 7);
        keys.put(104, 8);
        keys.put(105, 9); // 9
        keys.put(111, 10); // /
        keys.put(106, 11); // *
        keys.put(109, 12); // -
        keys.put(107, 13); // +
        keys.put(10, 14); // \n
        keys.put(110, 15); // .

        int width = 64;
        int height = 32;
        int pixelSize = 10;

        Keyboard keyboard = new Keyboard(keys);
        PixelDisplay display = new PixelDisplay(width, height, pixelSize, new Color(255, 255, 255), new Color(0, 0, 0));

        Chip8Machine machine = new Chip8Machine(rom, display, keyboard);
        machine.setProgramCounter(512);

        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setPreferredSize(new Dimension(width * pixelSize, height * pixelSize));
        jFrame.pack();
        jFrame.setVisible(true);

        while (true) {
            machine.step();

            BufferedImage frame = display.getFrame();
            jFrame.getGraphics().drawImage(frame, 0, 0, null);
        }
    }
}
