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
    private boolean negativeFlag = false;

    /**
     * Stores if the last operation resulted in a zero value.
     */
    private boolean zeroFlag = true;

    /**
     * Stores if the last operation caused on overflow.
     */
    private boolean overflowFlag = false;

    /**
     * Stores the carry bit for the last operation.
     */
    private boolean carryFlag = false;

    /**
     * Takes in two short values and returns their addition. Sets flags based on the result.
     * @return (short) (value1 + value2)
     */
    public short add(short value1, short value2) {
        short returnVal = (short) (value1 + value2);
        checkNegative(returnVal);
        checkZero(returnVal);
        checkOverflow(value1, value2, returnVal);
        checkCarry(value1, value2);
        return returnVal;
    }
    
    public short sub(short value1, short value2) {
    	short returnVal = (short) (value1 - value2);
    	checkNegative(returnVal);
    	checkZero(returnVal);
    	checkOverflow(value1, value2, returnVal);
    	checkCarry(value1, value2);
    	return returnVal;
    }

    public short and(short value1, short value2) {
    	short returnVal = (short) (value1 & value2);
    	checkNegative(returnVal);
    	checkZero(returnVal);
    	return returnVal;
    }
    
    public short or(short value1, short value2) {
    	short returnVal = (short) (value1 | value2);
    	checkNegative(returnVal);
    	checkZero(returnVal);
    	return returnVal;
    }
    
    public short arithShiftLeft(short value){
    	short returnVal = (short) ((value << rotateVal));
    	checkNegative(returnVal);
    	checkZero(returnVal);
    	checkArithLeftOverflow(value, returnVal);
    	return returnVal;
    }
    
    public short arithShiftRight(short value){
    	short returnVal = (short) ((value >> rotateVal));
    	checkNegative(returnVal);
    	checkZero(returnVal);
    	return returnVal;
    }
    
    public short rotateLeft(short value){
    	short returnVal = (short) (Integer.rotateLeft(value, rotateVal));
    	checkRotateLeftCarry(value);
    	return returnVal;
    }
    
    /**
     * Takes in a short value and rotates the rightmost bit to the the leftmost bit using the carry bit as
     * an intermediary.
     * @return rotated value as a short
     */
    public short rotateRight(short value) {
    	short returnVal = (short) (Integer.rotateRight(value, rotateVal));
    	checkRotateRightCarry(value);
    	return returnVal;
    }
    
    public short not(short value) {
    	short returnVal = (short) (~value);
    	return returnVal;
    }
    
    public short negation(short value) {
    	return 0;
    }
    
    //---------------------------------------------------------------------------------------------
    // Getters and Setters
    
    public boolean getNegativeFlag() {
    	return negativeFlag;
    }
    
    public boolean getZeroFlag() {
    	return zeroFlag;
    }
    
    public boolean getOverflowFlag() {
    	return overflowFlag;
    }
    
    public boolean getCarryFlag() {
    	return carryFlag;
    }
    
    private void setNegativeFlag(boolean val) {
    	negativeFlag = val;
    }
    
    private void setZeroFlag(boolean val) {
    	zeroFlag = val;
    }
    
    private void setOverflowFlag(boolean val) {
    	overflowFlag = val;
    }
    
    private void setCarryFlag(boolean val) {
    	carryFlag = val;
    }
    
    //---------------------------------------------------------------------------------------------
    // Check methods for setting NZVC values
    
    public void checkNegative(short value) {
    	if (value < 0) {
    		setNegativeFlag(true);
    	} else {
    		setNegativeFlag(false);
    	}
    }
    
    public void checkZero(short value) {
    	if (value == 0) {
    		setZeroFlag(true);
    	} else {
    		setZeroFlag(false);
    	}
    }
    
    /**
     * This is the original I made, only works for methods with value1 and value2
     * I do hope this is correct
     * @param value1
     * @param value2
     */
    private void checkOverflow(short value1, short value2, short returnVal) {
    	if ((value1 > 0 && value2 > 0) && (returnVal < 0)) {
    		setOverflowFlag(true);
    	} else if ((value1 < 0 && value2 < 0) && (returnVal > 0)) {
    		setOverflowFlag(true);
    	} else {
    		setOverflowFlag(false);
    	}
    }
  
    private void checkArithLeftOverflow(short value, short returnVal) {
    	if ((value > 0) && (returnVal < 0)) {
    		setOverflowFlag(true);
    	} else if ((value < 0) && (returnVal > 0)) {
    		setOverflowFlag(true);
    	} else {
    		setOverflowFlag(false);
    	}
    }
    
      private void checkCarry(short value1, short value2) {
    	if (Integer.compareUnsigned(value1, value2) == 0) {
    		setCarryFlag(true);
    	} else {
    		setCarryFlag(false);
    	}
    }
      
      private void checkRotateLeftCarry(short value) {
    	  if (Integer.toBinaryString(value).charAt(0) == '1') {
    		  setCarryFlag(true);
    	  } else {
    		  setCarryFlag(false);
    	  }
      }
      
      private void checkRotateRightCarry(short value) {
    	  if ((Integer.toBinaryString(value)).charAt(Integer.toBinaryString(value).length() - 1) == '1') {
    		  setCarryFlag(true);
    	  } else {
    		  setCarryFlag(false);
    	  }
      }
    
}
