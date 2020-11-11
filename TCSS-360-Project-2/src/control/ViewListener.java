package control;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

/**
 * Interface that allows the GUI to send messages to the listener which will then interpret those messages and run
 * the appropriate methods.
 * These are split into multiple methods to allow for better division of the code.
 * @author Group 8, Lead: Walter Kagel
 * @version 10/25/2020
 */
public interface ViewListener {

    /**
     * Sends the name of the button pushed to the listener
     * @param name name of button
     */
    void buttonPushed(String name);

    /**
     * Sends the name of the selection chosen from the file menu bar item to the controller.
     * @param name name of selection
     */
    void fileSelection(String name);

    /**
     * Sends the name of the selection chosen from the build menu bar item to the controller.
     * @param name name of selection
     */
    void buildSelection(String name);
}
