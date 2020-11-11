package model;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

/**
 * Model of a simple pep/8 register. It holds two byte values and has getter and setter methods for both
 * byte and short.
 * @author Group 8, Lead: Walter Kagel
 * @version 10/26/2020
 */
class Register {

    /**
     * The byte containing the MSB.
     */
    private byte mostSigByte = 0;

    /**
     * The byte containing the LSB.
     */
    private byte leastSigByte = 0;

    /**
     * Returns the byte specified by isMostSig.
     * @param isMostSig returns most significant byte if true, else returns least significant byte.
     * @return byte value
     */
    byte getByte(boolean isMostSig) {
        if (isMostSig) return mostSigByte;
        return leastSigByte;
    }

    /**
     * Returns the value stored in register as a short. Added for convenience.
     * @return value as short
     */
    short getShort() {
        return (short) ((mostSigByte << 8) | (leastSigByte & 0xFF));
    }

    /**
     * Sets the specified byte of the register to the given value.
     * @param isMostSig sets most significant byte if true, else sets least significant byte
     * @param value byte value to be stored
     */
    void setByte(boolean isMostSig, byte value) {
        if (isMostSig) {
            this.mostSigByte = value;
        } else {
            this.leastSigByte = value;
        }
    }

    /**
     * Sets both bytes of the register using the short value given. Added for convenience.
     * @param value short value to set register to
     */
    void setShort(short value) {
        mostSigByte = (byte) ((value & 0xFF00) >>> 8);
        leastSigByte = (byte) (value & 0xFF);
    }
}
