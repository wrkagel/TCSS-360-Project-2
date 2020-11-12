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
 * @version 11/07/2020
 */
class CPU {

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
    private final PCRegister programCounter;

    /**
     * Used to store the instruction specifier for an instruction.
     * Uses only the least significant byte of the register as the instruction specifier is
     * only 8 bits.
     */
    private final InstructionRegister instructionSpecifier;

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
    private final Memory mem;

    /**
     * Sets if the CPU updates the listener after every instruction or only after a stop instruction.
     * Added to increase efficiency.
     */
    private boolean isStep;

    /**
     * Stores the input buffer.
     */
    private String input = "";

    /**
     * Create the cpu with its ALU, registers, and flags.
     */
    CPU(Memory memIn){
        alu = new ALU();
        accumulator = new Register();
        index = new Register();
        stackPointer = new Register();
        programCounter = new PCRegister();
        instructionSpecifier = new InstructionRegister();
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
    boolean fetchExecute(boolean isTrace) {
        this.isStep = isTrace;
        instructionSpecifier.setInstruction(mem.getByte(programCounter.getShort()));
        programCounter.advance(1);
        int opCode = instructionSpecifier.getUnsignedValue();
        if(opCode == 0) {
            stopInstruction();
            return true;
        } else if (opCode == 1){
            //returnFromTrap();
            //Traps not currently implemented
        } else if (opCode == 2) {
            moveSPtoAcc();
        } else if (opCode == 3) {
            moveFlagstoAcc();
        } else if (opCode < 22) {
            branch();
        } else if (opCode < 24) {
            callSubroutine();
        } else if (opCode < 36) {
            unaryALUOp();
        } else if (opCode < 48) {
            //no operation trap calls
            //not currently implemented
        } else if (opCode < 64) {
            decimalIO();
        } else if (opCode < 72) {
            stringOut();
        } else if (opCode < 88) {
            charIO();
        } else if (opCode < 96) {
            returnFromCall();
        } else if (opCode < 192) {
            binaryALUOp();
        } else if (opCode < 224) {
            load();
        } else if (opCode < 256) {
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
        if (listener != null) updateListener();
        programCounter.setShort((short) 0);
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
        int opCode = instructionSpecifier.getUnsignedValue();
        operandSpecifier.setShort(mem.getShort(programCounter.getShort()));
        programCounter.advance(2);
        if (opCode < 6) {
            shouldBranch = true;
        } else if (opCode < 8) {
            //Less than or equal
            shouldBranch = negativeFlag || zeroFlag;
        } else if (opCode < 10) {
            //Less than
            shouldBranch = negativeFlag;
        } else if (opCode < 12) {
            //Equal
            shouldBranch = zeroFlag;
        } else if (opCode < 14) {
            //Not Equal
            shouldBranch = !zeroFlag;
        } else if (opCode < 16) {
            //Greater Than or Equal
            shouldBranch = !negativeFlag;
        } else if (opCode < 18) {
            //Greater than
            shouldBranch = !(negativeFlag || zeroFlag);
        } else if (opCode < 20) {
            //Branch if overflow
            shouldBranch = overflowFlag;
        } else if (opCode < 22) {
            //Branch if carry
            shouldBranch = carryFlag;
        } else {
            throw new IllegalStateException("Incorrectly went into branch function. Error in code.");
        }
        if (instructionSpecifier.getLSBit() == 1) {
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
        programCounter.advance(2);
        stackPointer.setShort((short) (stackPointer.getShort() - 2));
        mem.setShort(stackPointer.getShort(), programCounter.getShort());
        if (instructionSpecifier.getLSBit() == 1) {
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
        int opCode = instructionSpecifier.getUnsignedValue();
        short value;
        boolean[] checkFlags;
        if (instructionSpecifier.getLSBit() == 1) {
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
        if (instructionSpecifier.getLSBit() == 1) {
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
     * Performs an input or output operation on a decimal value. This can currently read in multi character decimal
     * numbers and this may be incorrect compared to how the original pep/8 simulator operates.
     */
    private void decimalIO() {
        if (listener == null) throw new IllegalStateException("Cannot do input or output without a listener");
        int opCode = instructionSpecifier.getUnsignedValue();
        operandSpecifier.setShort(mem.getShort(programCounter.getShort()));
        programCounter.advance(2);
        AddressingMode mode = instructionSpecifier.getAddresssingMode();
        boolean isInput = (opCode & 0x8) == 0;
        if (isInput) {
            if (mode == AddressingMode.I)
                throw new IllegalArgumentException("Illegal addressing mode for decimal input.");
            if (input.length() == 0) input = listener.getInput();
            short oper;
            try {
                oper = Short.parseShort(input);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Input cannot be read as a decimal word.");
            }
            operand.setShort(oper);
            mem.setShort(getOperandAddress(mode), oper);
        } else {
            short oper;
            if (mode == AddressingMode.I) {
                oper = operandSpecifier.getShort();
            } else {
                oper = mem.getShort(getOperandAddress(mode));
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
        AddressingMode mode = instructionSpecifier.getAddresssingMode();
        operandSpecifier.setShort(mem.getShort(programCounter.getShort()));
        programCounter.advance(2);
        if (mode != AddressingMode.D && mode != AddressingMode.N && mode != AddressingMode.SF)
            throw new IllegalArgumentException("Illegal addressing mode for string out.");
        short oper = getOperandAddress(mode);
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
        int instrSpec = instructionSpecifier.getUnsignedValue();
        operandSpecifier.setShort(mem.getShort(programCounter.getShort()));
        programCounter.advance(2);
        AddressingMode mode = instructionSpecifier.getAddresssingMode();
        boolean isInput = (instrSpec & 0x8) == 0x8;
        if (isInput) {
            if (mode == AddressingMode.I)
                throw new IllegalArgumentException("Illegal addressing mode for character input.");
            if (input.length() == 0) input = listener.getInput();
            char oper = input.charAt(0);
            input = input.substring(1);
            operand.setByte(true, (byte) 0);
            operand.setByte(false, (byte) oper);
            mem.setByte(getOperandAddress(mode), (byte) oper);
        } else {
            char oper;
            if (mode == AddressingMode.I) {
                oper = (char) operandSpecifier.getByte(false);
            } else {
                oper = (char) mem.getByte(getOperandAddress(mode));
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
        int n = (instructionSpecifier.getUnsignedValue() & 0x7);
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
        programCounter.advance(2);
        int opCode = instructionSpecifier.getUnsignedValue();
        AddressingMode mode = instructionSpecifier.getAddresssingMode();
        int reg = (opCode & 0xF) >>> 3;
        //Check {negative, zero, overflow, carry}
        boolean[] checkFlags;
        if (mode == AddressingMode.I) {
            operand.setShort(operandSpecifier.getShort());
        } else {
            operand.setShort(mem.getShort(getOperandAddress(mode)));
        }
        short value1;
        if (opCode < 112) {
            value1 = stackPointer.getShort();
        } else if (reg == 1) {
            value1 = index.getShort();
        } else {
            value1 = accumulator.getShort();
        }
        if (opCode < 104 ) {
            //ADD stack pointer
            value1 = alu.add(value1, operand.getShort());
            //set NZVC
            checkFlags = new boolean[]{true, true, true, true};
        } else if (opCode < 112) {
            //SUB stack pointer
            value1 = alu.sub(value1, operand.getShort());
            //set NZVC
            checkFlags = new boolean[]{true, true, true, true};
        } else if (opCode < 128) {
            //ADD
            value1 = alu.add(value1, operand.getShort());
            //set NZVC
            checkFlags = new boolean[]{true, true, true, true};
        } else if (opCode < 144) {
            //SUB
            value1 = alu.sub(value1, operand.getShort());
            //set NZVC
            checkFlags = new boolean[]{true, true, true, true};
        } else if (opCode < 160) {
            //AND
            value1 = alu.and(value1, operand.getShort());
            //set NZ
            checkFlags = new boolean[]{true, true, false, false};
        } else if (opCode < 176) {
            //OR
            value1 = alu.or(value1, operand.getShort());
            //set NZ
            checkFlags = new boolean[]{true, true, false, false};
        } else if (opCode < 192) {
            //CMP: Use subtract for compare.
            value1 = alu.sub(value1, operand.getShort());
            //set NZVC
            checkFlags = new boolean[]{true, true, true, true};
            //Invert negative flag if overflow flag is true.
            if (alu.getOverflowFlag()) {
                checkFlags[0] = false;
                if (negativeFlag == alu.getNegativeFlag()) negativeFlag = !negativeFlag;
            }
        } else {
            throw new IllegalStateException("Incorrectly went into binaryALUOp. Error in code.");
        }
        //Don't store the result if compare.
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
        programCounter.advance(2);
        int opCode = instructionSpecifier.getUnsignedValue();
        int reg = (opCode & 0xF) >>> 3;
        int isByte = (opCode & 0x10) >>> 4;
        AddressingMode mode = instructionSpecifier.getAddresssingMode();
        if (isByte == 0) {
            if (mode == AddressingMode.I) {
                operand.setShort(operandSpecifier.getShort());
            } else {
                operand.setShort(mem.getShort(getOperandAddress(mode)));
            }
            if (reg == 0) {
                accumulator.setShort(operand.getShort());
            } else {
                index.setShort(operand.getShort());
            }
        } else {
            operand.setByte(true, (byte) 0);
            if (mode == AddressingMode.I) {
                operand.setByte(false, operandSpecifier.getByte(false));
            } else {
                operand.setByte(false, mem.getByte(getOperandAddress(mode)));
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
        programCounter.advance(2);
        int opCode = instructionSpecifier.getUnsignedValue();
        int reg = (opCode & 0xF) >>> 3;
        int isByte = (opCode & 0x10) >>> 4;
        AddressingMode mode = instructionSpecifier.getAddresssingMode();
        short address;
        if (mode == AddressingMode.I)
            throw new IllegalArgumentException("Illegal addressing mode for store instruction.");
        address = getOperandAddress(mode);
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

    /**
     * Update listener method that updates listener with all current register, flag, and memory values.
     */
    private void updateListener() {
        short[] registerValues = new short[] {accumulator.getShort(), index.getShort(), stackPointer.getShort(),
                programCounter.getShort(), (short) instructionSpecifier.getUnsignedValue(), operandSpecifier.getShort(),
                operand.getShort()};
        boolean[] flags = new boolean[] {negativeFlag, zeroFlag, overflowFlag, carryFlag};
        listener.registerUpdate(registerValues);
        listener.flagUpdate(flags);
        listener.memoryUpdate(mem.getMemCopy());
    }

}
