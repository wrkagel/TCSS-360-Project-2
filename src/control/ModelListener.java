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
 * @author Group 8, Lead: Walter Kagel
 * @version 10/26/2020
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

    /**
     * Informs the listener of changes in memory.
     * @param startingAddress the starting memory address of the given values. Should be read as an unsigned short.
     * @param values a consecutive set of values in memory with the first value being the value at the
     *               starting address.
     */
    void memoryUpdate(short startingAddress, byte ... values);

    /**
     * Tells the listener that output has been generated.
     * @param outText generated output
     */
    void output(String outText);

    /**
     * Sends any generated error message to the listener.
     * @param message the error message.
     */
    void errorMessage(String message);
}
