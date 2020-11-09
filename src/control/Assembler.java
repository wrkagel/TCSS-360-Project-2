package control;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

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

    /**
     * Stores the assembly listing generated during assembly.
     */
    private String assemblerListing;

    /**
     * Stores all error messages generated during assembly.
     */
    private String errorMessages;

    //Actual assembler methods still need to be implemented.

    /**
     * Create and initialize an assembler.
     */
    public Assembler() {
        machineCode = "";
        assemblerListing = "";
        errorMessages = "";
    }

    /**
     * Takes in source code and assembles it into machine code, stored in the machineCode field, and an
     * assembly listing, stored in the assemblyListing field. If any errors are generated during assembly, then
     * nothing is stored and all generated error messages are added onto the errorMessages field.
     * @param sourceCodeIn the source code to be assembled.
     * @return whether the assembly completed without error (true), or had errors (false).
     */
    public boolean assembleSourceCode(String sourceCodeIn) {
        var sourceLines = new ArrayList<>(Arrays.asList(sourceCodeIn.split("\n")));
        if (!parsePseudoInstructions(sourceLines)) return false;
        buildSymbolTable();
        return false;
    }

    /**
     * Returns the machine code assembled from any given source code. Returns "" if there was an error during
     * assembly or if no assembly has yet taken place.
     * @return machine code as a string of bytes.
     */
    public String getMachineCode() {
        return machineCode;
    }

    /**
     * Returns the assembly listing made while assembling any given source code.
     * @return generated assembly listing. Should be line separated during generation.
     */
    public String getAssemblerListing() {
        return assemblerListing;
    }

    /**
     * Returns all generated error messages. Each error message should be on it's own line.
     * @return error messages generated during assembly.
     */
    public String getErrorMessages() {
        return errorMessages;
    }

    /**
     * Strips off all comments and anything that comes after the .END sentinel, then turns pseudo-ops into
     * lines that are easier to assemble.
     * @param sourceLines the raw source code split into lines.
     * @return boolean of if errors occurred or not.
     */
    public boolean parsePseudoInstructions(ArrayList<String> sourceLines) {
        if (!removeCommentsAndEnd(sourceLines)) {
            return false;
        }
        boolean errors = false;
        for (int i = 0; i < sourceLines.size(); i++) {
            String str = sourceLines.get(i);
            if (str.length() == 0) continue;
            String[] tokens = str.split(" ");
            switch(tokens[0]) {
                //Turns the .ASCII pseudo-op into a series of .BYTE ops. One for each character.
                case ".ASCII" -> {
                    if (tokens.length != 2) {
                        errorMessages = errorMessages + "Incorrect number of arguments for .ASCII pseudo-op on line " +
                                + i + ".\n";
                        errors = true;
                        break;
                    }
                    String line = tokens[1];
                    sourceLines.remove(i);
                    boolean isEscape = false;
                    for (char c:line.toCharArray()) {
                        if (c == '\\') {
                            isEscape = true;
                            continue;
                        }
                        if (c == '\"' && !isEscape) continue;
                        if (isEscape) {
                            c = getEscapeSequence(c);
                            if (c == ' ') {
                                errorMessages = errorMessages + "Invalid escape sequence at line " +
                                        i + ".\n";
                                errors = true;
                                break;
                            }
                        }
                        sourceLines.add(i, ".BYTE " + (byte) c);
                        i++;
                    }
                    i--;
                }
                //Turns the .BLOCK pseudo-op into a series of .BYTE 00 lines.
                case ".BLOCK" -> {
                    if (tokens.length != 2) {
                        errorMessages = errorMessages + "Incorrect number of arguments for .BLOCK pseudo-op on line " +
                                i + ".\n";
                        errors = true;
                        break;
                    }
                    try {
                        int value;
                        if (tokens[1].substring(0, 2) == "0X") {
                            value = Integer.parseInt(tokens[1].substring(2), 16);
                        } else {
                            value = Integer.parseInt(tokens[1]);
                        }
                        sourceLines.remove(i);
                        for (; value > 0; value--) {
                            sourceLines.add(i, ".BYTE 00");
                            i++;
                        }
                        i--;
                    } catch (Exception e) {
                        errorMessages = errorMessages + "Error with .BLOCK at line " + i + ".\n";
                        errors = true;
                        break;
                    }
                }
                case ".WORD" -> {
                    if (tokens.length != 2) {
                        errorMessages = errorMessages + "Incorrect number of arguments for .WORD pseudo-op at line " +
                                i + ".\n";
                        errors = true;
                        break;
                    }
                    int value;
                    if (tokens[1].substring(0, 2) == "0X") {
                        value = Integer.parseInt(tokens[1].substring(2), 16);
                    } else {
                        value = Integer.parseInt(tokens[1]);
                    }
                    sourceLines.remove(i);
                    if (value > Short.MAX_VALUE || value < Short.MIN_VALUE) {
                        errorMessages = errorMessages + "Value too large to fit in a word at line " + i + ".\n";
                        errors = true;
                        break;
                    }
                    sourceLines.add(i, ".BYTE "  + (byte) (value >> 8));
                    i++;
                    sourceLines.add(i, ".BYTE " + (byte) (value & 0xFF));
                }
            }
        }
        return errors;
    }

    /**
     * Helper method that removes all comments and anything past the end of the program.
     * @param sourceLines
     */
    private boolean removeCommentsAndEnd(ArrayList<String> sourceLines) {
        boolean isEnd = false;
        for (int i = 0; i < sourceLines.size(); i++) {
            if (isEnd) {
                sourceLines.set(i, "");
            } else {
                String str = sourceLines.get(i).toUpperCase();
                if (str.equals(".END")) isEnd = true;
                int commentIndex = str.indexOf(';');
                if (commentIndex == -1) continue;
                if (commentIndex == 0) {
                    sourceLines.set(i, "");
                } else {
                    sourceLines.set(i, str.substring(0, commentIndex));
                }
            }
        }
        if (!isEnd) errorMessages = errorMessages + "No .END sentinel found within the assembly source code.\n";
        return !isEnd;
    }

    /**
     * Returns the proper escape sequence for the given character. Returns ' ' if a valid escape sequence is not found.
     * @param c char to be turned into an escape sequence
     * @return escape sequence or ' ' if invalid.
     */
    private char getEscapeSequence(char c) {
        switch(c) {
            case '\\': return '\\';
            case 'n': return '\n';
            case 't': return '\t';
            case '\"': return '\"';
            case '0' : return '\0';
            default: return ' ';
        }
    }

    private void buildSymbolTable() {

    }
}
