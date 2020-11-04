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
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Controls all communication and updating between the model and view packages. Updates the view when a change
 * in model has occurred. Calls appropriate functions upon a user action on the view.
 *
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
            case "Run Object" -> {
                try {
                    machine.setMemory((short) 0, parseObjectCode(view.getObjectCode()));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(view, e.getMessage());
                }
                machine.run(false);
            }
            case "Debug Object" -> {
                try {
                    machine.setMemory((short) 0, parseObjectCode(view.getObjectCode()));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(view, e.getMessage());
                }
                machine.run(true);
            }
            case "Save" -> saveSource();
        }
    }

    /**
     * Takes in the name of the
     * @param name name of selection
     */
    @Override
    public void fileSelection(String name) {
        switch(name) {
            case "Open" -> openFile();
            case "New" -> reset();
        }
    }

    @Override
    public void buildSelection(String name) {
    }

    /**
     * Resets the machine with a fresh cpu and memory.
     */
    private void reset() {
        machine.reset();
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

    /**
     * Opens a file and reads it into source. Does no checking just reads in a text file.
     */
    private void openFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Text files", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(view);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " +
                    chooser.getSelectedFile().getName());

            File inputFile = chooser.getSelectedFile();
            if (!inputFile.canRead()) {
                JOptionPane.showMessageDialog(view, inputFile.getName() + " cannot be" +
                        " read.");
            } else {
                try (Scanner sc = new Scanner(inputFile)) {
                    StringBuilder sb = new StringBuilder();
                    while (sc.hasNext()) {
                        sb.append(sc.nextLine());
                    }
                    view.setSourceCode(sb.toString());
                } catch (FileNotFoundException e) {
                    JOptionPane.showMessageDialog(view, e.getMessage());
                }
            }
        }
    }

    private void saveSource() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Text files", "txt");
        chooser.setFileFilter(filter);
        boolean loop = true;
        while (loop) {
            int returnVal = chooser.showSaveDialog(view);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File outputFile = chooser.getSelectedFile();
                if (!outputFile.getAbsolutePath().endsWith(".txt")) {
                    outputFile = new File(outputFile.getAbsolutePath() + ".txt");
                }
                try {
                    if (outputFile.createNewFile()) {
                        FileWriter fw = new FileWriter(outputFile);
                        fw.append(view.getSourceCode());
                        fw.close();
                        loop = false;
                    } else {
                        int overwrite = JOptionPane.showConfirmDialog(view, "This file already exists would you " +
                                "like to replace it?");
                        if (overwrite == JOptionPane.YES_OPTION) {
                             if (!outputFile.delete()) {
                                 JOptionPane.showMessageDialog(view, outputFile.getName() +" could not be replaced.");
                                 loop = false;
                             } else {
                                 if (outputFile.createNewFile()) {
                                     FileWriter fw = new FileWriter(outputFile);
                                     fw.append(view.getSourceCode());
                                     fw.close();
                                     loop = false;
                                }
                             }
                        } else if (overwrite == JOptionPane.NO_OPTION) {
                            continue;
                        } else {
                            loop = false;
                        }
                    }
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(view, e.getMessage());
                }
            } else {
                loop = false;
            }
        }
    }
}
