package com.philfedor.chip8;

import com.philfedor.chip8.display.Chip8Display;
import org.apache.commons.codec.binary.Hex;

import java.util.HashMap;
import java.util.Stack;

public class Chip8Machine {
    final char[] DATA_REGISTERS = new char[]{
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
            'a',
            'b',
            'c',
            'd',
            'e',
            'f'
    };

    private int programCounter;
    private int addressRegister;
    private byte delayTimer;
    private byte soundTimer;
    private HashMap<String, Byte> dataRegisters;
    private Stack<Integer> stack;
    private Rom rom;
    private Chip8Display display;
    private Keyboard keyboard;
    private boolean isWaiting;

    public Chip8Machine(Rom rom, Chip8Display display, Keyboard keyboard) {
        this.programCounter = 0;
        this.addressRegister = 0;
        this.stack = new Stack<>();
        this.dataRegisters = new HashMap<>();
        this.isWaiting = false;
        this.delayTimer = 0;
        this.soundTimer = 0;

        this.rom = rom;
        this.display = display;
        this.keyboard = keyboard;

        for (int i = 0; i < DATA_REGISTERS.length; i++) {
            this.writeToDataRegister(DATA_REGISTERS[i], (byte) 0x00);
        }
    }

    public void setProgramCounter(int programCounter) {
        this.programCounter = programCounter;
    }

