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
 * @version 10/27/2020
 */
class MemoryTest {

    private Memory mem = new Memory();

    /**
     * Tests that the a byte can be stored and loaded from the minimum valid address 0x0000.
     */
    @Test
    void testStorageAtMinAddress() {
        mem.setByte((short) 0, (byte) 0xD1);
        assertEquals((byte) 0xD1, mem.getByte((short) 0));
    }

    /**
     * Tests that a byte can be stored and loaded from the maximum valid address 0xFFFF.
     */
    @Test
    void testStorageAtMaxAddress() {
        mem.setByte((short) 0xFFFF, (byte) 0x4E);
        assertEquals((byte) 0x4E, mem.getByte((short) 0xFFFF));
    }

    /**
     * First test that a byte can be stored and loaded from a non-extreme address.
     */
    @Test
    void testStorageAtMiddleAddress1() {
        mem.setByte((short) 0x045F, (byte) 0x04);
        assertEquals((byte) 0x04, mem.getByte((short) 0x045F));
    }

    /**
     * Second test that a byte can be stored and loaded from a non-extreme address.
     */
    @Test
    void testStorageAtMiddleAddress2() {
        mem.setByte((short) 0x89A1, (byte) 0x93);
        assertEquals((byte) 0x93, mem.getByte((short) 0x89A1));
    }

    /**
     * Tests that storing and getting shorts works properly.
     */
    @Test
    void testStorageOfShort() {
        mem.setShort((short) 0x3345, (short) 3456);
        assertEquals((short) 3456, mem.getShort((short) 0x3345));
    }

    /**
     * Tests that setting memory with bytes produces the correct short.
     */
    @Test
    void testSetByteGetShort() {
        mem.setByte((short) 0x5677, (byte) 0x56);
        mem.setByte((short) 0x5678, (byte) 0x22);
        assertEquals((short) 0x5622, mem.getShort((short) 0x5677));
    }

    /**
     * Tests that setting memory with a short produces the correct bytes.
     */
    @Test
    void testSetShortGetBytes() {
        mem.setShort((short) 0x1FFF, (short) 0x0F45);
        assertEquals((byte) 0x0F, mem.getByte((short) 0x1FFF));
        assertEquals((byte) 0x45, mem.getByte((short)0x2000));
    }

    /**
     * Tests that attempting to store a short that would go outside of memory throws an error.
     */
    @Test
    void testErrorStoringShort() {
        assertThrows(IllegalArgumentException.class, () -> mem.setShort((short) 0xFFFF, (short) 5));
    }

    /**
     * Tests that attempting to load a short that would go outside of memory throws an error.
     */
    @Test
    void testErrorLoadingShort() {
        assertThrows(IllegalArgumentException.class, () -> mem.getShort((short) 0xFFFF));
    }
}