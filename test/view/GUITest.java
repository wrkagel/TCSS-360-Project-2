package view;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

/**
 * Test class for the GUI.
 * @author Group 8 Lead:Taehong Kim,
 * @version 11/10/2020
 */
class GUITest {
    /**load gui**/
    private GUI gui = new GUI();

    /**
     * Register setter test
     * Set the dummy short list and register values
     * check values are printed in each areas.
     */
    @Test
    void setRegistersText() {
        short[] test = new short[]{0x11,0x22,0x33,0x44,0x55,0x66,0x77};
        gui.setRegistersText(test);
        assertEquals("0011 17",gui.getAccumulator());
        assertEquals("0022 34",gui.getIndexRegister());
        assertEquals("0033 51",gui.getStackPointer());
        assertEquals("0044 68",gui.getProgramCounter());
        assertEquals("0055 85",gui.getInstruction());
        assertEquals("0066 102",gui.getOperandSpecifier());
        assertEquals("0077 119",gui.getOperand());
    }

    /**
     * Memory setter test
     * Set the dummy byte[] and
     * check value is printed in each areas.
     */
    @Test
    void testSetMemory() {
        byte[] test = new byte[0x00];
        gui.setMemory(test);
        assertEquals("", gui.getMemory());
        byte[] test2 = new byte[0x02];
        gui.setMemory(test2);
        assertEquals("0000: 00 00 ", gui.getMemory());

    }

    /**
     * Output setter test
     * Check the output is empty or not then
     * Set the dummy input and check value is printed in the area.
     */
    @Test
    void setoutput() {
        assertTrue(gui.getoutput().isEmpty());
        gui.setoutput("output test");
        assertFalse(gui.getoutput().isEmpty());
        assertEquals("output test", gui.getoutput());
        gui.setoutput("__++");
        assertEquals("output test__++", gui.getoutput());
    }
    /**
     * Output getter test
     * Check the output is empty or not then
     * Set the dummy input and check value is printed in the area.
     */
    @Test
    void getoutput() {
        assertTrue(gui.getoutput().isEmpty());
        gui.setoutput("test");
        assertFalse(gui.getoutput().isEmpty());
        assertNotEquals("", gui.getoutput());
        assertEquals("test", gui.getoutput());
    }
    /**
     * ObjectCode setter test
     * Make empty in the object code area
     * then put the dummy strings and check the
     * inputs were correctly set in.
     */
    @Test
    void setObjectCode() {
        assertEquals("", gui.getObjectCode());
        gui.setObjectCode("object test");
        assertEquals("object test", gui.getObjectCode());
        assertNotEquals("none", gui.getObjectCode());
    }
    /**
     * SourceCode setter test
     * Check empty in the source code area
     * then put the dummy strings then check the
     * inputs were correctly set in.
     */
    @Test
    void setSourceCode() {
        assertFalse(gui.getSourceCode().isEmpty());
        gui.setSourceCode("");
        assertEquals("", gui.getSourceCode());
        assertTrue(gui.getSourceCode().isEmpty());
        assertNotEquals("test", gui.getSourceCode());
    }
    /**
     * Nbox setter test
     * Check Nbox was set with false status
     * set the Nbox to true and check condition
     * then back to the original condition and check again
     */
    @Test
    void setNbox() {
        assertFalse(gui.getNbox());
        gui.setNbox(true);
        assertTrue(gui.getNbox());
        gui.setNbox(false);
        assertFalse(gui.getNbox());
    }
    /**
     * Zbox setter test
     * Check Zbox was set with false status
     * set the Zbox to true and check condition
     * then back to the original condition and check again
     */
    @Test
    void setZbox() {
        assertFalse(gui.getZbox());
        gui.setZbox(true);
        assertTrue(gui.getZbox());
        gui.setZbox(false);
        assertFalse(gui.getZbox());
    }
    /**
     * Vbox setter test
     * Check Vbox was set with false status
     * set the Vbox to true and check condition
     * then back to the original condition and check again
     */
    @Test
    void setVbox() {
        assertFalse(gui.getVbox());
        gui.setVbox(true);
        assertTrue(gui.getVbox());
        gui.setVbox(false);
        assertFalse(gui.getVbox());
    }
    /**
     * Cbox setter test
     * Check Cbox was set with false status
     * set the Cbox to true and check condition
     * then back to the original condition and check again
     */
    @Test
    void setCbox() {
        assertFalse(gui.getCbox());
        gui.setCbox(true);
        assertTrue(gui.getCbox());
        gui.setCbox(false);
        assertFalse(gui.getCbox());
    }
    /**
     * ObjectCode getter test
     * check objectcode area is empty
     * then put the dummy strings and check the
     * inputs were correctly printed.
     */
    @Test
    void getObjectCode() {
        assertTrue(gui.getObjectCode().isEmpty());
        gui.setObjectCode("object code");
        assertFalse(gui.getObjectCode().isEmpty());
        assertEquals("object code", gui.getObjectCode());
    }
    /**
     * AsListing setter test
     * check Aslisting area is empty
     * then put the dummy strings and check the
     * inputs were correctly set.
     */
    @Test
    void setAsListing() {
        assertTrue(gui.getAsListing().isBlank());
        gui.setAsListing("setter test");
        assertFalse(gui.getAsListing().isBlank());
        assertEquals("setter test", gui.getAsListing());
        assertNotEquals("none", gui.getAsListing());
        assertNotEquals(null, gui.getAsListing());
    }
    /**
     * sourceCode getter test
     * check sourceCode area is empty
     * then put the dummy strings and check the
     * inputs were correctly printed.
     */
    @Test
    void getSourceCode() {
        assertEquals("Please type your Souce Code here",gui.getSourceCode());
        gui.setSourceCode("");
        assertNotEquals("Please type your Souce Code here", gui.getSourceCode());
        assertEquals("",gui.getSourceCode());
    }
    /**
     * Aslisting getter test
     * check Aslisting area is empty
     * then put the dummy strings and check the
     * inputs were correctly printed.
     */
    @Test
    void getAsListing() {
        assertTrue(gui.getAsListing().isEmpty());
        gui.setAsListing("test");
        assertEquals("test", gui.getAsListing());
        assertNotEquals("", gui.getAsListing());
    }
    /**
     * batchinput setter test
     * check batchinput area is empty
     * then put the dummy strings and check the
     * inputs were correctly set.
     */
    @Test
    void setBatchInput(){
        assertTrue(gui.getBatchInput().isBlank());
        gui.setBatchInput("settertest");
        assertFalse(gui.getBatchInput().isBlank());
        assertEquals("settertest", gui.getBatchInput());
        assertNotEquals("none", gui.getBatchInput());
        assertNotEquals(null, gui.getBatchInput());
    }
    /**
     * batchinput getter test
     * check batchinput area is empty
     * then put the dummy strings and check the
     * inputs were correctly printed.
     */
    @Test
    void getBatchInput() {
        assertTrue(gui.getBatchInput().isBlank());
        gui.setBatchInput("Batchtest");
        assertFalse(gui.getBatchInput().isBlank());
        assertEquals("Batchtest", gui.getBatchInput());
        assertNotEquals("none", gui.getBatchInput());
        assertNotEquals(null, gui.getBatchInput());
    }
    /**
     * memoryarea getter test
     * put the dummy byte and check the
     * inputs were correctly printed.
     */
    @Test
    void testGetMemory() {
        byte[] test = new byte[0x02];
        gui.setMemory(test);
        assertEquals("0000: 00 00 ", gui.getMemory());
        byte[] test2 = new byte[0x00];
        gui.setMemory(test2);
        assertEquals("", gui.getMemory());
    }
    /**
     * Accumulator register area getter test
     * put the dummy list and check the
     * inputs were correctly printed.
     */
    @Test
    void testGetAccumulator(){
        short[] accumulator = new short[]{0x11,0,0,0,0,0,0,0};
        gui.setRegistersText(accumulator);
        assertEquals("0011 17",gui.getAccumulator());

        /**
         * IndexRegister register area getter test
         * put the dummy list and check the
         * inputs were correctly printed.
         */
    }
    @Test
    void testgetIndexRegister(){
        short[] IndexRegister = new short[]{0,0x11,0,0,0,0,0};
        gui.setRegistersText(IndexRegister);
        assertEquals("0011 17",gui.getIndexRegister());

    }
    /**
     * StackPointer register area getter test
     * put the dummy list and check the
     * inputs were correctly printed.
     */

