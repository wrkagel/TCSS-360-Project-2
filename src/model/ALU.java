package model;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

/**
 * Executes all arithmetic operations for arithmetic instructions for the pep/8 simulator.
 * Stores flags based on the most recent operation.
 */
public class ALU {

    /**
     * Stores if the last operation resulted in a negative value.
     */
    private boolean negativeFlag = false;

    /**
     * Stores if the last operation resulted in a zero value.
     */
    private boolean zeroFlag = true;

    /**
     * Stores if the last operation caused on overflow.
     */
    private boolean overflowFlag = false;

    /**
     * Stores the carry bit for the last operation.
     */
    private boolean carryFlag = false;

    /**
     * Takes in two short values and returns their addition. Sets flags based on the result.
     * @return (short) (value1 + value2)
     */
    public short add(short value1, short value2) {

    }

    /**
     * Takes in a short value and rotates the rightmost bit to the the leftmost bit using the carry bit as
     * an intermediary.
     * @return rotated value as a short
     */
    public short rotateRight(short value){

    }
}
