package view;

import control.Controller;
import model.Assembler;
import model.Machine;

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
