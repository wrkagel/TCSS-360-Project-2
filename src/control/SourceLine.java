package control;

public class SourceLine {

    private final String symbol;

    private final Mnemonic mnemonic;

    private final String value;

    private final int lineNumber;

    public SourceLine(String rawSourceLine, int lineNumber) {
        this.lineNumber = lineNumber;
        rawSourceLine = rawSourceLine.stripLeading();
        int commentIndex = rawSourceLine.indexOf(';');
        if (commentIndex != -1) {
            rawSourceLine = rawSourceLine.substring(0, commentIndex);
        }
        int symbolIndex = rawSourceLine.indexOf(':');
        if (symbolIndex != -1) {
            symbol = rawSourceLine.substring(0, symbolIndex);
            rawSourceLine = rawSourceLine.substring(symbolIndex + 1);
            rawSourceLine = rawSourceLine.stripLeading();
        } else {
            symbol = "";
        }
        String[] tokens = rawSourceLine.split("\\s");
        if (tokens.length > 2) throw new IllegalArgumentException("Incorrect number of arguments at line " +
                lineNumber + ".\n");
        if (tokens.length == 0 || commentIndex == 0) {
            mnemonic = null;
            value = "";
        } else {
            try {
                if (tokens[0].charAt(0) == '.') {
                    mnemonic = Mnemonic.valueOf(tokens[0].toUpperCase().substring(1));
                } else {
                    mnemonic = Mnemonic.valueOf(tokens[0].toUpperCase());
                }
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(e.getMessage() + " at line " + lineNumber + ".\n");
            }
            if (tokens.length == 2) {
                value = tokens[1];
            } else {
                value = "";
            }
        }
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public Mnemonic getMnemonic() {
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
        String str = "";
        if (!symbol.equals("")) {
            str = symbol + ": ";
        }
        return str + mnemonic + " " + value;
    }
}
