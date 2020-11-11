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

    byte instruction = 0;

    void setInstruction(byte instruction) {
        this.instruction = instruction;
    }

    int getLSBit() {
        return (instruction & 0x1);
    }

    AddressingMode getAddresssingMode() {
        return AddressingMode.values()[(instruction & 0x7)];
    }

    int getUnsignedValue() {
        return Byte.toUnsignedInt(instruction);
    }
}
