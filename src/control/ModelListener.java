package control;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

/**
 * Interface that describes the methods used by the model to communicate with the listener when an
 * an internal change has occurred. The listener will then perform all necessary actions and function calls
 * based on that update information.
 */
public interface ModelListener {

    /**
     * Tells the listener that the caller is awaiting input. Listener will return the input.
     * @return input string
     */
    String getInput();

    /**
     * Tells the listener that a register with the given name has been update with the given value.
     * @param name name of register
     * @param value value of register
     */
    void registerUpdate(String name, short value);

    //Might want to change this to be (short location, byte value) so only the affected memory location needs to be updated
    //which will likely be faster, but slightly more complicated to implement.
    /**
     * Sends a copy of memory to the listener when a memory value has been changed.
     * @param mem copy of memory
     */
    void memoryUpdate(byte[] mem);

    /**
     * Tells the listener that output has been generated.
     * @param outText generated output
     */
    void output(String outText);
}
