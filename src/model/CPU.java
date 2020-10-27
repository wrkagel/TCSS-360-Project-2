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
    private final ALU alu;

    /**
     * Used as the pep/8 accumulator register.
     */
    private final Register accumulator;

    /**
     * Used as the pep/8 index register.
     */
    private final Register index;

    /**
     * Used as the pep/8 stack pointer.
     */
    private final Register stackPointer;

    /**
     * Used as the pep/8 program counter;
     */
    private final Register programCounter;

    /**
     * Used to store the instruction specifier for an instruction.
     * Uses only the least significant byte of the register as the instruction specifier is
     * only 8 bits.
     */
    private final Register instructionSpecifier;

    /**
     * Used to store the operand specifier for an instruction.
     */
    private final Register operandSpecifier;

    /**
     * Used to store the operand for the instruction. Could be the operand specifier if the instrucion
     * specifier indicates immediate addressing.
     */
    private final Register operand;

    /**
     * Stores the value that the most recent instruction that can set the negative flag has set it to.
     */
    private final boolean negativeFlag;

    /**
     * Stores the value that the most recent instruction that can set the zero flag has set it to.
     */
    private final boolean zeroFlag;

    /**
     * Stores the value that the most recent instruction that can set the overflow flag has set it to.
     */
    private final boolean overflowFlag;

    /**
     * Stores the value that the most recent instruction that can set the carry flag has set it to.
     */
    private final boolean carryFlag;

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
     * Performs one iteration of the fetch execute cycle. Uses the instruction specifier to call an appropriate
     * method. Returns true if the STOP instruction is encountered.
     * @param mem the memory that the cpu will fetch instructions from and read and write data to.
     * @return true if instruction read was STOP, false otherwise.
     */
    public boolean fetchExecute(Memory mem) {
        short instructionAddress = programCounter.getShort();
        instructionSpecifier.setByte(false, mem.loadByte(instructionAddress));
        programCounter.setShort((short) (instructionAddress + 1));
        short instSpec = instructionSpecifier.getShort();
        if(instSpec == 0) {
            return stopInstruction();
        } else if (instSpec == 1){
            //returnFromTrap();
        } else if (instSpec == 2) {
            moveSPtoAcc();
        } else if (instSpec == 3) {
            moveFlagstoAcc();
        } else if (instSpec < 22) {
            branch();
        } else if (instSpec < 24) {
            callSubroutine();
        } else if (instSpec < 36) {
            unaryALUop();
        } else if (instSpec < 48) {
            //no operation trap calls
        } else if (instSpec < 64) {
            decimalIO();
        } else if (instSpec < 72) {
            stringOut();
        } else if (instSpec < 88) {
            charIO();
        } else if (instSpec < 96) {
            returnFromCall();
        } else if (instSpec < 192) {
            binaryALUop();
        } else if (instSpec < 224) {
            load();
        } else if (instSpec < 256) {
            store();
        } else {
            listener.errorMessage("No valid opCode found. This message should never be reached.");
        }
        return false;
    }

    private void moveSPtoAcc() {
    }

    private void moveFlagstoAcc() {
    }

    private void branch() {
    }

    private void callSubroutine() {
    }

    private void unaryALUop() {
    }

    private void decimalIO() {
    }

    private void stringOut() {
    }

    private void charIO() {
    }

    private void returnFromCall() {
    }

    private void binaryALUop() {
    }

    private void load() {
    }

    private void store() {
    }

    private boolean stopInstruction() {
        listener.registerUpdate("programCounter", programCounter.getShort());
        listener.registerUpdate("instructionSpecifier", instructionSpecifier.getShort());
        listener.registerUpdate("operandSpecifier", null);
        listener.registerUpdate("operand", null);
        return true;
    }
}
