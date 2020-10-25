package model;

import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.*;

class RegisterTest {

    private Register reg = new Register();

    @org.junit.jupiter.api.Test
    void getByte() {
        reg.setByte(true, (byte) 0xFF);
        assertEquals((byte) 0xFF, reg.getByte(true));
    }

    @org.junit.jupiter.api.Test
    void getShort() {
    }

    @org.junit.jupiter.api.Test
    void setByte() {
    }

    @org.junit.jupiter.api.Test
    void setShort() {
    }
}