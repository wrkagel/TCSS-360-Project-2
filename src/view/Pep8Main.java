package view;

import control.Controller;
import control.Assembler;
import model.Machine;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

/**
 * This instantiates the pieces of the pep/8 simulator and then adds the controller as a listener.
 * @author Group 8
 * @version 11/6/2020
 */
public class Pep8Main {

    public static void main(String[] args) {
        GUI view = new GUI();
        Machine machine = new Machine();
        Assembler assembler = new Assembler();
        Controller controller = new Controller(machine, assembler, view);
        view.addViewListener(controller);
        machine.addListener(controller);
    }

}
