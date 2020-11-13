package control;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

import model.Machine;
import view.GUI;

import javax.swing.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controls all communication and updating between the model and view packages. Updates the view when a change
 * in model has occurred. Calls appropriate functions upon a user action on the view.
 * @author Group 8, Walter Kagel
 * @version 11/13/2020
 */
public class Controller implements ModelListener, ViewListener {

    private final Machine machine;

    private final Assembler assembler;

    private final GUI view;

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
        if (values[4] == 0) view.disableDebug();
        view.setRegistersText(values);
    }

    /**
     * Passes the flag updates from the CPU to the GUI.
     * @param values all flag values in the order of {N, Z, V, C}.
     */
    @Override
    public void flagUpdate(boolean[] values) {
        view.setNbox(values[0]);
        view.setZbox(values[1]);
        view.setVbox(values[2]);
        view.setCbox(values[3]);
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
            case "Run Object" -> runObject();
            case "Debug Object" -> debugObject();
            case "Save" -> saveSource();
            case "Run Source" -> runSource();
            case "Debug Source" -> debugSource();
            case "Assemble" -> assemble();
            case "Single Step" -> singleStep();
            case "Resume" -> resume();
        }
    }

    /**
     * Takes in the name of the user selection from the file menu and calls the appropriate methods.
     * @param name name of selection
     */
    @Override
    public void fileSelection(String name) {
        switch(name) {
            case "Open" -> openSource();
            case "New" -> reset();
        }
    }

    /**
     * Takes in the name of the user selction from the build menu and calls the appropriate methods.
     * @param name name of selection
     */
    @Override
    public void buildSelection(String name) {
        switch(name){
            case "Run Object" -> runObject();
            case "Debug Object" -> debugObject();
            case "Run Source" -> runSource();
            case "Start Debugging" -> debugSource();
        }
    }

    /**
     * Resets the machine with a fresh cpu and memory. Resets
     */
    private void reset() {
        machine.reset();
        view.setoutput("");
        view.setCbox(false);
        view.setVbox(false);
        view.setZbox(false);
        view.setNbox(false);
        view.setRegistersText(new short[8]);
        view.setMemory(new byte[65536]);
    }

    private byte[] parseObjectCode(String objectCode) {
        objectCode = objectCode.replaceAll("\\s", "");
        objectCode = objectCode.toUpperCase();
        Pattern p = Pattern.compile("[^0-9ABCDEF]");
        Matcher m = p.matcher(objectCode);
        if (m.find()) {
            throw new IllegalArgumentException("Error in object code.");
        } else {
            if (objectCode.length() % 2 != 0) {
                throw new IllegalArgumentException("Object code contains at least one incomplete byte.");
            }
            int arrLength = objectCode.length() / 2;
            byte[] bytes = new byte[arrLength];
            for (int i = 0; i < arrLength; i++) {
                bytes[i] = (byte) Integer.parseUnsignedInt(objectCode.substring(2 * i, 2 * (i + 1)), 16);
            }
            return bytes;
        }
    }

    private void runObject() {
        try {
            machine.setMemory((short) 0, parseObjectCode(view.getObjectCode()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, e.getMessage());
        }
        machine.run(false);
    }

    private void debugObject() {
        try {
            machine.setMemory((short) 0, parseObjectCode(view.getObjectCode()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, e.getMessage());
        }
    }

    private void runSource() {
        boolean success = assembler.assembleSourceCode(view.getSourceCode());
        if (success) {
            view.setObjectCode(assembler.getMachineCode());
            runObject();
        }
        view.setAsListing(turnErrorsToString(assembler.getErrorMessages()));
    }

    private void debugSource() {
        boolean success = assembler.assembleSourceCode(view.getSourceCode());
        if (success) {
            view.setObjectCode(assembler.getMachineCode());
            debugObject();
        }
        view.setAsListing(turnErrorsToString(assembler.getErrorMessages()));
    }

    private void assemble() {
        if (assembler.assembleSourceCode(view.getSourceCode())) {
            view.setObjectCode(assembler.getMachineCode());
        }
        view.setAsListing(turnErrorsToString(assembler.getErrorMessages()));
    }


    private String turnErrorsToString(ArrayList<String> errros) {
        StringBuilder sb = new StringBuilder();
        assembler.getErrorMessages().forEach((i) -> sb.append(i));
        return sb.toString();
    }
    /**
     * Opens a file and reads it into source. Does no checking just reads in a text file.
     */
    private void openSource() {
        FileIO.readFromFile(view);
    }

    /**
     * Attempts to save the text in the source code text area to a file chosen by the user.
     */
    private void saveSource() {
        FileIO.writeToFile(view);
    }

    private void singleStep() {
        machine.run(true);
    }

    private void resume() {
        view.disableDebug();
        machine.run(false);
    }
}
