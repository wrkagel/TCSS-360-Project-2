package model;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

/**
 * Will take in raw text or file name and will attempt to assemble based on pep/8 assembly language the text
 * into machine language and assembly listing. Returns a boolean of if the assembly was succesful or not.
 * Stores the generated text in private fields accessible with getters and will log all errors into an errorText
 * string generated during the assembly attempt.
 */
public class Assembler {

    /**
     * Stores assembled machine code.
     */
    private String machineCode = "";

    /**
     * Stores the assembly listing generated during assembly.
     */
    private String assemblyListing = "";

    /**
     * Stores all error messages generated during assembly.
     */
    private String errorMessages = "";

    //Actual assembler methods still need to be implemented.
}
