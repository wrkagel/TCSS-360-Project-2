package model;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

/**
 * The program counter. Fnctionally identical to a normal pep/8 register except that it contains the advance method
 * for moving the program counter forward.
 * @author Group 8, Lead: Walter
 * @version 11/07/2020
 */
class PCRegister extends Register{

    /**
     * Advances the pc register by n bytes. With the current implementation the pc will loop back to 0 after 0xFFFF.
     * This could allow for a program to run infinitely.
     * @param n the number of bytes to advance.
     */
    void advance(int n) {
        super.setShort((short) (super.getShort() + n));
    }

}
