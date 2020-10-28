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
 * @version 10/28/2020
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
    private boolean negativeFlag;

    /**
     * Stores the value that the most recent instruction that can set the zero flag has set it to.
     */
    private boolean zeroFlag;

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

    /**
     * Used to give the CPU access to memory for reading instructions, loading, and storing.
     */
    private Memory mem;

    /**
     * Sets if the CPU updates the listener after every instruction or only after a stop instruction.
     * Added to increase efficiency.
     */
    private boolean isTrace;

    /**
     * Create the cpu with its ALU, registers, and flags.
     */
    public CPU(Memory mem){
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
        listener = null;
        isTrace = false;
        this.mem = mem;
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
     * @return true if instruction read was STOP, false otherwise.
     */
    public boolean fetchExecute(boolean isTrace) {
        this.isTrace = isTrace;
        short instructionAddress = programCounter.getShort();
        instructionSpecifier.setByte(false, mem.getByte(instructionAddress));
        programCounter.setShort((short) (instructionAddress + 1));
        int instSpec = Short.toUnsignedInt(instructionSpecifier.getShort());
        if(instSpec == 0) {
            stopInstruction();
            return true;
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
            if (listener != null) {
                listener.errorMessage("No valid opCode found. This message should never be reached.");
            }
        }
        return false;
    }

    /**
     * Informs the listener of the register updates of a stop instruction and then returns true.
     */
    private void stopInstruction() {
        if (listener == null) return;
        listener.registerUpdate("programCounter", programCounter.getShort());
        listener.registerUpdate("instructionSpecifier", instructionSpecifier.getShort());
        listener.registerUpdate("operandSpecifier", null);
        listener.registerUpdate("operand", null);
    }

    /**
     * Moves the stack pointer into the accumulator.
     */
    private void moveSPtoAcc() {
        short value = stackPointer.getShort();
        accumulator.setShort(value);
        if(listener == null || !isTrace) return;
        listener.registerUpdate("accumulator", value);
        listener.registerUpdate("programCounter", programCounter.getShort());
        listener.registerUpdate("instructionSpecifier", instructionSpecifier.getShort());
        listener.registerUpdate("operandSpecifier", null);
        listener.registerUpdate("operand", null);
    }

    /**
     * Moves the nzvc flags into the accumulator with bits [0:11] being filled with zeroes.
     */
    private void moveFlagstoAcc() {
        short value = 0;
        if (negativeFlag) value += 8;
        if (zeroFlag) value += 4;
        if (overflowFlag) value+= 2;
        if (carryFlag) value += 1;
        accumulator.setShort(value);
        if (listener == null || !isTrace) return;
        listener.registerUpdate("accumulator", value);
        listener.registerUpdate("programCounter", programCounter.getShort());
        listener.registerUpdate("instructionSpecifier", instructionSpecifier.getShort());
        listener.registerUpdate("operandSpecifier", null);
        listener.registerUpdate("operand", null);
    }

    /**
     * Checks if branch condition specified by the instruction is met and then branches if it is.
     */
    private void branch() {
        boolean shouldBranch;
        int instSpec = Short.toUnsignedInt(instructionSpecifier.getShort());
        short opSpec = mem.getShort(programCounter.getShort());
        operandSpecifier.setShort(opSpec);
        programCounter.setShort((short) (programCounter.getShort() + 2));
        if (instSpec < 6) {
            shouldBranch = true;
        } else if (instSpec < 8) {
            shouldBranch = negativeFlag || zeroFlag;
        } else if (instSpec < 10) {
            shouldBranch = negativeFlag;
        } else if (instSpec < 12) {
            shouldBranch = zeroFlag;
        } else if (instSpec < 14) {
            shouldBranch = !zeroFlag;
        } else if (instSpec < 16) {
            shouldBranch = !negativeFlag;
        } else if (instSpec < 18) {
            shouldBranch = !(negativeFlag && zeroFlag);
        } else if (instSpec < 20) {
            shouldBranch = overflowFlag;
        } else if (instSpec < 22) {
            shouldBranch = carryFlag;
        } else {
            listener.errorMessage("Incorrectly went into branch function.");
            return;
        }
        short oper;
        if (shouldBranch) {
            if ((instSpec & 0x1) == 1) {
                oper = mem.getShort(getOperandAddress(AddressingMode.X));
            } else {
                oper = opSpec;
            }
            programCounter.setShort(oper);
        }
        if (listener == null || !isTrace) return;
        listener.registerUpdate("programCounter", programCounter.getShort());
        listener.registerUpdate("instructionSpecifier", instructionSpecifier.getShort());
        listener.registerUpdate("operandSpecifier", operandSpecifier.getShort());
        listener.registerUpdate("operand", operand.getShort());
    }

    /**
     * Calls a subroutine. Currently unimplemented.
     */
    private void callSubroutine() {
    }

    /**
     * Performs an ALU operation that only has one input.
     */
    private void unaryALUop() {
    }

    /**
     * Performs an input or output operation on a decimal value.
     */
    private void decimalIO() {
    }

    /**
     * Outputs a null terminated string based with the first character at the starting address
     * indicated by the operand.
     */
    private void stringOut() {
    }

    /**
     * Performs input or output of a single character.
     */
    private void charIO() {
    }

    /**
     * Returns from a call.
     */
    private void returnFromCall() {
    }

    /**
     * Performs an ALU operation with two inputs.
     */
    private void binaryALUop() {
    }

    /**
     * Loads a short or byte into the accumulator or index.
     */
    private void load() {
        operandSpecifier.setShort(mem.getShort(programCounter.getShort()));
        programCounter.setShort((short) (programCounter.getShort() + 2));
        int opCode = instructionSpecifier.getByte(false) >>> 4;
        int regAndMode = instructionSpecifier.getByte(false) & 0xF;
        int isByte = opCode & 0x1;
        int reg = regAndMode >>> 3;
        int mode = regAndMode & 0x7;
        if (isByte == 0) {
            if (mode == 0) {
                operand.setShort(operandSpecifier.getShort());
            } else {
                operand.setShort(mem.getShort(getOperandAddress(AddressingMode.values()[mode])));
            }
            if (reg == 0) {
                accumulator.setShort(operand.getShort());
            } else {
                index.setShort(operand.getShort());
            }
        } else {
            operand.setByte(true, (byte) 0);
            if (mode == 0) {
                operand.setByte(false, operandSpecifier.getByte(false));
            } else {
                operand.setByte(false, mem.getByte(getOperandAddress(AddressingMode.values()[mode])));
            }
            if (reg == 0) {
                accumulator.setByte(false, operand.getByte(false));
            } else {
                index.setByte(false, operand.getByte(false));
            }
        }
        if(operand.getShort() < 0) {
            negativeFlag = true;
        } else {
            negativeFlag = false;
        }
        if (operand.getShort() == 0) {
            zeroFlag = true;
        } else {
            zeroFlag = false;
        }
        if (listener == null || !isTrace) return;
        listener.registerUpdate("programCounter", programCounter.getShort());
        listener.registerUpdate("instructionSpecifier", instructionSpecifier.getShort());
        listener.registerUpdate("operandSpecifier", operandSpecifier.getShort());
        listener.registerUpdate("operand", operand.getShort());
        listener.registerUpdate("accumulator", accumulator.getShort());
        listener.registerUpdate("index", index.getShort());
        listener.flagUpdate("negativeFlag", negativeFlag);
        listener.flagUpdate("zeroFlag", zeroFlag);
    }

    /**
     * Stores a short or byte from the accumulator or index into a memory address.
     */
    private void store() {
        operandSpecifier.setShort(mem.getShort(programCounter.getShort()));
        programCounter.setShort((short) (programCounter.getShort() + 2));
        int opCode = instructionSpecifier.getByte(false) >>> 4;
        int regAndMode = instructionSpecifier.getByte(false) & 0xF;
        int isByte = opCode & 0x1;
        int reg = regAndMode >>> 3;
        int mode = regAndMode & 0x7;
        short address;
        if (mode == 0) throw new IllegalArgumentException("Illegal addressing mode for store instruction.");
        address = getOperandAddress(AddressingMode.values()[mode]);
        if (isByte == 0) {
            if (reg == 0) {
                operand.setShort(accumulator.getShort());
            } else {
                operand.setShort(index.getShort());
            }
            mem.setShort(address, operand.getShort());
        } else {
            operand.setByte(true, (byte) 0);
            if (reg == 0) {
                operand.setByte(false, accumulator.getByte(false));
            } else {
                operand.setByte(false, index.getByte(false));
            }
            mem.setByte(address, operand.getByte(false));
        }
        if (listener == null || !isTrace) return;
        listener.registerUpdate("programCounter", programCounter.getShort());
        listener.registerUpdate("instructionSpecifier", instructionSpecifier.getShort());
        listener.registerUpdate("operandSpecifier", operandSpecifier.getShort());
        listener.registerUpdate("operand", operand.getShort());
        if (isByte == 0) {
            listener.memoryUpdate(address, new byte[] {mem.getByte(address), mem.getByte((short) (address + 1))});
        } else {
            listener.memoryUpdate(address, mem.getByte(address));
        }
    }

    private short getOperandAddress(AddressingMode mode) {
        short address = operandSpecifier.getShort();
        switch(mode) {
            case I -> {
                listener.errorMessage("Immediate Mode does not require addressing. Error in code.");
                return address;
            }
            case N -> address = mem.getShort(address);
            case S -> address = (short) (stackPointer.getShort() + operandSpecifier.getShort());
            case SF -> {
                address = (short) (stackPointer.getShort() + operandSpecifier.getShort());
                address = mem.getShort(address);
            }
            case X -> address = (short) (operandSpecifier.getShort() + index.getShort());
            case SX -> {
                address = (short) (operandSpecifier.getShort() + index.getShort());
                address = mem.getShort(address);
            }
            case SXF -> {
                address = (short) (stackPointer.getShort() + operandSpecifier.getShort());
                address = mem.getShort(address);
                address = (short) (address + index.getShort());
            }
            case D -> {}
        }
        return address;
    }

}
