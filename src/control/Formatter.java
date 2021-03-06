package control;

import java.util.ArrayList;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

/**
 * Formats source lines so that they can be used for building the symbol table and then turned into machine code.
 * @author Group 8
 * @version 11/12/2020
 */
public class Formatter {

    /**
     * Turns Source Lines with pseudo ops into lines that are easier to convert into machine code.
     * Replaces .ASCII, .BLOCK, and .WORD with equivalent .BYTE lines.
     * @param sourceLines ArrayList containing the lines of source code with all non-instructions removed.
     * @return false if errors were generated, true otherwise.
     */
    public static boolean parsePseudoInstructions(ArrayList<SourceLine> sourceLines, ArrayList<String> errorMessages) {
        boolean errors = false;
        for (int i = 0; i < sourceLines.size(); i++) {
            SourceLine sourceLine = sourceLines.get(i);
            switch(sourceLine.getMnemonic()) {
                //Turns the .ASCII pseudo-op into a series of .BYTE ops. One for each character.
                case ASCII -> {
                    String value = sourceLine.getValue();
                    int lineNumber = sourceLine.getLineNumber();
                    String symbol = sourceLine.getSymbol();
                    if (value.equals("")) {
                        errorMessages.add("Incorrect number of arguments for .ASCII pseudo-op at line " +
                                lineNumber + ".\n");
                        continue;
                    }
                    sourceLines.remove(i);

                    boolean isEscape = false;
                    boolean isFirst = true;
                    char[] chars = value.toCharArray();
                    for (int j = 0; j < chars.length; j++) {
                        StringBuilder sb = new StringBuilder(".BYTE ");
                        if (chars[j] == '\\') {
                            isEscape = true;
                            continue;
                        }
                        //Ignore non escaped quotation marks.
                        if (chars[j] == '\"' && !isEscape) continue;
                        //Deal with escape sequences.
                        if (isEscape) {
                            chars[j] = getEscapeSequence(chars[j]);
                            isEscape = false;
                            if (chars[j] == ' ') {
                                errorMessages.add("Invalid escape sequence at line " +
                                        lineNumber + ".\n");
                                errors = true;
                                break;
                            }
                            //Deal with byte literal escape sequence
                            if (chars[j] == 'x') {
                                try {
                                    String strHex = "" + chars[j + 1] + chars[j + 2];
                                    j += 2;
                                    int intHex = Integer.parseInt(strHex, 16);
                                    sb.append((byte) (intHex & 0xFF));
                                } catch (Exception e) {
                                    errorMessages.add("Unable to parse byte literal after \\x at line " +
                                            lineNumber + ".\n");
                                    break;
                                }
                            } else {
                                sb.append((byte) chars[j]);
                            }
                        } else {
                            sb.append((byte) chars[j]);
                        }
                        //Add symbol to first .BYTE so it can be used for symbol table if necessary.
                        if (isFirst && !symbol.equals("")) {
                            isFirst = false;
                            sb.insert(0, symbol + ": ");
                        }
                        sourceLines.add(i, new SourceLine(sb.toString(), lineNumber));
                        i++;
                    }
                    i--;
                }
                //Turns the .BLOCK pseudo-op into a series of .BYTE 00 lines.
                case BLOCK -> {
                    String value = sourceLine.getValue();
                    int lineNumber = sourceLine.getLineNumber();
                    String symbol = sourceLine.getSymbol();
                    if (value.equals("")) {
                        errorMessages.add("Incorrect number of arguments for .BLOCK at line " +
                                lineNumber + ".\n");
                        continue;
                    }
                    try {
                        int numberOfBytes;
                        if (value.toUpperCase().startsWith("0X")) {
                            numberOfBytes = Integer.parseInt(value.substring(2), 16);
                        } else {
                            numberOfBytes = Integer.parseInt(value);
                        }
                        if (numberOfBytes < 1) {
                            errorMessages.add("Argument for .BLOCK cannot be less than one. Error at line " +
                                    lineNumber + ".\n");
                            break;
                        }
                        sourceLines.remove(i);
                        sourceLines.add(i, new SourceLine((symbol + ": .BYTE 00"), lineNumber));
                        i++;
                        numberOfBytes--;
                        for (; numberOfBytes > 0; numberOfBytes--) {
                            sourceLines.add(i, new SourceLine(".BYTE 00", lineNumber));
                            i++;
                        }
                        i--;
                    } catch (Exception e) {
                        errorMessages.add("Error parsing number for .BLOCK at line " + lineNumber + ".\n");
                        errors = true;
                    }
                }
                //Turn a WORD pseudoOp into two two .BYTE pseudoOps
                case WORD -> {
                    String value = sourceLine.getValue();
                    int lineNumber = sourceLine.getLineNumber();
                    String symbol = sourceLine.getSymbol();
                    if (value.equals("")) {
                        errorMessages.add("Incorrect number of arguments for .WORD pseudo-op at line " +
                                lineNumber + ".\n");
                    }
                    int wordValue;
                    try {
                        if (value.toUpperCase().startsWith("0X")) {
                            wordValue = Integer.parseInt(value.substring(2), 16);
                        } else {
                            wordValue = Integer.parseInt(value);
                        }
                    } catch (NumberFormatException e) {
                        errorMessages.add("Could not parse value for .WORD at line " + lineNumber + ".\n");
                        errors = true;
                        break;
                    }
                    sourceLines.remove(i);
                    //Check that value isn't too big or small to fit in a word value (two bytes).
                    if (wordValue > Short.MAX_VALUE || wordValue < Short.MIN_VALUE) {
                        errorMessages.add("Value cannot fit in size of word at line " + lineNumber + ".\n");
                        errors = true;
                        break;
                    }
                    //Add symbol to first line if there is one.
                    sourceLines.add(i, new SourceLine(symbol + ": .BYTE "  +
                            ((byte) (wordValue >> 8)), lineNumber));
                    i++;
                    sourceLines.add(i, new SourceLine(".BYTE " + ((byte) (wordValue & 0xFF)), lineNumber));
                }
                //Checks that the .BYTE pseudoOp doesn't contain any errors and changes any hex values to
                //decimal for consistency.
                case BYTE -> {
                    String value = sourceLine.getValue();
                    int lineNumber = sourceLine.getLineNumber();
                    String symbol = sourceLine.getSymbol();
                    if (value.equals("")) {
                        errorMessages.add("Incorrect number of arguments for .BYTE pseudo-op at line " +
                                lineNumber + "./n");
                    }
                    int wordValue;
                    try {
                        if (value.toUpperCase().startsWith("0X")) {
                            wordValue = Integer.parseInt(value.substring(2), 16);
                        } else {
                            wordValue = Integer.parseInt(value);
                        }
                    } catch (NumberFormatException e) {
                        errorMessages.add("Could not parse value for .BYTE at line " + lineNumber + ".\n");
                        errors = true;
                        break;
                    }
                    sourceLines.remove(i);
                    if (wordValue > Byte.MAX_VALUE || wordValue < Byte.MIN_VALUE) {
                        errorMessages.add("Value cannot fit in size of word at line " + lineNumber + ".\n");
                        errors = true;
                        break;
                    }
                    //Since the byte is always only a single line we always add the symbol.
                    sourceLines.add(i, new SourceLine(symbol + ": " + ".BYTE " +
                            ((byte) (wordValue & 0xFF)), lineNumber));
                }
            }
        }
        return !errors;
    }

    /**
     * Returns the proper escape sequence for the given character. Returns ' ' if a valid escape sequence is not found.
     * @param c char to be turned into an escape sequence
     * @return escape sequence or ' ' if invalid.
     */
    private static char getEscapeSequence(char c) {
        return switch (c) {
            case '\\' -> '\\';
            case 'n' -> '\n';
            case 't' -> '\t';
            case 'x' -> 'x';
            case '\"' -> '\"';
            case '0' -> '\0';
            default -> ' ';
        };
    }

}
