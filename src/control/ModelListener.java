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
 * @version 11/4/2020
 */
public interface ModelListener {

    /**
     * Tells the listener that the caller is awaiting input. Listener will return the input.
     * @return input string
     */
    String getInput();

    /**
     * Passes along a list of values for registers in a predetermined order.
     */
    void registerUpdate(short[] values);

    /**
     * Passes along a list of values for flags in a predetermined order.
     * @param values [N, Z, V, C] order
     */
    void flagUpdate(boolean[] values);

    /**
     * Gives the listener a full copy of memory to pass to the view.
     * @param values a full copy of all bytes in memory.
     */
    void memoryUpdate(byte[] values);

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
