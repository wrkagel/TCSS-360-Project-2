package model;

import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

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
    private Register reg = new Register();

    @org.junit.jupiter.api.Test
    void getByte() {
        reg.setByte(true, (byte) 0xFF);
        assertEquals((byte) 0xFF, reg.getByte(true));
    }

    @org.junit.jupiter.api.Test
    void getShort() {
    }

    @org.junit.jupiter.api.Test
    void setByte() {
    }

    @org.junit.jupiter.api.Test
    void setShort() {
    }
}