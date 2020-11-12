package model;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

/**
 * Used to denote the type of addressing mode used by an instruction.
 * Will be used by the CPU to retrieve the correct operand.
 * @author Group 8, Lead: Walter Kagel
 * @version 10/27/2020
 */
public enum AddressingMode {
    I, D, N, S, SF, X, SX, SXF
}
