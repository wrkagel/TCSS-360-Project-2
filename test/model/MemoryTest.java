package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

/**
 * Tests the load and store operation of the Memory class.
 * @author Group8, Lead: Walter Kagel
 * @version 10/26/2020
 */
class MemoryTest {

    private Memory mem = new Memory();

    /**
     * Tests that the a byte can be stored and loaded from the minimum valid address 0x0000.
     */
    @Test
    void testStorageAtMinAddress() {
        mem.storeByte((short) 0, (byte) 0xD1);
        assertEquals((byte) 0xD1, mem.loadByte((short) 0));
    }

    /**
     * Tests that a byte can be stored and loaded from the maximum valid address 0xFFFF.
     */
    @Test
    void testStorageAtMaxAddress() {
        mem.storeByte((short) 0xFFFF, (byte) 0x4E);
        assertEquals((byte) 0x4E, mem.loadByte((short) 0xFFFF));
    }

    /**
     * First test that a byte can be stored and loaded from a non-extreme address.
     */
    @Test
    void testStorageAtMiddleAddress1() {
        mem.storeByte((short) 0x045F, (byte) 0x04);
        assertEquals((byte) 0x04, mem.loadByte((short) 0x045F));
    }

    /**
     * Second test that a byte can be stored and loaded from a non-extreme address.
     */
    @Test
    void testStorageAtMiddleAddress2() {
        mem.storeByte((short) 0x89A1, (byte) 0x93);
        assertEquals((byte) 0x93, mem.loadByte((short) 0x89A1));
    }
}