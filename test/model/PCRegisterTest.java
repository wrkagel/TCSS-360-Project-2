package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

/**
 * Tests the PCRegister Classes unique methods.
 * @author Group 8, Lead: Walter
 */
class PCRegisterTest {

    /**
     * The PCRegister upon which the tests will be performed.
     */
    private final PCRegister pc = new PCRegister();

    /**
     * Tests that the pc can be advanced normally.
     */
    @Test
    void advanceNormal() {
        pc.advance(3);
        assertEquals((short) 3, pc.getShort());
    }

    /**
     * Tests that the pc can overflow if it advances by too large of a number.
     */
    @Test
    void advanceOverflow() {
        pc.advance(3);
        pc.advance(65535);
        assertEquals((short) 2, pc.getShort());
    }
}