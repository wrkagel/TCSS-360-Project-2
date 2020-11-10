package control;

import java.util.ArrayList;

public class Formatter {

    /**
     * Helper method that removes all comments and anything past the end of the program.
     * This also turns all of the text to upper case.
     * Note that lines that contain only comments, but are before .END will be blank strings instead of being
     * removed. This is done so that the line count for error message generation remains correct.
     * @param sourceLines ArrayList containing unformatted lines of source code.
     * @param errorMessages Used to store any error messages generated during the method.
     * @return false if errors were generated, true otherwise.
     */
    public static boolean removeCommentsAndEnd(ArrayList<String> sourceLines, ArrayList<String> errorMessages) {
        boolean isEnd = false;
        for (int i = 0; i < sourceLines.size(); i++) {
            String str = sourceLines.get(i).toUpperCase();
            if (str.equals(".END")) {
                isEnd = true;
            }
            if (isEnd) {
                sourceLines.remove(i);
                i--;
            } else {
                int commentIndex = str.indexOf(';');
                if (commentIndex == -1) continue;
                if (commentIndex == 0) {
                    sourceLines.set(i, "");
                } else {
                    sourceLines.set(i, str.substring(0, commentIndex));
                }
            }
        }
        if (!isEnd) errorMessages.add("No .END sentinel found within the source code.\n");
        return !isEnd;
    }

    /**
     * Turns pseudo ops into lines that are easier to convert into machine code. Replaces .ASCII, .BLOCK, and .WORD
     * with equivalent .BYTE lines.
     * @param sourceLines ArrayList containing the lines of source code with all non-instructions removed.
     * @return false if errors were generated, true otherwise.
     */
    public static boolean parsePseudoInstructions(ArrayList<String> sourceLines, ArrayList<String> errorMessages) {
        boolean errors = false;
        for (int i = 0; i < sourceLines.size(); i++) {
            String str = sourceLines.get(i);
            if (str.length() == 0) continue;
            String[] tokens = str.split(" ");
            switch(tokens[0].toUpperCase()) {
                //Turns the .ASCII pseudo-op into a series of .BYTE ops. One for each character.
                case ".ASCII" -> {
                    if (tokens.length != 2) {
                        errorMessages.add("Incorrect number of arguments for .ASCII pseudo-op on line " +
                                + i + ".\n");
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
                            isEscape = false;
                            if (c == ' ') {
                                errorMessages.add("Invalid escape sequence at line " +
                                        i + ".\n");
                                errors = true;
                                break;
                            }
                        }
                        sourceLines.add(i, ".BYTE " + ((byte) c));
                        i++;
                    }
                    i--;
                }
                //Turns the .BLOCK pseudo-op into a series of .BYTE 00 lines.
                case ".BLOCK" -> {
                    if (tokens.length != 2) {
                        errorMessages.add("Incorrect number of arguments for .BLOCK at line " +
                                i + ".\n");
                        errors = true;
                        break;
                    }
                    try {
                        int value;
                        if (tokens[1].toUpperCase().startsWith("0X")) {
                            value = Integer.parseInt(tokens[1].substring(2), 16);
                        } else {
                            value = Integer.parseInt(tokens[1]);
                        }
                        if (value < 1) {
                            errorMessages.add("Argument for .BLOCK cannot be less than one. Error at line " + i + ".\n");
                            break;
                        }
                        sourceLines.remove(i);
                        for (; value > 0; value--) {
                            sourceLines.add(i, ".BYTE 00");
                            i++;
                        }
                        i--;
                    } catch (Exception e) {
                        errorMessages.add("Error parsing number for .BLOCK at line " + i + ".\n");
                        errors = true;
                    }
                }
                case ".WORD" -> {
                    if (tokens.length != 2) {
                        errorMessages.add("Incorrect number of arguments for .WORD pseudo-op at line " +
                                i + ".\n");
                        errors = true;
                        break;
                    }
                    int value;
                    if (tokens[1].toUpperCase().startsWith("0X")) {
                        value = Integer.parseInt(tokens[1].substring(2), 16);
                    } else {
                        value = Integer.parseInt(tokens[1]);
                    }
                    sourceLines.remove(i);
                    if (value > Short.MAX_VALUE || value < Short.MIN_VALUE) {
                        errorMessages.add("Value too large to fit in a word at line " + i + ".\n");
                        errors = true;
                        break;
                    }
                    sourceLines.add(i, ".BYTE "  + ((byte) (value >> 8)));
                    i++;
                    sourceLines.add(i, ".BYTE " + ((byte) (value & 0xFF)));
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
