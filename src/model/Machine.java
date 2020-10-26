package model;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

import control.ModelListener;

/**
 * Holds all of the parts necessary for the pep/8 machine to run. CPU, Memory, and input buffer
 * as well as a listener that it informs when stored values are updated.
 * @author Group 8, Lead: Walter Kagel
 * @version 10/26/2020
 */
public class Machine {

    /**
     * The CPU of the simulated machine.
     */
    private CPU myCPU;

    /**
     * The memory of the simulated machine.
     */
    private Memory myMem;

    /**
     * Holds the listener that will be notified when an update occurs to the stored values in the Machine.
     */
    private ModelListener listener;

    /**
     * Creates a new machine with a new CPU and Memory.
     */
    public Machine() {
        myCPU = new CPU();
        myMem = new Memory();
    }

    /**
     * Adds a listener to the Model.
     * @param listener
     */
    public void addListener(ModelListener listener) {
        this.listener = listener;
    }

    /**
     * Sets memory to the byte values given by setting memory at the start address and then
     * consecutively setting each next memory address until all values in the byte array
     * have been stored. This method exists as an easy way to set or reset memory before
     * execution begins.
     * @param startAddress starting memory address will be read as an unsigned short.
     *                     (No conversion from signed short to unsigned is necessary)
     * @param values byte[] storing all byte values to be stored into memory.
     */
    public void setMemory(short startAddress, byte ... values) {
        if (startAddress + values.length - 1 > 65535) {
            listener.errorMessage("Attempt to set memory outside of valid addresses.");
        }
        for (int i =0; i < values.length; i++) {
            myMem.storeByte((short) (startAddress + i), values[i]);
        }

    }

}
