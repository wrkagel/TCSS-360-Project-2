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
 * Will take in raw text or file name and will attempt to assemble based on
 * pep/8 assembly language the text into machine language and assembly listing.
 * Returns a boolean of if the assembly was succesful or not. Stores the
 * generated text in private fields accessible with getters and will log all
 * errors into an errorText string generated during the assembly attempt.
 * 
 * @author Group 8
 * @version 11/18/2020
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
	 * Create and initialize an assembler.
	 */
	public Assembler() {
		machineCode = "";
		// assemblerListing = "";
		errorMessages = new ArrayList<>();
		symbolTable = new HashMap<>();
	}

	/**
	 * Takes in source code and assembles it into machine code, stored in the
	 * machineCode field, and an assembly listing, stored in the assemblyListing
	 * field. If any errors are generated during assembly, then nothing is stored
	 * and all generated error messages are added onto the errorMessages field.
	 * 
	 * @param sourceCodeIn the source code to be assembled.
	 * @return whether the assembly completed without error (true), or had errors
	 *         (false).
	 */
	public boolean assembleSourceCode(String sourceCodeIn) {
		symbolTable.clear();
		var rawSourceLines = sourceCodeIn.split("\n");
		var sourceLines = new ArrayList<SourceLine>();
		errorMessages = new ArrayList<>();
		machineCode = "";
		boolean noErrors = true;
		boolean hasEnd = false;
		for (int i = 0; i < rawSourceLines.length; i++) {
			try {
				SourceLine sourceLine = new SourceLine(rawSourceLines[i], i);
				if (sourceLine.getMnemonic() != null)
					sourceLines.add(sourceLine);
				if (sourceLine.getMnemonic() == Mnemonic.END) {
					hasEnd = true;
					break;
				}
			} catch (Exception e) {
				noErrors = false;
				errorMessages.add(e.getMessage());
			}
		}
		if (!hasEnd) {
			noErrors = false;
			errorMessages.add("Source code does not contain the .END sentinel.");
		}
		return noErrors && Formatter.parsePseudoInstructions(sourceLines, errorMessages)
				&& buildSymbolTable(sourceLines) && buildMachineCode(sourceLines);
	}

	/**
	 * Returns the machine code assembled from any given source code. Returns "" if
	 * there was an error during assembly or if no assembly has yet taken place.
	 * 
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
	 * Returns all generated error messages. Each error message should be on it's
	 * own line.
	 * 
	 * @return error messages generated during assembly.
	 */
	public ArrayList<String> getErrorMessages() {
		return errorMessages;
	}

	/**
	 * Takes in the sourceLines after they have had all the pseudoInstructions
	 * changed into .BYTES for ease of determining addresses when building the
	 * symbol table. Builds the symbol table.
	 * 
	 * @param sourceLines an ArrayList of sourceLines that contains only those lines
	 *                    that concern the program directly.j(No comments, blank
	 *                    lines, or anything after the .END pseudoInstruction.
	 * @return boolean false if errors occurred, true otherwise.
	 */
	private boolean buildSymbolTable(ArrayList<SourceLine> sourceLines) {
		short address = 0;
		boolean noError = true;

		for (int i = 0; i < sourceLines.size(); i++) { // iterates through sourceLines
			String symbol = sourceLines.get(i).getSymbol();
			if (isValidSymbol(symbol)  // if symbol is valid
					&& !symbolTable.containsKey(symbol) // if doesn't exist in symbol table
					&& !symbol.equals("")) { // if symbol isn't blank
				// if symbol doesn't equal "", adds to symbolTable
				symbolTable.put(sourceLines.get(i).getSymbol(), address);
			} else {
				if (sourceLines.get(i).getSymbol().equals("")) {
					// if symbol equals "", skips line, increments address
				} else if (!isValidSymbol(sourceLines.get(i).getSymbol())) {
					// error message if symbol isn't valid in general
					errorMessages.add("Invalid symbol name at line " + sourceLines.get(i).getLineNumber() + ".\n");
					noError = false;
				} else if (symbolTable.containsKey(sourceLines.get(i).getSymbol())) {
					// error message if symbol exists already
					errorMessages.add(
							"Symbol at line " + sourceLines.get(i).getLineNumber() + " already exists in symbol table.\n");
					noError = false;
				}
				// will return false in event of invalid symbol
			}
			address = increment(address, sourceLines.get(i).getMnemonic(), sourceLines.get(i).getValue());
		}
		return noError;
	}

	/**
	 * @param address  Current address
	 * @param mnemonic Current sourceLine line's mnemonic
	 * @param value    Current sourceLines line's value
	 * @return Returns the new incremented value of address
	 */
	private short increment(final short address, final Mnemonic mnemonic, final String value) {
		short newAddress = address;
		if (mnemonic == Mnemonic.BYTE || value.equals("")) {
			newAddress++;
		} else {
			newAddress += 3;
		}
		return newAddress;
	}

	/**
	 * Symbols are valid when <8 in length, first character is a letter, and isn't
	 * equal to ""
	 * 
	 * @param a Symbol parameter
	 * @return returns whether symbol is valid or not
	 */
	private boolean isValidSymbol(final String a) {
		if (!a.equals("") && a.length() < 8 && Character.isAlphabetic(a.charAt(1))) {
			for (int i = 0; i < a.length(); i++) {
				if (!Character.isLetterOrDigit(a.charAt(i))) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Takes in the sourceLines after they have been parsed for pseudo-ops and after
	 * the symbol table has been built, then builds and stores the machine code into
	 * the machine code string.
	 * 
	 * @param sourceLines ArrayList of SourceLine
	 * @return boolean false if errors occurred, true otherwise.
	 */
	private boolean buildMachineCode(ArrayList<SourceLine> sourceLines) {
		boolean errors = false;
		StringBuilder sb = new StringBuilder();
		for (SourceLine sourceLine : sourceLines) {
			String value = sourceLine.getValue();
			if (sourceLine.getMnemonic() == Mnemonic.BYTE) {
				int dec = Integer.parseInt(value);
				String hex = Integer.toHexString(dec & 0xFF).toUpperCase();
				if (hex.length() < 2)
					hex = "0" + hex;
				sb.append(hex);
			} else if (value.equals("")) {
				sb.append(sourceLine.getMnemonic().getMachineCode());
			} else {
				int modeIndex = value.indexOf(',');
				if (modeIndex == -1) {
					errorMessages.add(
							"Instruction requires an addressing mode at line " + sourceLine.getLineNumber() + ".\n");
					errors = true;
					continue;
				}
				String[] tokens = value.split(",");
				int operandValue;
				try {
					AddressingMode mode = AddressingMode.valueOf(tokens[1].toUpperCase());
					sb.append(sourceLine.getMnemonic().getMachineCode(mode));
					sb.append(" ");
					if (tokens[0].toUpperCase().startsWith("0X")) {
						tokens[0] = tokens[0].substring(2);
						if (tokens[0].length() > 4)
							throw new IllegalArgumentException();
						operandValue = Integer.parseInt(tokens[0], 16);
					} else {
						operandValue = Integer.parseInt(tokens[0]);
					}
					if (operandValue > Short.MAX_VALUE || operandValue < Short.MIN_VALUE) {
						throw new IllegalArgumentException();
					}
				} catch (IllegalArgumentException e) {
					if (e.getClass() == NumberFormatException.class) {
						Short temp = symbolTable.get(tokens[0]);
						if (temp == null) {

							errorMessages.add("Error when translating line " + sourceLine.getLineNumber()
									+ " to machine " + "code.\n");
							errors = true;
							continue;
						}
						operandValue = temp;
					} else {
						errorMessages.add("Error when translating line " + sourceLine.getLineNumber() + " to machine "
								+ "code.\n");
						errors = true;
						continue;
					}
				}
				String hex = Integer.toHexString(operandValue & 0xFFFF);
				while (hex.length() < 4)
					hex = "0" + hex;
				sb.append(hex, 0, 2);
				sb.append(" ");
				sb.append(hex, 2, 4);
			}
			sb.append(" ");
		}
		if (!errors)
			machineCode = sb.toString();
		return !errors;
	}
}
