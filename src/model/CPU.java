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
 * @version 10/29/2020
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
    private boolean overflowFlag;

    /**
     * Stores the value that the most recent instruction that can set the carry flag has set it to.
     */
    private boolean carryFlag;

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
    private boolean isStep;

    private String input = "";

    /**
     * Create the cpu with its ALU, registers, and flags.
     */
    public CPU(Memory memIn){
        alu = new ALU();
        accumulator = new Register();
        index = new Register();
        stackPointer = new Register();
        programCounter = new Register();
        instructionSpecifier = new Register();
        operandSpecifier = new Register();
        operand = new Register();
        negativeFlag = false;
        zeroFlag = false;
        overflowFlag = false;
        carryFlag = false;
        listener = null;
        isStep = false;
        mem = memIn;
        //Initialize stack pointer to top of stack.
        stackPointer.setShort((short) 0xFBCF);
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
        this.isStep = isTrace;
        short instructionAddress = programCounter.getShort();
        instructionSpecifier.setByte(false, mem.getByte(instructionAddress));
        programCounter.setShort((short) (instructionAddress + 1));
        int instSpec = Short.toUnsignedInt(instructionSpecifier.getShort());
        if(instSpec == 0) {
            stopInstruction();
            return true;
        } else if (instSpec == 1){
            //returnFromTrap();
            //Traps not currently implemented
        } else if (instSpec == 2) {
            moveSPtoAcc();
        } else if (instSpec == 3) {
            moveFlagstoAcc();
        } else if (instSpec < 22) {
            branch();
        } else if (instSpec < 24) {
            callSubroutine();
        } else if (instSpec < 36) {
            unaryALUOp();
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
            binaryALUOp();
        } else if (instSpec < 224) {
            load();
        } else if (instSpec < 256) {
            store();
        } else {
            if (listener != null) {
                throw new IllegalStateException("No valid opCode found. Error in code.");
            }
        }
        return false;
    }

    /**
     * Informs the listener of the register updates of a stop instruction and then returns true.
     */
    private void stopInstruction() {
        if (listener == null) updateListener();
    }

    /**
     * Moves the stack pointer into the accumulator.
     */
    private void moveSPtoAcc() {
        short value = stackPointer.getShort();
        accumulator.setShort(value);
        if(listener != null && isStep) updateListener();
    }

    /**
     * Moves the nzvc flags into the accumulator with bits [0:11] being filled with zeroes.
     */
    private void moveFlagstoAcc() {
        short value = 0;
        if (negativeFlag) value += 8;
        if (zeroFlag) value += 4;
        if (overflowFlag) value += 2;
        if (carryFlag) value += 1;
        accumulator.setShort(value);
        if (listener != null && isStep) updateListener();
    }

    /**
     * Checks if branch condition specified by the instruction is met and then branches if it is.
     */
    private void branch() {
        boolean shouldBranch;
        int instSpec = Short.toUnsignedInt(instructionSpecifier.getShort());
        operandSpecifier.setShort(mem.getShort(programCounter.getShort()));
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
            shouldBranch = !(negativeFlag || zeroFlag);
        } else if (instSpec < 20) {
            shouldBranch = overflowFlag;
        } else if (instSpec < 22) {
            shouldBranch = carryFlag;
        } else {
            throw new IllegalStateException("Incorrectly went into branch function. Error in code.");
        }
        if ((instSpec & 0x1) == 1) {
            operand.setShort(mem.getShort(getOperandAddress(AddressingMode.X)));
        } else {
            operand.setShort(operandSpecifier.getShort());
        }
        if (shouldBranch) {
            programCounter.setShort(operand.getShort());
        }
        if (listener != null && isStep) updateListener();
    }

    /**
     * Moves the stack pointer down 2, stores the program counter at mem[SP], then puts the operand
     * into the PC.
     */
    private void callSubroutine() {
        operandSpecifier.setShort(mem.getShort(programCounter.getShort()));
        programCounter.setShort((short) (programCounter.getShort() + 2));
        stackPointer.setShort((short) (stackPointer.getShort() - 2));
        mem.setShort(stackPointer.getShort(), programCounter.getShort());
        if ((instructionSpecifier.getShort() & 0x1) == 1) {
            operand.setShort(mem.getShort(getOperandAddress(AddressingMode.X)));
        } else {
            operand.setShort(operandSpecifier.getShort());
        }
        programCounter.setShort(operand.getShort());
        if (listener != null && isStep) updateListener();
    }

    /**
     * Performs an ALU operation that only has one input.
     */
    private void unaryALUOp() {
        int opCode = Short.toUnsignedInt(instructionSpecifier.getShort());
        short value;
        boolean[] checkFlags;
        if ((instructionSpecifier.getShort() & 0x1) == 1) {
            value = index.getShort();
        } else {
            value = accumulator.getShort();
        }
        if (opCode < 26) {
            value = alu.not(value);
            //set NZ
            checkFlags = new boolean[]{true, true, false, false};
        } else if (opCode < 28) {
            value = alu.negation(value);
            //set NZV
            checkFlags = new boolean[]{true, true, true, false};
        } else if (opCode < 30) {
            value = alu.arithShiftLeft(value);
            //set NZVC
            checkFlags = new boolean[]{true, true, true, true};
        } else if (opCode < 32) {
            value = alu.arithShiftRight(value);
            //set NZC
            checkFlags = new boolean[]{true, true, false, true};
        } else if (opCode < 34) {
            value = alu.rotateLeft(value);
            //set C
            checkFlags = new boolean[]{false, false, false, true};
        } else if (opCode < 36) {
            value = alu.rotateRight(value);
            //set C
            checkFlags = new boolean[]{false, false, false, true};
        } else {
            throw new IllegalStateException("Incorrectly went into unaryALUOp. Error in code.");
        }
        if ((instructionSpecifier.getShort() & 0x1) == 1) {
            index.setShort(value);
        } else {
            accumulator.setShort(value);
        }
        if (checkFlags[0]) negativeFlag = alu.getNegativeFlag();
        if (checkFlags[1]) zeroFlag = alu.getZeroFlag();
        if (checkFlags[2]) overflowFlag = alu.getOverflowFlag();
        if (checkFlags[3]) carryFlag = alu.getCarryFlag();
        if (listener != null && isStep) updateListener();
    }

    /**
     * Performs an input or output operation on a decimal value.
     */
    private void decimalIO() {
        if (listener == null) throw new IllegalStateException("Cannot do input or output without a listener");
        int instrSpec = Short.toUnsignedInt(instructionSpecifier.getShort());
        operandSpecifier.setShort(mem.getShort(programCounter.getShort()));
        programCounter.setShort((short) (programCounter.getShort() + 2));
        int mode = instrSpec & 0x7;
        boolean isInput = (instrSpec & 0x8) == 0;
        if (isInput) {
            if (mode == 0) throw new IllegalArgumentException("Illegal addressing mode for decimal input.");
            if (input.length() == 0) input = listener.getInput();
            short oper;
            try {
                oper = Short.parseShort(input);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Input cannot be read as a decimal word.");
            }
            operand.setShort(oper);
            mem.setShort(getOperandAddress(AddressingMode.values()[mode]), oper);
        } else {
            short oper;
            if (mode == 0) {
                oper = operandSpecifier.getShort();
            } else {
                oper = mem.getShort(getOperandAddress(AddressingMode.values()[mode]));
            }
            operand.setShort(oper);
            listener.output("" + oper);
        }
        if (isStep) updateListener();
    }

    /**
     * Outputs a null terminated string with the first character at the starting address
     * indicated by the operand.
     */
    private void stringOut() {
        if (listener == null) throw new IllegalStateException("Cannot perform string out without a listener");
        int instSpec = Short.toUnsignedInt(instructionSpecifier.getShort());
        int mode = instSpec & 0x7;
        operandSpecifier.setShort(mem.getShort(programCounter.getShort()));
        programCounter.setShort((short) (programCounter.getShort() + 2));
        if (mode != 1 && mode != 2 && mode != 4) throw new IllegalArgumentException("Illegal addressing mode" +
                " for string out.");
        short oper = getOperandAddress(AddressingMode.values()[mode]);
        operand.setShort(oper);
        StringBuilder sb = new StringBuilder();
        byte out = mem.getByte(oper);
        while (out != 0) {
            sb.append((char) out);
            oper++;
            out = mem.getByte(oper);
        }
        listener.output(sb.toString());
        if (isStep) updateListener();
    }

    /**
     * Performs input or output of a single character.
     */
    private void charIO() {
        if (listener == null) throw new IllegalStateException("Cannot do input or output without a listener");
        int instrSpec = Short.toUnsignedInt(instructionSpecifier.getShort());
        operandSpecifier.setShort(mem.getShort(programCounter.getShort()));
        programCounter.setShort((short) (programCounter.getShort() + 2));
        int mode = instrSpec & 0x7;
        boolean isInput = (instrSpec & 0x8) == 0x8;
        if (isInput) {
            if (mode == 0) throw new IllegalArgumentException("Illegal addressing mode for character input.");
            if (input.length() == 0) input = listener.getInput();
            char oper = input.charAt(0);
            input = input.substring(1);
            operand.setByte(true, (byte) 0);
            operand.setByte(false, (byte) oper);
            mem.setByte(getOperandAddress(AddressingMode.values()[mode]), (byte) oper);
        } else {
            char oper;
            if (mode == 0) {
                oper = (char) operandSpecifier.getByte(false);
            } else {
                oper = (char) mem.getByte(getOperandAddress(AddressingMode.values()[mode]));
            }
            operand.setByte(true, (byte) 0);
            operand.setByte(false, (byte) oper);
            listener.output("" + oper);
        }
        if (isStep) updateListener();
    }

    /**
     * Returns from a call.
     */
    private void returnFromCall() {
        int n = instructionSpecifier.getShort() & 0x7;
        stackPointer.setShort((short) (stackPointer.getShort() + n));
        programCounter.setShort(mem.getShort(stackPointer.getShort()));
        stackPointer.setShort((short) (stackPointer.getShort() + 2));
        if (listener != null && isStep) updateListener();
    }

    /**
     * Performs an ALU operation with two inputs.
     */
    private void binaryALUOp() {
        operandSpecifier.setShort(mem.getShort(programCounter.getShort()));
        programCounter.setShort((short) (programCounter.getShort() + 2));
        int opCode = instructionSpecifier.getShort();
        int mode = (opCode & 0x7);
        int reg = (opCode & 0xF) >>> 3;
        //Check {negative, zero, overflow, carry}
        boolean[] checkFlags;
        if (mode == 0) {
            operand.setShort(operandSpecifier.getShort());
        } else {
            operand.setShort(getOperandAddress(AddressingMode.values()[mode]));
        }
        short value1;
        if (opCode < 112) {
            value1 = stackPointer.getShort();
            checkFlags = new boolean[]{false, false, false, false};
        } else if (reg == 1) {
            value1 = index.getShort();
        } else {
            value1 = accumulator.getShort();
        }
        if (opCode < 104 ) {
            value1 = alu.add(value1, operand.getShort());
            //set NZVC
            checkFlags = new boolean[]{true, true, true, true};
        } else if (opCode < 112) {
            value1 = alu.sub(value1, operand.getShort());
            //set NZVC
            checkFlags = new boolean[]{true, true, true, true};
        } else if (opCode < 128) {
            value1 = alu.add(value1, operand.getShort());
            //set NZVC
            checkFlags = new boolean[]{true, true, true, true};
        } else if (opCode < 144) {
            value1 = alu.sub(value1, operand.getShort());
            //set NZVC
            checkFlags = new boolean[]{true, true, true, true};
        } else if (opCode < 160) {
            value1 = alu.and(value1, operand.getShort());
            //set NZ
            checkFlags = new boolean[]{true, true, false, false};
        } else if (opCode < 176) {
            value1 = alu.or(value1, operand.getShort());
            //set NZ
            checkFlags = new boolean[]{true, true, false, false};
        } else if (opCode < 192) {
            value1 = alu.sub(value1, operand.getShort());
            //set NZVC
            checkFlags = new boolean[]{true, true, true, true};
        } else {
            throw new IllegalStateException("Incorrectly went into binaryALUOp. Error in code.");
        }
        if (opCode < 176) {
            if (opCode < 112) {
                stackPointer.setShort(value1);
            } else if (reg == 1) {
                index.setShort(value1);
            } else {
                accumulator.setShort(value1);
            }
        }
        if (checkFlags[0]) negativeFlag = alu.getNegativeFlag();
        if (checkFlags[1]) zeroFlag = alu.getZeroFlag();
        if (checkFlags[2]) overflowFlag = alu.getOverflowFlag();
        if (checkFlags[3]) carryFlag = alu.getCarryFlag();
        if(listener != null && isStep) updateListener();
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
        negativeFlag = operand.getShort() < 0;
        zeroFlag = operand.getShort() == 0;
        if (listener != null && isStep) updateListener();
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
        if (listener != null && isStep) updateListener();
    }

    private short getOperandAddress(AddressingMode mode) {
        short address = operandSpecifier.getShort();
        switch(mode) {
            case I -> throw new IllegalStateException("Immediate mode does not require addressing. " +
                    "Error in code.");
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

    private void updateListener() {
        short[] registerValues = new short[] {accumulator.getShort(), index.getShort(), stackPointer.getShort(),
                programCounter.getShort(), instructionSpecifier.getShort(), operandSpecifier.getShort(),
                operand.getShort()};
        boolean[] flags = new boolean[] {negativeFlag, zeroFlag, overflowFlag, carryFlag};
        listener.registerUpdate(registerValues);
        listener.flagUpdate(flags);
        listener.memoryUpdate(mem.getMemCopy());
    }

}
