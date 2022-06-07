package com.philfedor.chip8;

import org.apache.commons.codec.binary.Hex;

import java.util.ArrayList;

public class Rom {
    private ArrayList<Byte> data;

    //offset used to emulate original interpreter location,
    //at the first 511 bytes
    public Rom(byte[] data, int offset) {
        this.data = new ArrayList<>();

        for (int i = 0; i < 4096; i++) {
            this.data.add((byte) 0);
        }

        for (int i = 0; i < data.length; i++) {
            this.data.set(i + offset, data[i]);
        }
    }

    private void writeFonts() {

    }

    public byte read(int location) {
        Byte mem = this.data.get(location);

        if (mem == null) {
            return (byte) 0;
        }

        return mem;
    }

    public void write(int location, byte bite) {
        this.data.set(location, bite);
    }

    public void print() {
        for (int i = 0; i * 2 < (this.data.size() - 1); i++) {
            byte[] mem = new byte[2];
            mem[0] = this.data.get(i * 2);
            mem[1] = this.data.get((i * 2) + 1);

            String hexInstruction = Hex.encodeHexString(mem);

            System.out.println(hexInstruction);
        }
    }



    private byte[] toByteArray(int data) {
        byte bite1 = (byte) data;
        byte bite2 = (byte) (data >> 8);

        return new byte[]{
                bite2,
                bite1
        };
    }
}
