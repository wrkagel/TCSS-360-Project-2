package control;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

import model.Assembler;
import model.Machine;
import view.GUI;

import javax.swing.*;

/**
 * Controls all communication and updating between the model and view packages. Updates the view when a change
 * in model has occurred. Calls appropriate functions upon a user action on the view.
 *
 */
public class Controller implements ModelListener, ViewListener {

    private Machine machine;

    private Assembler assembler;

    private GUI view;

    public Controller(Machine machine, Assembler assembler, GUI view) {
        this.machine = machine;
        this.assembler = assembler;
        this.view = view;
    }

    //Placeholders for all needed listener methods.

    /**
     * Takes a request from the CPU for input and gets input from the GUI that is then returned to the CPU.
     * @return input as a String.
     */
    @Override
    public String getInput() {
        return view.getBatchInput();
    }

    /**
     * Passes the register updates from the CPU to the GUI.
     * @param values all register values in the same order as listed in the GUI.
     */
    @Override
    public void registerUpdate(short[] values) {
        view.setRegistersText(values);
    }

    /**
     * Passes the flag updates from the CPU to the GUI.
     * @param values all flag values in the order of {N, Z, V, C}.
     */
    @Override
    public void flagUpdate(boolean[] values) {
    }

    /**
     * Passes a copy of the memory from the CPU or Memory and passes it to the GUI.
     * @param values a full copy of the bytes stored in memory.
     */
    @Override
    public void memoryUpdate(byte[] values) {
        view.setMemory(values);
    }

    /**
     * Adds the outText to the output textbox of the GUI.
     * @param outText generated output
     */
    @Override
    public void output(String outText) {
        view.setoutput(outText);
    }

    /**
     * Displays the error message in a dialogue box for the user to see.
     * @param message the error message.
     */
    @Override
    public void errorMessage(String message) {
        JOptionPane.showMessageDialog(view, message);
    }

    /**
     * Takes in the name of the pushed button and then calls the appropriate methods.
     * @param name name of button
     */
    @Override
    public void buttonPushed(String name) {
        switch (name) {
            case "New" -> reset();
            case "Run Source" -> {
                if (assembler.assembleSourceCode(view.getSourceCode())) {

                } else {
                    JOptionPane.showMessageDialog(view, assembler.getErrorMessages());
                }
            }
        }
    }

    /**
     * Takes in the name of the
     * @param name name of selection
     */
    @Override
    public void fileSelection(String name) {

    }

    @Override
    public void buildSelection(String name) {
    }

    /**
     * Resets the machine with a fresh cpu and memory.
     */
    private void reset() {
        machine.reset();
    }
}
