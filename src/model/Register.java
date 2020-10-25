package model;

/*
TCSS 360 Project #2
Group 8
RJ Alabado, Walter Kagel, Taehong Kim
 */

/**
 * Model of a simple pep/8 register. It holds two byte values and has getter and setter methods for both
 * byte and short.
 */
public class Register {

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
    public byte getByte(boolean isMostSig) {
        if (isMostSig) return mostSigByte;
        return leastSigByte;
    }

    /**
     * Returns the value stored in register as a short. Added for convenience.
     * @return value as short
     */
    public short getShort() {
        short value = (short) ((mostSigByte << 4) + leastSigByte);
        return value;
    }

    /**
     * Sets the specified byte of the register to the given value.
     * @param isMostSig sets most significant byte if true, else sets least significant byte
     * @param value byte value to be stored
     */
    public void setByte(boolean isMostSig, byte value) {
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
    public void setShort(short value) {
        mostSigByte = (byte) (value >>> 4);
        leastSigByte = (byte) (value & 0xF);
    }
}
