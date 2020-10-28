package model;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

import java.util.Arrays;

/**
 * Model of memory for the pep/8 simulator.
 * @author Group 8, Lead: Walter Kagel
 * @version 10/27/2020
 */
public class Memory {

    /**
     * Holds the bytes in memory. The size allows for any valid unsigned short to be used as an address.
     */
    private byte[] mem = new byte[65536];

    /**
     * Takes in a memory address and sets that memory address to the given value.
     * The allowable memory addresses are all unsigned short values. The method
     * automatically converts the Java signed shorts to an unsigned value e.g.
     * a short with a value of -1 in Java will address mem[65535].
     * @param address unsigned value of the given short that denotes memory address.
     * @param value byte value to be stored in memory
     */
    public void setByte(short address, byte value){
        mem[Short.toUnsignedInt(address)] = value;
    }

    /**
     * Returns the byte stored at the memory address given. The address will be read as an unsigned short,
     * but no conversion is necessary from the Java signed short e.g. a short with a value of -1 in Java
     * will address mem[65535]
     * @param address unsigned value of the given short that denotes memroy address
     * @return the byte value stored at that address.
     */
    public byte getByte(short address){
        return mem[Short.toUnsignedInt(address)];
    }

    /**
     * Sets two consecutive bytes in storage to the given short value starting at the given address.
     * @param address place in memory to place the value
     * @param value value to be stored in memory
     */
    public void setShort(short address, short value) {
        int intAddress = Short.toUnsignedInt(address);
        if (intAddress >= mem.length -1) {
            throw new IllegalArgumentException("Addressing would go outside the bounds of memory.");
        }
        mem[intAddress] = (byte) ((value & 0xFF00) >>> 8);
        mem[intAddress + 1] = (byte) (value & 0xFF);
    }

    public short getShort(short address) {
        int intAddress = Short.toUnsignedInt(address);
        if (intAddress >= mem.length - 1) {
            throw new IllegalArgumentException("Addressing would go outside the bounds of memory.");
        }
        byte mostSig = mem[intAddress];
        byte leastSig = mem[intAddress + 1];
        return (short) ((mostSig << 8) | (leastSig & 0xFF));
    }

}
