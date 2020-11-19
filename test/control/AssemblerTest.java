package control;



import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

/**
 * Test class for the assembler.
 * @author Group 8
 * 11/13/2020
 */
class AssemblerTest {

    private Assembler assembler = new Assembler();

    //A simple test of assembling source code.
    @Test
    void testAssembleSourceCodeByte() {
        assembler.assembleSourceCode(".BYTE 0x10\n.end");
        assertEquals("10  ", assembler.getMachineCode());
    }

    @Test
    void testAssembleSourceCodeUnary() {
        assembler.assembleSourceCode(("Stop\n.end"));
        assertEquals("00  ", assembler.getMachineCode());
    }

    @Test
    void testAssembleSourceCodeNonUnary() {
        assembler.assembleSourceCode("BR 0x1000,x\n.End");
        assertEquals("05 10 00  ", assembler.getMachineCode());
    }

    @Test
    void testGetMachineCode() {
        assembler.assembleSourceCode("ADDA 15,i\nSTOP\n.END");
        assertEquals("70 00 0F 00  ", assembler.getMachineCode());
    }

    @Test
    void testGetErrorMessagesNoEnd() {
        assembler.assembleSourceCode("ADDA 15,i");
        assertEquals("Source code does not contain the .END sentinel.\n", assembler.getErrorMessages().get(0));
    }

    @Test
    void testGetErrorMessagesNoAddressingMode() {
        assembler.assembleSourceCode("ADDA 10\n.END");
        assertEquals("Instruction requires an addressing mode at line 0.\n",
                assembler.getErrorMessages().get(0));
    }

    @Test
    void testGetErrorMessagesIntegerParseError() {
        assembler.assembleSourceCode("ADDA test,i\n.end");
        assertEquals("Error when translating line 0 to machine code.\n",
                assembler.getErrorMessages().get(0));
    }

    @Test
    void testGetErrorMessagesValueTooLarge() {
        assembler.assembleSourceCode("SUBI 0xFFFFF,n\n.end");
        assertEquals("Error when translating line 0 to machine code.\n",
                assembler.getErrorMessages().get(0));
    }

    @Test
    void testGetErrorMessagesSourceLineError() {
        assembler.assembleSourceCode("ADDA 0x30,i test\n.end");
        assertEquals("Incorrect number of arguments at line 0.\n",
                assembler.getErrorMessages().get(0));
    }

    @Test
    void testGetErrorMessagesInvalidMnemonic() {
        assembler.assembleSourceCode("ADDB 0x3008,sx\n.end");
        assertEquals("No enum constant control.Mnemonic.ADDB at line 0.\n",
                assembler.getErrorMessages().get(0));
    }

    @Test
    void testSymbolTable() {
        assembler.assembleSourceCode("BR main,i\nnum: .WORD 2\nmain: ADDA num,d\nSTOP\n.END");
        assertEquals("04 00 05 00 02 71 00 03 00  ", assembler.getMachineCode());
    }

    @Test
    void testInvalidSymbolError() {
        assembler.assembleSourceCode("7Test: .BYTE 2\nstop\n.END");
        assertEquals("Invalid symbol name at line 0.\n", assembler.getErrorMessages().get(0));
    }

    @Test
    void testRepeatedSymbolError() {
        assembler.assembleSourceCode("num: .WORD 1\nnum: .WORD 2\n.END");
        assertEquals("Symbol at line 1 already exists in symbol table.\n", assembler.getErrorMessages().get(0));
    }
}