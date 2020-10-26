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
 * @version 10/26/2020
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
    public void storeByte(short address, byte value){
        mem[Short.toUnsignedInt(address)] = value;
    }

    /**
     * Returns the byte stored at the memory address given. The address will be read as an unsigned short,
     * but no conversion is necessary from the Java signed short e.g. a short with a value of -1 in Java
     * will address mem[65535]
     * @param address unsigned value of the given short that denotes memroy address
     * @return the byte value stored at that address.
     */
    public byte loadByte(short address){
        return mem[Short.toUnsignedInt(address)];
    }

}
