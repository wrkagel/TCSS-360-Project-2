package control;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

/**
 * Holds the relevant information for a line of source code. Checks there are not too many arguments for a line of
 * code.
 * @author Group 8, Lead: Walter Kagel
 * @version 11/19/2020
 */
public class SourceLine {

    /**
     * Stores any symbol name that should be associated with this line.
     */
    private final String symbol;

    /**
     * Stores the mnemonic for the source line.
     */
    private final Mnemonic mnemonic;

    /**
     * Stores any value that should be associated with the mnemonic. This includes any addressing modes as well.
     */
    private final String value;

    /**
     * Stores the original line number of the source line. Used when creating error messages.
     */
    private final int lineNumber;

    /**
     * Parses the raw source line string extracting only the information that is relevant to the assembler.
     * @param rawSourceLine raw line of text that nothing has been done to.
     * @param lineNumber line number of raw text line.
     */
    public SourceLine(String rawSourceLine, int lineNumber) {
        this.lineNumber = lineNumber;
        //remove leading whitespace
        rawSourceLine = rawSourceLine.stripLeading();
        //remove comments if they exist
        int commentIndex = rawSourceLine.indexOf(';');
        if (commentIndex != -1) {
            rawSourceLine = rawSourceLine.substring(0, commentIndex);
        }
        //extract symbol if it exists
        int symbolIndex = rawSourceLine.indexOf(':');
        if (symbolIndex != -1) {
            symbol = rawSourceLine.substring(0, symbolIndex);
            rawSourceLine = rawSourceLine.substring(symbolIndex + 1);
            rawSourceLine = rawSourceLine.stripLeading();
        } else {
            symbol = "";
        }
        //Split the remaining line (should only be mnemonics and values) by whitespace
        String[] tokens = rawSourceLine.split("\\s");
        //If the remaining line was empty then set everything to blank.
        if (tokens[0] == "") {
            mnemonic = null;
            value = "";
        } else {
            //Determine and set the mnemonic
            try {
                if (tokens[0].charAt(0) == '.') {
                    mnemonic = Mnemonic.valueOf(tokens[0].toUpperCase().substring(1));
                } else {
                    mnemonic = Mnemonic.valueOf(tokens[0].toUpperCase());
                }
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e.getMessage() + " at line " + lineNumber + ".\n");
            }
            value = rawSourceLine.substring(tokens[0].length()).stripLeading();
        }
    }

    /**
     * Returns the line number.
     * @return original line number of the source line
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Returns the mnemonic of the source line
     * @return Mnemonic
     */
    public Mnemonic getMnemonic() {
        return mnemonic;
    }

    /**
     * Returns the value associated with the mnemonic. Includes addressing mode. If there is no value, then it
     * returns and empty String.
     * @return String
     */
    public String getValue() {
        return value;
    }

    /**
     * Returns the symbol name associated with this source line
     * @return String
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Returns a string including the symbol name (if it exists), the mnemonic, and the value (if it exists).
     * Will be in the form "[symbol]: [mnemonic] [value]"
     * @return String
     */
    @Override
    public String toString() {
        String str = "";
        if (!symbol.equals("")) {
            str = symbol + ": ";
        }
        return str + mnemonic + " " + value;
    }
}
