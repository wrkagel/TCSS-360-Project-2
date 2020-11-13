package model;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

/**
 * The instruction register class that stores and returns the instruction byte.
 * @author Group 8, Lead: Walter Kagel
 * @version 11/6/2020
 */
class InstructionRegister {

    /**
     * Stores the single byte that is the instruction specifier
     */
    byte instruction = 0;

    /**
     * Set the instruction specifier based on the byte given.
     * @param instruction first byte of any data read as an instruction.
     */
    void setInstruction(byte instruction) {
        this.instruction = instruction;
    }

    /**
     * Grabs only the least significant bit. Used for determining something for a couple instructions.
     * @return
     */
    int getLSBit() {
        return (instruction & 0x1);
    }

    /**
     * Returns the 3 least significant digits as an integer. This is the addressing mode for most instructions that
     * require one.
     * @return
     */
    AddressingMode getAddresssingMode() {
        return AddressingMode.values()[(instruction & 0x7)];
    }

    /**
     * Returns the instruction values as an unsigned int.
     * @return
     */
    int getUnsignedValue() {
        return Byte.toUnsignedInt(instruction);
    }
}
