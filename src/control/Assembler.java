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
    private ArrayList<String> errorMessages;

    private HashMap<String, Short> symbolTable;

    /**
     * Create and initializes an assembler.
     */
    public Assembler() {
        machineCode = "";
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
        //Grab and create the raw source lines
        var rawSourceLines = sourceCodeIn.split("\n");
        var sourceLines = new ArrayList<SourceLine>();
        //Reset error messages and machine code for the new assembly attempt
        errorMessages = new ArrayList<>();
        machineCode = "";
        boolean noErrors = true;
        boolean hasEnd = false;
        //Turn raw source lines into Source Line objects. Ignore lines that contain no code relevant to the
        //assembler. Ignore any lines after the .End sentinel.
        for (int i = 0; i < rawSourceLines.length; i++) {
            try {
                SourceLine sourceLine = new SourceLine(rawSourceLines[i], i);
                if (sourceLine.getMnemonic() != null) sourceLines.add(sourceLine);
                if (sourceLine.getMnemonic() == Mnemonic.END) {
                    hasEnd = true;
                    break;
                }
            } catch (Exception e) {
                noErrors = false;
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
        return true;
    }

    /**
     * Takes in the sourceLines after they have been parsed for pseudo-ops and after the symbol table has been built,
     * then builds and stores the machine code into the machine code string.
     * @param sourceLines ArrayList of SourceLine
     * @return boolean false if errors occurred, true otherwise.
     */
    private boolean buildMachineCode(ArrayList<SourceLine> sourceLines){
        boolean errors = false;
        StringBuilder sb = new StringBuilder();
        for(SourceLine sourceLine:sourceLines) {
            String value = sourceLine.getValue();
            //Turns .BYTE pseudoOp into a pair of hex characters that are added to machine code.
            if (sourceLine.getMnemonic() == Mnemonic.BYTE) {
                int dec = Integer.parseInt(value);
                String hex = Integer.toHexString(dec);
                if(hex.length() < 2) hex = "0" + hex;
                sb.append(hex);
            } else if (value.equals("")) { //Deal with any unary mnemonics
                sb.append(sourceLine.getMnemonic().getMachineCode());
            } else { //Deal with all non-unary mnemonics.
                //non-mnemonics must have an addressing mode.
                int modeIndex = value.indexOf(',');
                if (modeIndex == -1) {
                    errorMessages.add("Instruction requires an addressing mode at line " + sourceLine.getLineNumber() +
                            ".\n");
                    errors = true;
                    continue;
                }
                //Split apart address and addressign mode
                String[] tokens = value.split(",");
                int operandValue;
                try {
                    //Use AddressingMode Enum from model to more easily create the hex string characters for the
                    //mnemonic
                    AddressingMode mode = AddressingMode.valueOf(tokens[1].toUpperCase());
                    sb.append(sourceLine.getMnemonic().getMachineCode(mode));
                    sb.append(" ");
                    //Turn the value into machine code hex characters and add them.
                    if (tokens[0].toUpperCase().startsWith("0X")) {
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
                String hex = Integer.toHexString(operandValue & 0xFFFF);
                while (hex.length() < 4) hex = "0" + hex;
                sb.append(hex, 0, 2);
                sb.append(" ");
                sb.append(hex, 2, 4);
            }
            sb.append(" ");
        }
        if (!errors) machineCode = sb.toString();
        return !errors;
    }
}
