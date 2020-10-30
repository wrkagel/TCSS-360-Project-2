package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

/**
 * Tests the register class.
 * @author Group8, Lead: Walter Kagel
 * @version 10/26/2020
 */
class RegisterTest {

    /**
     * Makes a register to be tested.
     */
    private final Register reg = new Register();

    /**
     * Tests that the most significant byte register holds and returns the maximum unsigned byte value.
     * i.e. stores and returns 0xFF.
     */
    @Test
    void testMaxUnsignedByte1() {
        reg.setByte(true, (byte) 0xFF);
        assertEquals((byte) 0xFF, reg.getByte(true));
    }

    /**
     * Tests that the most significant byte register stores and returns the minimum unsigned byte value.
     * i.e. stores and returns 0x00
     */
    @Test
    void testMinUnsignedByte1() {
        reg.setByte(true, (byte) 0);
        assertEquals((byte) 0, reg.getByte(true));
    }

    /**
     * Tests that the most significant byte register stores and returns the maximum signed byte value.
     * i.e. stores and returns 0x7F
     */
    @Test
    void testMaxSignedByte1() {
        reg.setByte(true, (byte) 0x7F);
        assertEquals((byte) 0x7F, reg.getByte(true));
    }

    /**
     * Tests that the most significant byte register stores and returns the minimum signed byte value.
     * i.e. stores and returns 0x80
     */
    @Test
    void testMinSignedByte1() {
        reg.setByte(true, (byte) 0x80);
        assertEquals((byte) 0x80, reg.getByte(true));
    }

    /**
     * Tests that the most significant byte register stores and returns a non-extreme value.
     */
    @Test
    void testMixedByte1() {
        reg.setByte(true, (byte) 0x4C);
        assertEquals((byte) 0x4C, reg.getByte(true));
    }

    /**
     * Tests that the least significant byte register holds and returns the maximum unsigned byte value.
     * i.e. stores and returns 0xFF.
     */
    @Test
    void testMaxUnsignedByte2() {
        reg.setByte(false, (byte) 0xFF);
        assertEquals((byte) 0xFF, reg.getByte(false));
    }

    /**
     * Tests that the least significant byte register stores and returns the minimum unsigned byte value.
     * i.e. stores and returns 0x00
     */
    @Test
    void testMinUnsignedByte2() {
        reg.setByte(false, (byte) 0);
        assertEquals((byte) 0, reg.getByte(false));
    }

    /**
     * Tests that the least significant byte register stores and returns the maximum signed byte value.
     * i.e. stores and returns 0x7F
     */
    @Test
    void testMaxSignedByte2() {
        reg.setByte(false, (byte) 0x7F);
        assertEquals((byte) 0x7F, reg.getByte(false));
    }

    /**
     * Tests that the least significant byte register stores and returns the minimum signed byte value.
     * i.e. stores and returns 0x80
     */
    @Test
    void testMinSignedByte2() {
        reg.setByte(false, (byte) 0x80);
        assertEquals((byte) 0x80, reg.getByte(false));
    }

    /**
     * Tests that the least significant byte register stores and returns a non-extreme value.
     */
    @Test
    void testMixedByte2() {
        reg.setByte(false, (byte) 0xC4);
        assertEquals((byte) 0xC4, reg.getByte(false));
    }

    /**
     * Tests that the register properly sets and returns the maximum unsigned short value.
     * i.e. stores and returns 0xFFFF
     */
    @Test
    void testMaxUnsignedShort() {
        reg.setShort((short) 0xFFFF);
        assertEquals((short) 0xFFFF, reg.getShort());
    }

    /**
     * Tests that the register properly sets and returns the minimum unsigned short value.
     * i.e. stores and returns 0x0000
     */
    @Test
    void testMinUnsignedShort() {
        reg.setShort((short) 0x0000);
        assertEquals((short) 0x0000, reg.getShort());
    }

    /**
     * Tests that the register properly sets and returns the maximum Signed short value.
     * i.e. stores and returns 0x7FFF
     */
    @Test
    void testMaxSignedShort() {
        reg.setShort((short) 0x7FFF);
        assertEquals((short) 0x7FFF, reg.getShort());
    }

    /**
     * Tests that the register properly sets and returns the minimum Signed short value.
     * i.e. stores and returns 0x8000
     */
    @Test
    void testMinSignedShort() {
        reg.setShort((short) 0x8000);
        assertEquals((short) 0x8000, reg.getShort());
    }

    /**
     * Tests that the register properly sets and returns a non-extreme value.
     */
    @Test
    void testMixedShort() {
        reg.setShort((short) 0x3D72);
        assertEquals((short) 0x3D72, reg.getShort());
    }

    /**
     * Tests that setting the two byte values causes the correct short to be returned.
     */
    @Test
    void testIfSettingBytesReturnsCorrectShort() {
        reg.setByte(true, (byte) 0x4C);
        reg.setByte(false, (byte) 0x07);
        assertEquals((short) 0x4c07, reg.getShort());
    }

    /**
     * Tests that setting the register value using a short returns the correct most significant byte.
     */
    @Test
    void testIfSettingShortReturnsCorrectByte1() {
        reg.setShort((short) 0x67C2);
        assertEquals((byte) 0x67, reg.getByte(true));
    }

    /**
     * Tests that setting the register value using a short returns the correct least significant byte.
     */
    @Test
    void testIfSettingShortReturnsCorrectByte2() {
        reg.setShort((short) 0x67C2);
        assertEquals((byte) 0xC2, reg.getByte(false));
    }
}