    @Test
    void testgetStackPointer(){
        short[] getStackPointer = new short[]{0,0,0x11,0,0,0,0};
        gui.setRegistersText(getStackPointer);
        assertEquals("0011 17",gui.getStackPointer());

    }
    /**
     * ProgramCounter register area getter test
     * put the dummy list and check the
     * inputs were correctly printed.
     */
    @Test
    void testgetProgramCounter(){
        short[] getProgramCounter = new short[]{0,0,0,0x11,0,0,0};
        gui.setRegistersText(getProgramCounter);
        assertEquals("0011 17",gui.getProgramCounter());

    }

    /**
     * Instruction register area getter test
     * put the dummy list and check the
     * inputs were correctly printed.
     */
    @Test
    void testgetInstruction(){
        short[] getInstruction = new short[]{0,0,0,0,0x11,0,0};
        gui.setRegistersText(getInstruction);
        assertEquals("0011 17",gui.getInstruction());

    }

    /**
     * Operand Specifier register area getter test
     * put the dummy list and check the
     * inputs were correctly printed.
     */
    @Test
    void testgetOperandSpecifier(){
        short[] getOperandSpecifier = new short[]{0,0,0,0,0,0x11,0};
        gui.setRegistersText(getOperandSpecifier);
        assertEquals("0011 17",gui.getOperandSpecifier());

    }
    /**
     * Operand register area getter test
     * put the dummy list and check the
     * inputs were correctly printed.
     */
    @Test
    void testgetOperand(){
        short[] getOperand = new short[]{0,0,0,0,0,0,0x11};
        gui.setRegistersText(getOperand);
        assertEquals("0011 17",gui.getOperand());
    }
}