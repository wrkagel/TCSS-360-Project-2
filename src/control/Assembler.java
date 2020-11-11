package control;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

import java.util.ArrayList;

/**
 * Will take in raw text or file name and will attempt to assemble based on pep/8 assembly language the text
 * into machine language and assembly listing. Returns a boolean of if the assembly was succesful or not.
 * Stores the generated text in private fields accessible with getters and will log all errors into an errorText
 * string generated during the assembly attempt.
 * @author Group 8
 * @version 11/08/2020
 */
public class Assembler {

    /**
     * Stores assembled machine code.
     */
    private String machineCode;

//    /**
//     * Stores the assembly listing generated during assembly.
//     */
//    private String assemblerListing;

    /**
     * Stores all error messages generated during assembly.
     */
    private final ArrayList<String> errorMessages;

    //Actual assembler methods still need to be implemented.

    /**
     * Create and initialize an assembler.
     */
    public Assembler() {
        machineCode = "";
    //    assemblerListing = "";
        errorMessages = new ArrayList<>();
    }

    /**
     * Takes in source code and assembles it into machine code, stored in the machineCode field, and an
     * assembly listing, stored in the assemblyListing field. If any errors are generated during assembly, then
     * nothing is stored and all generated error messages are added onto the errorMessages field.
     * @param sourceCodeIn the source code to be assembled.
     * @return whether the assembly completed without error (true), or had errors (false).
     */
    public boolean assembleSourceCode(String sourceCodeIn) {
        var rawSourceLines = sourceCodeIn.split("\n");
        var sourceLines = new ArrayList<SourceLine>();
        boolean noErrors = false;
        for (int i = 0; i < rawSourceLines.length; i++) {
            try {
                SourceLine sourceLine = new SourceLine(rawSourceLines[i], i);
                if (sourceLine.getMnemonic() != "") sourceLines.add(sourceLine);
                if (sourceLine.getMnemonic().toUpperCase() == ".END") break;
            } catch (Exception e) {
                noErrors = true;
                errorMessages.add(e.getMessage());
            }
        }
        return noErrors &&
                Formatter.parsePseudoInstructions(sourceLines, errorMessages) &&
                buildSymbolTable(sourceLines) &&
                buildMachineCode();
    }

    /**
     * Returns the machine code assembled from any given source code. Returns "" if there was an error during
     * assembly or if no assembly has yet taken place.
     * @return machine code as a string of bytes.
     */
    public String getMachineCode() {
        return machineCode;
    }

//    /**
//     * Returns the assembly listing made while assembling any given source code.
//     * @return generated assembly listing. Should be line separated during generation.
//     */
//    public String getAssemblerListing() {
//        return assemblerListing;
//    }

    /**
     * Returns all generated error messages. Each error message should be on it's own line.
     * @return error messages generated during assembly.
     */
    public ArrayList<String> getErrorMessages() {
        return errorMessages;
    }

    /**
     * Takes in the sourceLines after they have had all the pseudoInstructions changed into .BYTES for
     * ease of determining addresses when building the symbol table.
     * @param sourceLines an ArrayList of sourceLines that contains only those lines that concern the program
     *                    directly. (No comments, blank lines, or anything after the .END pseudoInstruction.
     * @return boolean false if errors occurred, true otherwise.
     */
    private boolean buildSymbolTable(ArrayList<SourceLine> sourceLines) {
        return false;
    }

    private boolean buildMachineCode(){
        return false;
    }
}
