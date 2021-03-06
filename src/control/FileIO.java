package control;

import view.GUI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

/**
 * FileIO handles the file input and output for the pep/8 Simulator.
 * @author Group 8, Lead: Walter
 * @version 11/07/2020
 */
public class FileIO {

    /**
     * Opens a file chooser for a person to choose a file to read from. Reads in the file into the source code text
     * box of the GUI.
     * @param view used to popup JFileChooser, error messages, and set source code.
     */
    public static void readFromFile(GUI view) {

        JFileChooser chooser = new JFileChooser();
        StringBuilder sb = new StringBuilder();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Text files", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(view);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            //Check that the file can actually be read from.
            if (!file.canRead()) {
                throw new IllegalArgumentException(file.getName() + " cannot be read.");
            } else {
                try (Scanner sc = new Scanner(file)) {
                    while (sc.hasNext()) {
                        sb.append(sc.nextLine() + '\n');
                    }
                } catch (FileNotFoundException e) {
                    JOptionPane.showMessageDialog(view, e.getMessage());
                }
            }
        }
        view.setSourceCode(sb.toString());
    }

    /**
     * Writes the source text area of view to a file chosen by the user using JFileChooser. Will ask if
     * the user wishes to replace the file if it already exists.
     * @param view used to popup JFileChooser, error messages, and get source code to save.
     */
    public static void writeToFile(GUI view) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Text files", "txt");
        chooser.setFileFilter(filter);
        //Loop until the user has successfully chosen a file location to save to or has cancelled out.
        boolean loop = true;
        while (loop) {
            int returnVal = chooser.showSaveDialog(view);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File outputFile = chooser.getSelectedFile();
                //Add the .txt file extension if the user didn't do it already
                if (!outputFile.getAbsolutePath().endsWith(".txt")) {
                    outputFile = new File(outputFile.getAbsolutePath() + ".txt");
                }
                try {
                    //Try creating a file at the chosen location
                    if (outputFile.createNewFile()) {
                        FileWriter fw = new FileWriter(outputFile);
                        fw.append(view.getSourceCode());
                        fw.close();
                        loop = false;
                    } else {
                        //Ask the user if they want to overwrite an existing file.
                        int overwrite = JOptionPane.showConfirmDialog(view,
                                "This file already exists would you like to replace it?");
                        if (overwrite == JOptionPane.YES_OPTION) {
                            if (!outputFile.delete()) {
                                //Inform the user that the file cannot be replaced.
                                JOptionPane.showMessageDialog(view,
                                        outputFile.getName() +" could not be replaced.");
                            } else {
                                if (outputFile.createNewFile()) {
                                    FileWriter fw = new FileWriter(outputFile);
                                    fw.append(view.getSourceCode());
                                    fw.close();
                                    loop = false;
                                }
                            }
                        } else {
                            if (overwrite != JOptionPane.NO_OPTION) {
                                loop = false;
                            }
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
