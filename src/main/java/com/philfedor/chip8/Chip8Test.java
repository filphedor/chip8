//package com.philfedor.chip8;
//
//import java.io.File;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//
//public class Chip8Test {
//    static Keyboard keyboard = new Keyboard(null);
//    public static void test() {
//        assertPassed(clearDisplay());
//        assertPassed(goToTest());
//        assertPassed(setConstantTest());
//        assertPassed(callReturnTest());
//        assertPassed(conditionalTest());
//        assertPassed(addNoCarryTest());
//        assertPassed(regAssignmentTest());
//        assertPassed(bitOpTest());
//    }
//
//    private static boolean clearDisplay() {
//        Display display = new Display();
//        display.setPixel(2, 2, true);
//
//        Rom rom = loadRom("clearDisplay.ch8", 0);
//        Chip8Machine chip8Machine = new Chip8Machine(display, keyboard);
//        chip8Machine.runRom(rom, 0);
//
//        boolean[][] displayPixels = display.getPixels();
//
//        for (int i = 0; i < displayPixels.length; i++) {
//            for (int j = 0; j < displayPixels[i].length; j++) {
//                if (displayPixels[i][j]) {
//                    return false;
//                }
//            }
//        }
//
//        return true;
//    }
//
//    private static boolean goToTest() {
//        Display display = new Display();
//        display.setPixel(2, 2, true);
//
//        Rom rom = loadRom("goto.ch8", 0);
//        Chip8Machine chip8Machine = new Chip8Machine(display, keyboard);
//        chip8Machine.runRom(rom, 0);
//
//        boolean[][] displayPixels = display.getPixels();
//
//        for (int i = 0; i < displayPixels.length; i++) {
//            for (int j = 0; j < displayPixels[i].length; j++) {
//                if (displayPixels[i][j]) {
//                    return false;
//                }
//            }
//        }
//
//        return true;
//    }
//
//    private static boolean setConstantTest() {
//        Display display = new Display();
//
//        Rom rom = loadRom("setConstant.ch8", 0);
//        Chip8Machine chip8Machine = new Chip8Machine(display, keyboard);
//        chip8Machine.runRom(rom, 0);
//
//        byte result = chip8Machine.readFromDataRegister('0');
//
//        return result == 3;
//    }
//
//    private static boolean callReturnTest() {
//        Display display = new Display();
//
//        Rom rom = loadRom("callReturn.ch8", 0);
//        Chip8Machine chip8Machine = new Chip8Machine(display, keyboard);
//        chip8Machine.runRom(rom, 0);
//
//        byte result0 = chip8Machine.readFromDataRegister('0');
//        byte result1 = chip8Machine.readFromDataRegister('1');
//
//        return result0 == 3 && result1 == 4;
//    }
//
//    private static boolean conditionalTest() {
//        Display display = new Display();
//
//        Rom rom = loadRom("conditional.ch8", 0);
//        Chip8Machine chip8Machine = new Chip8Machine(display, keyboard);
//        chip8Machine.runRom(rom, 0);
//
//        byte result0 = chip8Machine.readFromDataRegister('0');
//        byte result1 = chip8Machine.readFromDataRegister('1');
//        byte result2 = chip8Machine.readFromDataRegister('2');
//
//        return result0 == 3 && result1 == 4 && result2 == 5;
//    }
//
//    private static boolean addNoCarryTest() {
//        Display display = new Display();
//
//        Rom rom = loadRom("addNoCarry.ch8", 0);
//        Chip8Machine chip8Machine = new Chip8Machine(display, keyboard);
//        chip8Machine.runRom(rom, 0);
//
//        byte result0 = chip8Machine.readFromDataRegister('0');
//        byte resultF = chip8Machine.readFromDataRegister('f');
//
//        return result0 == -2 && resultF == 0;
//    }
//
//    private static boolean regAssignmentTest() {
//        Display display = new Display();
//
//        Rom rom = loadRom("regAssignment.ch8", 0);
//        Chip8Machine chip8Machine = new Chip8Machine(display, keyboard);
//        chip8Machine.runRom(rom, 0);
//
//        byte result1 = chip8Machine.readFromDataRegister('1');
//
//        return result1 == 3;
//    }
//
//    private static boolean bitOpTest() {
//        Display display = new Display();
//
//        Rom rom = loadRom("bitOp.ch8", 0);
//        Chip8Machine chip8Machine = new Chip8Machine(display, keyboard);
//        chip8Machine.runRom(rom, 0);
//
//        byte result0 = chip8Machine.readFromDataRegister('0');
//        byte result2 = chip8Machine.readFromDataRegister('2');
//        byte result4 = chip8Machine.readFromDataRegister('4');
//
//        return result0 == -1 && result2 == 1 && result4 == -2;
//    }
//
//    private static void assertPassed(boolean test) {
//        if (test) {
//            System.out.println("Passed");
//        } else {
//            System.out.println("Failed");
//        }
//    }
//
//    private static Rom loadRom(String fileName, int offset) {
//        Path path = Paths.get("roms/" + fileName);
//
//        byte[] romBytes = new byte[0];
//
//        try {
//            File file = new File(path.toString());
//            romBytes = Files.readAllBytes(file.toPath());
//        } catch (Exception e) {
//            System.out.println("Error reading rom:");
//            e.printStackTrace();
//        }
//
//        return new Rom(romBytes, offset);
//    }
//}
