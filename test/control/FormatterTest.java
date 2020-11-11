package control;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FormatterTest {

    private ArrayList<SourceLine> testLines;

    private ArrayList<String> errorMessages;

    @BeforeEach
    void initialize() {
        testLines = new ArrayList<>();
        errorMessages = new ArrayList<>();
    }

    @Test
    void parsePseudoInstructionsASCII() {
        testLines.add(new SourceLine(".ASCII \"\\\"Testing\\\"\"", 0));
        Formatter.parsePseudoInstructions(testLines, errorMessages);
        char[] test = "\"Testing\"".toCharArray();
        for (int i = 0; i < test.length; i++) {
            assertEquals(".BYTE " + (byte) test[i], testLines.get(i).toString());
        }
    }

    @Test
    void parsePseudoInstructionsASCIIError() {
        testLines.add(new SourceLine(".ASCII", 1));
        Formatter.parsePseudoInstructions(testLines, errorMessages);
        assertEquals("Incorrect number of arguments for .ASCII pseudo-op at line 1.\n", errorMessages.get(0));
    }

    @Test
    void parsePseudoInstructionsBlock() {
        testLines.add(new SourceLine(".BLOCK 5", 0));
        Formatter.parsePseudoInstructions(testLines, errorMessages);
        for (int i = 0; i < 5; i++) {
            assertEquals(".BYTE 00", testLines.get(i).toString());
        }
    }

    @Test
    void parsePseudoInstructionsBlockError() {
        testLines.add(new SourceLine(".Block 22.", 1));
        testLines.add(new SourceLine(".Block -22", 2));
        testLines.add(new SourceLine(".BLOCK", 3));
        Formatter.parsePseudoInstructions(testLines, errorMessages);
        assertEquals("Error parsing number for .BLOCK at line 1.\n", errorMessages.get(0));
        assertEquals("Argument for .BLOCK cannot be less than one. Error at line 2.\n", errorMessages.get(1));
        assertEquals("Incorrect number of arguments for .BLOCK at line 3.\n", errorMessages.get(2));
    }

    @Test
    void parsePseudoInstructionsWord() {
        testLines.add(new SourceLine(".WORD 200", 0));
        testLines.add(new SourceLine(".WORD -4112", 1));
        testLines.add(new SourceLine(".WORD 0x0808", 2));
        Formatter.parsePseudoInstructions(testLines, errorMessages);
        assertEquals(".BYTE 0", testLines.get(0).toString());
        assertEquals(".BYTE -56", testLines.get(1).toString());
        assertEquals(".BYTE -17", testLines.get(2).toString());
        assertEquals(".BYTE -16", testLines.get(3).toString());
        assertEquals(".BYTE 8", testLines.get(4).toString());
        assertEquals(".BYTE 8", testLines.get(5).toString());
    }

    @Test
    void parsePseudoInstructionsWordError() {
        testLines.add(new SourceLine(".Word potato", 0));
        testLines.add(new SourceLine(".WORD", 1));
        testLines.add(new SourceLine(".WORD 10000000000", 2));
        testLines.add(new SourceLine(".WORD -10000000000", 3));
    }
}