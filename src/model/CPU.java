package model;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

import control.ModelListener;

/**
 * Processes the instructions stored in memory by performing the fetch-execute cycle for a single instruction.
 * Contains all pep/8 internal registers.
 * @author Group 8, Lead: Walter Kagel
 * @version 10/25/2020
 */
public class CPU {

    /**
     * The arithmetic and logic unit for the cpu.
     */
    private ALU alu;

    /**
     * Used as the pep/8 accumulator register.
     */
    private Register accumulator;

    /**
     * Used as the pep/8 index register.
     */
    private Register index;

    /**
     * Used as the pep/8 stack pointer.
     */
    private Register stackPointer;

    /**
     * Used as the pep/8 program counter;
     */
    private Register programCounter;

    /**
     * Used to store the instruction specifier for an instruction.
     */
    private Register instructionSpecifier;

    /**
     * Used to store the operand specifier for an instruction.
     */
    private Register operandSpecifier;

    /**
     * Used to store the operand for the instruction. Could be the operand specifier if the instrucion
     * specifier indicates immediate addressing.
     */
    private Register operand;

    /**
     * Stores the value that the most recent instruction that can set the negative flag has set it to.
     */
    private boolean negativeFlag;

    /**
     * Stores the value that the most recent instruction that can set the zero flag has set it to.
     */
    private boolean zeroFlag;

    /**
     * Stores the value that the most recent instruction that can set the overflow flag has set it to.
     */
    private boolean overflowFlag;

    /**
     * Stores the value that the most recent instruction that can set the carry flag has set it to.
     */
    private boolean carryFlag;

    /**
     * The cpu informs the listener when an update occurs to a stored value.
     */
    private ModelListener listener;

    public CPU(){
        alu = new ALU();
        accumulator = new Register();
        index = new Register();
        stackPointer = new Register();
        programCounter = new Register();
        instructionSpecifier = new Register();
        operandSpecifier = new Register();
        operand = new Register();
        negativeFlag = false;
        zeroFlag = true;
        overflowFlag = false;
        carryFlag = false;
    }

    /**
     * Adds the listener that will be informed of updates to stored values.
     * @param listener object that implements the ModelListener interface.
     */
    public void addListener(ModelListener listener) {
        this.listener = listener;
    }

    /**
     * Performs one iteration of the fetch execute cycle.
     * @param mem the memory that the cpu will fetch instructions from and read and write data to.
     * @return true if instruction read was STOP, false otherwise.
     */
    public boolean fetchExecute(Memory mem) {
        return true;
    }

}