    public void step() {
        if (this.isWaiting) {
            return;
        }

        this.delayTimer = (byte) Math.max((this.delayTimer & 0xff) - 1, 0);
        this.soundTimer = (byte) Math.max((this.soundTimer & 0xff) - 1, 0);

        byte[] instruction = new byte[2];
        instruction[0] = rom.read(programCounter);
        instruction[1] = rom.read(programCounter + 1);

        String hexInstruction = Hex.encodeHexString(instruction);

        if (hexInstruction.equals("00e0")) {
            this.clearDisplay();

            this.programCounter += 2;
        }

        if (hexInstruction.equals("00ee")) {
            this.returnFromCall();
        }

        if (hexInstruction.charAt(0) == '1') {
            this.jump(Integer.parseInt(hexInstruction.substring(1, 4), 16));
        }

        if (hexInstruction.charAt(0) == '2') {
            this.call(Integer.parseInt(hexInstruction.substring(1, 4), 16));
        }

        if (hexInstruction.charAt(0) == '3') {
            this.skipIfEqual(hexInstruction.charAt(1), hexInstruction.substring(2, 4));
        }

        if (hexInstruction.charAt(0) == '4') {
            this.skipIfNotEqual(hexInstruction.charAt(1), hexInstruction.substring(2, 4));
        }

        if (hexInstruction.charAt(0) == '5') {
            this.skipIfRegsEqual(hexInstruction.charAt(1), hexInstruction.charAt(2));
        }

        if (hexInstruction.charAt(0) == '6') {
            char[] hexValue = new char[2];
            hexValue[0] = hexInstruction.charAt(2);
            hexValue[1] = hexInstruction.charAt(3);

            byte[] bites = new byte[1];

            try {
                bites = Hex.decodeHex(hexValue);
            } catch (Exception e) {
                System.out.println("Hex decode error for value: " + hexValue);
                e.printStackTrace();
            }

            this.writeToDataRegister(hexInstruction.charAt(1), bites[0]);

            this.programCounter += 2;
        }

        if (hexInstruction.charAt(0) == '7') {
            int constant = Integer.parseInt(hexInstruction.substring(2, 4), 16);

            int current = this.readFromDataRegister(hexInstruction.charAt(1)) & 0xff;

            int sum = current + constant;

            this.writeToDataRegister(hexInstruction.charAt(1),(byte) sum);

            this.programCounter += 2;
        }

        if (hexInstruction.charAt(0) == '8' && hexInstruction.charAt(3) == '0') {
            char x = hexInstruction.charAt(1);
            char y = hexInstruction.charAt(2);

            byte yByte = this.readFromDataRegister(y);

            this.writeToDataRegister(x,yByte);

            this.programCounter += 2;
        }

        if (hexInstruction.charAt(0) == '8' && hexInstruction.charAt(3) == '1') {
            char x = hexInstruction.charAt(1);
            char y = hexInstruction.charAt(2);

            byte xByte = this.readFromDataRegister(x);
            byte yByte = this.readFromDataRegister(y);

            byte result = (byte) (xByte | yByte);

            this.writeToDataRegister(x,result);

            this.programCounter += 2;
        }

        if (hexInstruction.charAt(0) == '8' && hexInstruction.charAt(3) == '2') {
            char x = hexInstruction.charAt(1);
            char y = hexInstruction.charAt(2);

            byte xByte = this.readFromDataRegister(x);
            byte yByte = this.readFromDataRegister(y);

            byte result = (byte) (xByte & yByte);

            this.writeToDataRegister(x,result);

            this.programCounter += 2;
        }

        if (hexInstruction.charAt(0) == '8' && hexInstruction.charAt(3) == '3') {
            char x = hexInstruction.charAt(1);
            char y = hexInstruction.charAt(2);

            byte xByte = this.readFromDataRegister(x);
            byte yByte = this.readFromDataRegister(y);

            byte result = (byte) (xByte ^ yByte);

            this.writeToDataRegister(x,result);

            this.programCounter += 2;
        }

        if (hexInstruction.charAt(0) == '8' && hexInstruction.charAt(3) == '4') {
            char x = hexInstruction.charAt(1);
            char y = hexInstruction.charAt(2);

            byte xByte = this.readFromDataRegister(x);
            byte yByte = this.readFromDataRegister(y);

            int sum = xByte + yByte;

            byte carry = (byte) (sum >> 8);

            this.writeToDataRegister(x, (byte) sum);
            this.writeToDataRegister('f', carry);

            this.programCounter += 2;
        }

        if (hexInstruction.charAt(0) == '8' && hexInstruction.charAt(3) == '5') {
            char x = hexInstruction.charAt(1);
            char y = hexInstruction.charAt(2);

            byte xByte = this.readFromDataRegister(x);
            byte yByte = this.readFromDataRegister(y);

            int sum = (xByte + 255) - yByte;

            byte carry = (byte) (sum >> 8);

            this.writeToDataRegister(x, (byte) sum);
            this.writeToDataRegister('f', carry);

            this.programCounter += 2;
        }

        if (hexInstruction.charAt(0) == '8' && hexInstruction.charAt(3) == '6') {
            char x = hexInstruction.charAt(1);

            byte xByte = this.readFromDataRegister(x);

            byte carry = (byte) (xByte & 0x01);

            int shift = xByte >> 1;

            this.writeToDataRegister(x, (byte) shift);
            this.writeToDataRegister('f', carry);

            this.programCounter += 2;
        }

        if (hexInstruction.charAt(0) == '8' && hexInstruction.charAt(3) == '7') {
            char x = hexInstruction.charAt(1);
            char y = hexInstruction.charAt(2);

            byte xByte = this.readFromDataRegister(x);
            byte yByte = this.readFromDataRegister(y);

            int sum = (yByte + 255) - xByte;

            byte carry = (byte) (sum >> 8);

            this.writeToDataRegister(x, (byte) sum);
            this.writeToDataRegister('f', carry);

            this.programCounter += 2;
        }

        if (hexInstruction.charAt(0) == '8' && hexInstruction.charAt(3) == 'e') {
            char x = hexInstruction.charAt(1);

            byte xByte = this.readFromDataRegister(x);

            byte carry = (byte) (xByte >> 8);

            int shift = xByte << 1;

            this.writeToDataRegister(x, (byte) shift);
            this.writeToDataRegister('f', carry);

            this.programCounter += 2;
        }

        if (hexInstruction.charAt(0) == '9') {
            this.skipIfRegsNotEqual(hexInstruction.charAt(1), hexInstruction.charAt(2));
        }

        if (hexInstruction.charAt(0) == 'a') {
            this.addressRegister = (Integer.parseInt(hexInstruction.substring(1, 4), 16));

            this.programCounter += 2;
        }

        if (hexInstruction.charAt(0) == 'b') {
            byte xByte = this.readFromDataRegister('0');
            int offset = Integer.parseInt(hexInstruction.substring(1, 4), 16);

            this.programCounter = (xByte & 0xff) + offset;
        }

        if (hexInstruction.charAt(0) == 'c') {
            char x = hexInstruction.charAt(1);
            int in = Integer.parseInt(hexInstruction.substring(1, 4), 16);

            int rand = (int) (Math.random() * 255);

            this.writeToDataRegister(x, (byte) (rand & in));

            this.programCounter += 2;
        }

        if (hexInstruction.charAt(0) == 'd') {
            char x = hexInstruction.charAt(1);
            char y = hexInstruction.charAt(2);

            int xLoc = (int) (this.readFromDataRegister(x) & 0xff);
            int yLoc = (int) (this.readFromDataRegister(y) & 0xff);
            int h = Integer.parseInt(hexInstruction.substring(3, 4), 16);

            boolean didFlip = false;

            for (int j = 0; j < h; j++) {
                byte data = rom.read(this.addressRegister + j);

                for (int b = 0; b < 8; b++) {
                    boolean d = ((data >> (7 -b)) & 0x01) == 1;

                    if (d) {
                        boolean current = display.getPixel(xLoc + b, yLoc + j);

                        this.display.setPixel(xLoc + b, yLoc + j, !current);

                        if (current) {
                            didFlip = true;
                        }
                    }
                }
            }

            if (didFlip) {
                this.writeToDataRegister('f', (byte) 0x01);
            } else {
                this.writeToDataRegister('f', (byte) 0x00);
            }

            this.programCounter += 2;
        }

        if (hexInstruction.charAt(0) == 'e' && hexInstruction.substring(2, 4).equals("9e")) {
            char x = hexInstruction.charAt(1);
            int key = (this.readFromDataRegister(x) & 0xff);

            if (this.keyboard.isKeyPressed(key)) {
                this.programCounter += 4;
            } else {
                this.programCounter += 2;
            }
        }

        if (hexInstruction.charAt(0) == 'e' && hexInstruction.substring(2, 4).equals("a1")) {
            char x = hexInstruction.charAt(1);
            int key = (this.readFromDataRegister(x) & 0xff);

            if (!this.keyboard.isKeyPressed(key)) {
                this.programCounter += 4;
            } else {
                this.programCounter += 2;
            }
        }

        if (hexInstruction.charAt(0) == 'f' && hexInstruction.substring(2, 4).equals("07")) {
            char x = hexInstruction.charAt(1);

            this.writeToDataRegister(x, (byte) this.delayTimer);

            this.programCounter += 2;
        }

        if (hexInstruction.charAt(0) == 'f' && hexInstruction.substring(2, 4).equals("0a")) {
            char x = hexInstruction.charAt(1);
            this.isWaiting = true;

            this.keyboard.waitForKey(kw -> {
                this.isWaiting = false;

                this.writeToDataRegister(x, (byte) kw);
            });

            this.programCounter += 2;
        }

        if (hexInstruction.charAt(0) == 'f' && hexInstruction.substring(2, 4).equals("15")) {
            char x = hexInstruction.charAt(1);

            this.delayTimer = this.readFromDataRegister(x);

            this.programCounter += 2;
        }

        if (hexInstruction.charAt(0) == 'f' && hexInstruction.substring(2, 4).equals("18")) {
            char x = hexInstruction.charAt(1);

            this.soundTimer = this.readFromDataRegister(x);

            this.programCounter += 2;
        }

        if (hexInstruction.charAt(0) == 'f' && hexInstruction.substring(2, 4).equals("1e")) {
            char x = hexInstruction.charAt(1);

            this.addressRegister += this.readFromDataRegister(x);

            this.programCounter += 2;
        }

        if (hexInstruction.charAt(0) == 'f' && hexInstruction.substring(2, 4).equals("29")) {
            char x = hexInstruction.charAt(1);

            this.addressRegister += this.readFromDataRegister(x);

            this.programCounter += 2;
        }

        if (hexInstruction.charAt(0) == 'f' && hexInstruction.substring(2, 4).equals("33")) {
            char x = hexInstruction.charAt(1);

            int num = this.readFromDataRegister(x) & 0xff;

            rom.write(this.addressRegister, (byte) Math.floor(num / 100.0));
            rom.write(this.addressRegister + 1, (byte) Math.floor((num % 100.0) / 10.0));
            rom.write(this.addressRegister + 2, (byte) Math.floor((num % 10.0)));

            this.programCounter += 2;
        }

        if (hexInstruction.charAt(0) == 'f' && hexInstruction.substring(2, 4).equals("55")) {
            String x = hexInstruction.substring(1, 2);

            int max = Integer.parseInt(x, 16);

            for (int i = 0; i <= max; i++) {
                this.rom.write(this.addressRegister + i, this.readFromDataRegister(Integer.toHexString(i).charAt(0)));
            }

            this.programCounter += 2;
        }

        if (hexInstruction.charAt(0) == 'f' && hexInstruction.substring(2, 4).equals("65")) {
            String x = hexInstruction.substring(1, 2);

            int max = Integer.parseInt(x, 16);

            for (int i = 0; i <= max; i++) {
                this.writeToDataRegister(Integer.toHexString(i).charAt(0), this.rom.read(this.addressRegister + i));
            }

            this.programCounter += 2;
        }
    }

