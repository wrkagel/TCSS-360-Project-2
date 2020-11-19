package control;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

/**
 * Simple test class for the SourceLine class.
 * @author Group 8
 * @version 11/19/2020
 */
class SourceLineTest {

    private SourceLine sourceLine;

    @Test
    void getLineNumber() {
        sourceLine = new SourceLine(".ASCII 27", 14);
        assertEquals(14, sourceLine.getLineNumber());
    }

    @Test
    void getMnemonic() {
        sourceLine = new SourceLine("ADDA 0x3555,d", 0);
        assertEquals(Mnemonic.ADDA, sourceLine.getMnemonic());
    }

    @Test
    void invalidMnemonic() {
        assertThrows(IllegalArgumentException.class, () -> new SourceLine("ADDB 0x3555,d", 0));
    }

    @Test
    void getValue() {
        sourceLine = new SourceLine("num: ADDA 0x3555,i", 0);
        assertEquals("0x3555,i", sourceLine.getValue());
    }

    @Test
    void getSymbol() {
        sourceLine = new SourceLine("num: ADDA 0x3555,i", 10);
        assertEquals("num", sourceLine.getSymbol());
    }

    @Test
    void testToString() {
        sourceLine = new SourceLine("num: ADDA 0x3555,i", 10);
        assertEquals("num: ADDA 0x3555,i", sourceLine.toString());
    }

    @Test
    void testRemoveComments() {
        sourceLine = new SourceLine("ADDA 0x2222; This is a comment.", 10);
        assertEquals("ADDA 0x2222", sourceLine.toString());
    }

    @Test
    void testOnlyComment() {
        sourceLine = new SourceLine(";This is a line that contains only a comment.", 10);
        assertNull(sourceLine.getMnemonic());
        assertEquals("", sourceLine.getSymbol());
        assertEquals("", sourceLine.getValue());
    }

    @Test
    void testUnaryInstruction() {
        sourceLine = new SourceLine("   NOTA", 10);
        assertEquals(Mnemonic.NOTA, sourceLine.getMnemonic());
        assertEquals("", sourceLine.getValue());
    }

    @Test
    void testSymbolInAddress() {
        sourceLine = new SourceLine("ADDA num,d", 10);
        assertEquals("num,d", sourceLine.getValue());
    }


}