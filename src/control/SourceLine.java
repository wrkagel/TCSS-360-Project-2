package control;

public class SourceLine {

    private String symbol;

    private String mnemonic;

    private String value;

    private int lineNumber;

    public SourceLine(String rawSourceLine, int lineNumber) {
        this.lineNumber = lineNumber;
        rawSourceLine = rawSourceLine.stripLeading();
        int commentIndex = rawSourceLine.indexOf(';');
        if (commentIndex > 0) {
            rawSourceLine = rawSourceLine.substring(0, commentIndex);
        }
        int symbolIndex = rawSourceLine.indexOf(';');
        if (symbolIndex > 7) throw new IllegalArgumentException("Symbol name too long or not at beginning of " +
                "line at line " + lineNumber + ".\n");
        if (symbolIndex != -1) {
            symbol = rawSourceLine.substring(symbolIndex);
            rawSourceLine = rawSourceLine.substring(symbolIndex);
            rawSourceLine = rawSourceLine.stripLeading();
        } else {
            symbol = "";
        }
        String[] tokens = rawSourceLine.split("\\s");
        if (tokens.length > 2) throw new IllegalArgumentException("Incorrect number of arguments at line " +
                lineNumber + ".\n");
        if (tokens.length == 0 || commentIndex == 0) {
            mnemonic = "";
            value = "";
        } else {
            mnemonic = tokens[0].toUpperCase();
            if (rawSourceLine.length() > tokens[0].length()) {
                value = rawSourceLine.substring(tokens[0].length() + 1);
            } else {
                value = "";
            }
        }
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public String getValue() {
        return value;
    }

    public String getSymbol() {
        return symbol;
    }

    @Override
    public String toString() {
        return mnemonic + " " + value;
    }
}
