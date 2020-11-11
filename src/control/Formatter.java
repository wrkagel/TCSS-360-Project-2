package control;

import java.util.ArrayList;

public class Formatter {

    /**
     * Turns pseudo ops into lines that are easier to convert into machine code. Replaces .ASCII, .BLOCK, and .WORD
     * with equivalent .BYTE lines.
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
                    for (char c:value.toCharArray()) {
                        if (c == '\\') {
                            isEscape = true;
                            continue;
                        }
                        if (c == '\"' && !isEscape) continue;
                        if (isEscape) {
                            c = getEscapeSequence(c);
                            isEscape = false;
                            if (c == ' ') {
                                errorMessages.add("Invalid escape sequence at line " +
                                        lineNumber + ".\n");
                                errors = true;
                                break;
                            }
                        }
                        if (isFirst) {
                            sourceLines.add(i, new SourceLine(symbol + ": .BYTE " + ((byte) c), lineNumber));
                            isFirst = false;
                        }
                        sourceLines.add(i, new SourceLine(".BYTE " + ((byte) c), lineNumber));
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
                case WORD -> {
                    String value = sourceLine.getValue();
                    int lineNumber = sourceLine.getLineNumber();
                    String symbol = sourceLine.getSymbol();
                    if (value.equals("")) {
                        errorMessages.add("Incorrect number of arguments for .ASCII pseudo-op at line " +
                                lineNumber + "./n");
                    }
                    int wordValue;
                    if (value.toUpperCase().startsWith("0X")) {
                        wordValue = Integer.parseInt(value.substring(2), 16);
                    } else {
                        wordValue = Integer.parseInt(value);
                    }
                    sourceLines.remove(i);
                    if (wordValue > Short.MAX_VALUE || wordValue < Short.MIN_VALUE) {
                        errorMessages.add("Value cannot fit in size of word at line " + lineNumber + ".\n");
                        errors = true;
                        break;
                    }
                    sourceLines.add(i, new SourceLine(symbol + ": .BYTE "  +
                            ((byte) (wordValue >> 8)), lineNumber));
                    i++;
                    sourceLines.add(i, new SourceLine(".BYTE " + ((byte) (wordValue & 0xFF)), lineNumber));
                }
            }
        }
        return errors;
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
            case '\"' -> '\"';
            case '0' -> '\0';
            default -> ' ';
        };
    }

}
