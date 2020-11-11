package control;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

import model.AddressingMode;

import java.util.ArrayList;
import java.util.HashMap;

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
     * Stores all error messages generated during assembly.
     */
    private final ArrayList<String> errorMessages;

    private HashMap<String, Short> symbolTable;

    /**
     * Create and initialize an assembler.
     */
    public Assembler() {
        machineCode = "";
    //    assemblerListing = "";
        errorMessages = new ArrayList<>();
        symbolTable = new HashMap<>();
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
        boolean hasEnd = false;
        for (int i = 0; i < rawSourceLines.length; i++) {
            try {
                SourceLine sourceLine = new SourceLine(rawSourceLines[i], i);
                if (sourceLine.getMnemonic() != null) sourceLines.add(sourceLine);
                if (sourceLine.getMnemonic() == Mnemonic.END) {
                    hasEnd = true;
                    break;
                }
            } catch (Exception e) {
                noErrors = true;
                errorMessages.add(e.getMessage());
            }
        }
        if(!hasEnd) {
            noErrors = false;
            errorMessages.add("Source code does not contain the .END sentinel.");
        }
        return noErrors &&
                Formatter.parsePseudoInstructions(sourceLines, errorMessages) &&
                buildSymbolTable(sourceLines) &&
                buildMachineCode(sourceLines);
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
     * ease of determining addresses when building the symbol table. Builds the symbol table.
     * @param sourceLines an ArrayList of sourceLines that contains only those lines that concern the program
     *                    directly. (No comments, blank lines, or anything after the .END pseudoInstruction.
     * @return boolean false if errors occurred, true otherwise.
     */
    private boolean buildSymbolTable(ArrayList<SourceLine> sourceLines) {
        return false;
    }

    /**
     * Takes in the sourceLines after they have been parsed for pseudo-ops and after the symbol table has been built,
     * then builds and stores the machine code into the machine code string.
     * @param sourceLines
     * @return boolean false if errors occurred, true otherwise.
     */
    private boolean buildMachineCode(ArrayList<SourceLine> sourceLines){
        boolean errors = false;
        for(SourceLine sourceLine:sourceLines) {
            StringBuilder sb = new StringBuilder();
            String value = sourceLine.getValue();
            if (value.equals("")) {
                sb.append(sourceLine.getMnemonic().getMachineCode());
                sb.append(" ");
            } else {
                int modeIndex = value.indexOf(',');
                if (modeIndex == -1) {
                    errorMessages.add("Instruction requires an addressing mode at line " + sourceLine.getLineNumber() +
                            ".\n");
                    errors = true;
                    continue;
                }
                String[] tokens = value.split(",");
                int operandValue;
                try {
                    AddressingMode mode = AddressingMode.valueOf(tokens[1]);
                    sb.append(sourceLine.getMnemonic().getMachineCode(mode));
                    sb.append(" ");
                    if (tokens[0].toUpperCase().equals("0X")) {
                        tokens[0] = tokens[0].substring(2);
                        if (tokens[0].length() > 4) throw new IllegalArgumentException();
                        operandValue = Integer.parseInt(tokens[0], 16);
                    } else {
                        operandValue = Integer.parseInt(tokens[0]);
                    }
                    if (operandValue > Short.MAX_VALUE || operandValue < Short.MIN_VALUE) {
                        throw new IllegalArgumentException();
                    }
                } catch (IllegalArgumentException e) {
                    if (e.getClass() == NumberFormatException.class) {
                       Short temp = symbolTable.get(value);
                       if (temp == null) {

                           errorMessages.add("Error when translating line " + sourceLine.getLineNumber() + " to machine " +
                                   "code.\n");
                           errors = true;
                           continue;
                       }
                       operandValue = temp;
                    } else {
                        errorMessages.add("Error when translating line " + sourceLine.getLineNumber() + " to machine " +
                                "code.\n");
                        errors = true;
                        continue;
                    }
                }
                sb.append(Integer.toHexString((operandValue & 0xFF00) >>> 8));
                sb.append(" ");
                sb.append(Integer.toHexString(operandValue & 0xFF));
                sb.append(" ");
            }
        }
        return !errors;
    }
}
