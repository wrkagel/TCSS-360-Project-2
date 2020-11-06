package model;

class PCRegister extends Register{

    void advance(int n) {
        super.setShort((short) (super.getShort() + n));
    }

    void setPC(short value) {
        super.setShort(value);
    }

}
