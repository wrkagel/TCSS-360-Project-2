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

    public static void readFromFile(GUI view) {

        JFileChooser chooser = new JFileChooser();
        StringBuilder sb = new StringBuilder();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Text files", "txt");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(view);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.canRead()) {
                throw new IllegalArgumentException(file.getName() + " cannot be read.");
            } else {
                try (Scanner sc = new Scanner(file)) {
                    while (sc.hasNext()) {
                        sb.append(sc.nextLine());
                    }
                } catch (FileNotFoundException e) {
                    JOptionPane.showMessageDialog(view, e.getMessage());
                }
            }
        }
        view.setSourceCode(sb.toString());
    }



    public static void writeToFile(GUI view) {
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
                        int overwrite = JOptionPane.showConfirmDialog(view,
                                "This file already exists would you like to replace it?");
                        if (overwrite == JOptionPane.YES_OPTION) {
                            if (!outputFile.delete()) {
                                JOptionPane.showMessageDialog(view,
                                        outputFile.getName() +" could not be replaced.");
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
