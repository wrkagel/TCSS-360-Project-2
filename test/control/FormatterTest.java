package control;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FormatterTest {

    private ArrayList<String> testLines;

    private ArrayList<String> errorMessages;

    @BeforeEach
    void initialize() {
        testLines = new ArrayList<>();
        errorMessages = new ArrayList<>();
    }

    @Test
    void testRemoveCommentLines() {
        testLines.add(";");
        testLines.add("; This is a comment line");
        Formatter.removeCommentsAndEnd(testLines, errorMessages);
        assertEquals("", testLines.get(0));
        assertEquals("", testLines.get(1));
    }

    @Test
    void testRemoveEndAndBeyond() {
        testLines.add(".END");
        testLines.add("Beyond");
        testLines.add("Further Beyond");
        Formatter.removeCommentsAndEnd(testLines, errorMessages);
        assertTrue(testLines.isEmpty());
    }

    @Test
    void testNoEndError() {
        testLines.add(";Comment");
        testLines.add("Not a comment");
        Formatter.removeCommentsAndEnd(testLines, errorMessages);
        assertEquals("No .END sentinel found within the source code.\n", errorMessages.get(0));
    }

    @Test
    void parsePseudoInstructionsASCII() {
        testLines.add(".ASCII \"\\\"Testing\\\"\"");
        Formatter.parsePseudoInstructions(testLines, errorMessages);
        char[] test = "\"Testing\"".toCharArray();
        for (int i = 0; i < test.length; i++) {
            assertEquals(".BYTE " + (byte) test[i], testLines.get(i));
        }
    }

    @Test
    void parsePseudoInstructionsASCIIError() {
        testLines.add(".ASCII Testing error");
        Formatter.parsePseudoInstructions(testLines, errorMessages);
        assertEquals("Incorrect number of arguments for .ASCII pseudo-op on line 0.\n", errorMessages.get(0));
        testLines.remove(0);
        testLines.add(".ASCII");
        assertEquals("Incorrect number of arguments for .ASCII pseudo-op on line 0.\n", errorMessages.get(0));
    }

    @Test
    void parsePseudoInstructionsBlock() {
        testLines.add(".BLOCK 5");
        Formatter.parsePseudoInstructions(testLines, errorMessages);
        for (int i = 0; i < 5; i++) {
            assertEquals(".BYTE 00", testLines.get(i));
        }
    }

    @Test
    void parsePseudoInstructionsBlockError() {
        testLines.add(".BLOCK 22 Cheese.");
        testLines.add(".Block 22.");
        testLines.add(".Block -22");
        testLines.add(".BLOCK");
        Formatter.parsePseudoInstructions(testLines, errorMessages);
        assertEquals("Incorrect number of arguments for .BLOCK at line 0.\n", errorMessages.get(0));
        assertEquals("Error parsing number for .BLOCK at line 1.\n", errorMessages.get(1));
        assertEquals("Argument for .BLOCK cannot be less than one. Error at line 2.\n", errorMessages.get(2));
        assertEquals("Incorrect number of arguments for .BLOCK at line 3.\n", errorMessages.get(3));
    }

    @Test
    void parsePseudoInstructionsWord() {
        testLines.add(".WORD 200");
        testLines.add(".WORD -4112");
        testLines.add(".WORD 0x0808");
        Formatter.parsePseudoInstructions(testLines, errorMessages);
        assertEquals(".BYTE 0", testLines.get(0));
        assertEquals(".BYTE -56", testLines.get(1));
        assertEquals(".BYTE -17", testLines.get(2));
        assertEquals(".BYTE -16", testLines.get(3));
        assertEquals(".BYTE 8", testLines.get(4));
        assertEquals(".BYTE 8", testLines.get(5));
    }

    @Test
    void parsePseudoInstructionsWordError() {
        testLines.add(".Word potato");
        testLines.add(".WORD");
        testLines.add(".WORD 22 world");
        testLines.add(".WORD 10000000000");
        testLines.add(".WORD 10000000000");
    }
}