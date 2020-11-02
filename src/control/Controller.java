package control;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

import model.Assembler;
import model.Machine;
import view.GUI;

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

    @Override
    public String getInput() {
        return view.getBatchInput();
    }

    @Override
    public void registerUpdate(short[] values) {

    }

    @Override
    public void flagUpdate(boolean[] values) {

    }

    @Override
    public void memoryUpdate(byte[] values) {

    }

    @Override
    public void output(String outText) {

    }

    @Override
    public void errorMessage(String message) {

    }

    @Override
    public void buttonPushed(String name) {

    }

    @Override
    public void fileSelection(String name) {

    }

    @Override
    public void buildSelection(String name) {

    }

    @Override
    public void editSelection(String name) {

    }

    @Override
    public void traceSelection(String name) {

    }
}