    public void printState() {
        System.out.println("Program counter: " + programCounter);

        for (int i = 0; i < DATA_REGISTERS.length; i++) {
            byte data = this.readFromDataRegister(DATA_REGISTERS[i]);

            byte[] arrayData = new byte[1];
            arrayData[0] = data;

            String hexData = Hex.encodeHexString(arrayData);

            System.out.println(DATA_REGISTERS[i] + ": " + "0x" + hexData);

            this.programCounter += 1;
        }
    }

    public int getProgramCounter() {
        return this.programCounter;
    }

    public Byte readFromDataRegister(char register) {
        return this.dataRegisters.get(String.valueOf(register));
    }

    public void writeToDataRegister(char register, Byte data) {
        this.dataRegisters.put(String.valueOf(register), data);
    }

    private void clearDisplay() {
        this.display.clear();
    }

    private void returnFromCall() {
        Integer ret = this.stack.pop();

        this.programCounter = ret;
    }

    private void jump(int address) {
        this.programCounter = address;
    }

    private void call(int address) {
        this.stack.push(this.programCounter + 2);
        this.programCounter = address;
    }

    private void skipIfEqual(char register, String hexValue) {
        byte[] data = new byte[1];
        data[0] = this.readFromDataRegister(register);


        if (Hex.encodeHexString(data).equals(hexValue)) {
            this.programCounter += 4;
        } else {
            this.programCounter += 2;
        }
    }

    private void skipIfNotEqual(char register, String hexValue) {
        byte[] data = new byte[1];
        data[0] = this.readFromDataRegister(register);


        if (!Hex.encodeHexString(data).equals(hexValue)) {
            this.programCounter += 4;
        } else {
            this.programCounter += 2;
        }
    }

    private void skipIfRegsEqual(char register1, char register2) {
        if (this.readFromDataRegister(register1).equals(this.readFromDataRegister(register2))) {
            this.programCounter += 4;
        } else {
            this.programCounter += 2;
        }
    }

    private void skipIfRegsNotEqual(char register1, char register2) {
        if (!this.readFromDataRegister(register1).equals(this.readFromDataRegister(register2))) {
            this.programCounter += 4;
        } else {
            this.programCounter += 2;
        }
    }
}
