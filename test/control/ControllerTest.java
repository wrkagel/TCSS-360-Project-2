package control;

import model.Machine;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.GUI;

import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.*;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

/**
 * Simple test class for the controller. Some pure GUI interactions are not being tested.
 * @author Group 8, Lead: Walter Kagel
 * @version 11/13/2020
 */
class ControllerTest {

    private static final Assembler assembler = new Assembler();

    private static final Machine machine = new Machine();

    private static final GUI view = new GUI();

    private static final Controller controller = new Controller(machine, assembler, view);

    @BeforeAll
    static void initialize() {
        machine.addListener(controller);
        view.addViewListener(controller);
    }

    @BeforeEach
    void reInitialize() {
        view.actionPerformed(new ActionEvent(view, 0, "New"));
    }

    @Test
    void registerUpdate() {
        controller.registerUpdate(new short[] {20, 50, 0, 0, 0, 0, 0});
        assertEquals("0032 50", view.getIndexRegister());
    }

    @Test
    void flagUpdate() {
        controller.flagUpdate(new boolean[] {true, false, true, false});
        assertTrue(view.getNbox());
        assertFalse(view.getZbox());
        assertTrue(view.getVbox());
        assertFalse(view.getCbox());
    }

    @Test
    void memoryUpdate() {
        controller.memoryUpdate(new byte[] {0x50, (byte) 0xFF});
        assertEquals("0000: 50 FF ", view.getMemory());
    }

    @Test
    void output() {
        controller.output("Test");
        assertEquals("Test", view.getoutput());
    }

    @Test
    void buttonPushedNew() {
        machine.setMemory((short)0, new byte[] {10, 10});
        assertEquals("0000: 0A 0A", view.getMemory().substring(0, 11));
        controller.buttonPushed("New");
        assertEquals("0000: 00 00", view.getMemory().substring(0, 11));
    }

    @Test
    void buttonPushedRunSource() {
        view.setSourceCode("ADDI 0x15,i\n.end");
        controller.buttonPushed("Run Source");
        assertEquals("0015 21", view.getIndexRegister());
    }

    @Test
    void buttonPushedRunObject() {
        view.setObjectCode("78 00 17 00");
        controller.buttonPushed("Run Object");
        assertEquals("0017 23", view.getIndexRegister());
    }

    @Test
    void buttonPushedDebugSource() {
        view.setSourceCode("SUBI 0x20,i\n.end");
        controller.buttonPushed("Debug Source");
        controller.buttonPushed("Single Step");
        assertEquals("FFE0 -32", view.getIndexRegister());
    }

    @Test
    void buttonPushedDebugObject() {
        view.setObjectCode("88 FF FF 00");
        controller.buttonPushed("Debug Object");
        controller.buttonPushed("Single Step");
        assertEquals("0001 1", view.getIndexRegister());
    }

    @Test
    void buttonPushedResume() {
        view.setSourceCode("SUBI -1,i\nSUBI -2,i\nSTOP\n.end");
        controller.buttonPushed("Debug Source");
        controller.buttonPushed("Resume");
        assertEquals("0003 3", view.getIndexRegister());
    }
}