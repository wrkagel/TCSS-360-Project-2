package model;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

/**
 * Executes all arithmetic operations for arithmetic instructions for the pep/8 simulator.
 * Stores flags based on the most recent operation.
 * @author Group 8, Lead: RJ Alabado
 * @version 10/26/2020
 */
public class ALU {
	
	/**
	 * Controls how many shifts are computed
	 */
	private static final int rotateVal = 1;

    /**
     * Stores if the last operation resulted in a negative value.
     */
    private static boolean negativeFlag = false;

    /**
     * Stores if the last operation resulted in a zero value.
     */
    private static boolean zeroFlag = true;

    /**
     * Stores if the last operation caused on overflow.
     */
    private static boolean overflowFlag = false;

    /**
     * Stores the carry bit for the last operation.
     */
    private static boolean carryFlag = false;

    /**
     * Takes in two short values and returns their addition. Sets flags based on the result.
     * @return (short) (value1 + value2)
     */
    public static short add(short value1, short value2) {
        short returnVal = (short) (value1 + value2);
        checkNegative(returnVal);
        checkZero(returnVal);
        checkOverflow(value1, value2);
        checkCarry(value1, value2);
        return returnVal;
    }
    
    public static short sub(short value1, short value2) {
    	short returnVal = (short) (value1 - value2);
    	checkNegative(returnVal);
    	checkZero(returnVal);
    	checkOverflow(value1, value2);
    	checkCarry(value1, value2);
    	return returnVal;
    }

    public static short and(short value1, short value2) {
    	short returnVal = (short) (value1 & value2);
    	checkNegative(returnVal);
    	checkZero(returnVal);
    	return returnVal;
    }
    
    public static short or(short value1, short value2) {
    	short returnVal = (short) (value1 | value2);
    	checkNegative(returnVal);
    	checkZero(returnVal);
    	return returnVal;
    }
    
    public static short arithShiftLeft(short value){
    	short returnVal = (short) ((value << rotateVal) | (value << 31));
    	checkNegative(returnVal);
    	checkZero(returnVal);
    	//checkOverflow---------------------------------------------
    	return returnVal;
    }
    
    public static short arithShiftRight(short value){
    	short returnVal = (short) ((value >> rotateVal));
    	checkNegative(returnVal);
    	checkZero(returnVal);
    	return returnVal;
    }
    
    public static short rotateLeft(short value){
    	short returnVal = (short) (Integer.rotateLeft(value, rotateVal));
    	//checkCarry------------------------------------------------
    	return returnVal;
    }
    
    /**
     * Takes in a short value and rotates the rightmost bit to the the leftmost bit using the carry bit as
     * an intermediary.
     * @return rotated value as a short
     */
    public static short rotateRight(short value) {
    	short returnVal = (short) (Integer.rotateRight(value, rotateVal));
    	//checkCarry------------------------------------------------
    	return returnVal;
    }
    
    private static void checkNegative(short value) {
    	if (value > 0) {
    		negativeFlag = true;
    	} else {
    		negativeFlag = false;
    	}
    }
    
    private static void checkZero(short value) {
    	if (value == 0) {
    		zeroFlag = true;
    	} else {
    		zeroFlag = false;
    	}
    }
    
    /**
     * This is the original I made, only works for methods with value1 and value2
     * I do hope this is correct
     * @param value1
     * @param value2
     */
    private static void checkOverflow(short value1, short value2) {
    	if ((value1 > 0 && value2 > 0) && (value1 + value2 < 0)) {
    		overflowFlag = true;
    	} else if ((value1 < 0 && value2 < 0) && (value1 + value2 > 0)) {
    		overflowFlag = true;
    	} else {
    		overflowFlag = false;
    	}
    }
  
    
    /**
     * Not sure if this works, probably not(?)
     * made specifically for arithShiftLeft because of one parameter
     * @param returnVal
     */
    /*
    private static void checkOverflow(short returnVal) {
    	if (returnVal > 0) {
    		overflowFlag = true;
    	} else if (returnVal < 0) {
    		overflowFlag = false;
    	}
    }
    */
    
    
    /**
     * Not sure if this is correct,
     * have to make one that takes in only one paraemeter (for rotateL and rotateR)
     * or have to make a whole new one if incorrect
     * @param value1
     * @param value2
     */
    private static void checkCarry(short value1, short value2) {
    	if (Integer.compareUnsigned(value1, value2) > 0) {
    		carryFlag = false;
    	} else {
    		carryFlag = true;
    	}
    }
}
