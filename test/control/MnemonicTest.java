package control;

import model.AddressingMode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static control.Mnemonic.*;

class MnemonicTest {

    private final Mnemonic[] unary = new Mnemonic[] {STOP, MOVSPA, MOVFLGA, NOTA, NOTI, NEGA, NEGI, ASLA, ASLI, ASRA,
            ASRI, ROLA, ROLI, RORA, RORI, RET0, RET1, RET2, RET3, RET4, RET5, RET6, RET7, END};

    private final Mnemonic[] nonUnary = new Mnemonic[] {BR, BRLE, BRLT, BREQ, BRNE, BRGE, BRGT, BRV, BRC, CALL, DECI,
            DECO, STRO, CHARI, CHARO, ADDSP, SUBSP, ADDA, ADDI, SUBA, SUBI, ANDA, ANDI, ORA, ORI, CPA, CPI, LDA,
            LDI, LDBYTEA, LDBYTEI, STA, STI, STBYTEA, STBYTEI, ASCII, BLOCK, WORD, BYTE};

    @Test
    void testGetMachineCodeWithoutAddress() {
        String[] expectedCodeValues = new String[] {"00", "02", "03", "18", "19", "1A", "1B", "1C", "1D", "1E", "1F",
                "20", "21", "22", "23", "58", "59", "5A", "5B", "5C", "5D", "5E", "5F", ""};
        for (int i = 0; i < unary.length; i++) {
            assertEquals(expectedCodeValues[i], unary[i].getMachineCode());
        }
        for (int i = 0; i < nonUnary.length; i++) {
            int finalI = i;
            assertThrows(IllegalArgumentException.class, () -> nonUnary[finalI].getMachineCode());
        }
    }

    @Test
    void testGetMachineCodeWithAddressBRAndCALL() {
        String[] expectedCodeValues = new String[] {"04", "05", "06", "07", "08", "09", "0A", "0B", "0C", "0D", "0E",
                "0F", "10", "11", "12", "13", "14", "15", "16", "17"};
        for (int i = 0; i < expectedCodeValues.length; i++) {
            assertEquals(expectedCodeValues[i], nonUnary[i / 2].getMachineCode(AddressingMode.I));
            i++;
            assertEquals(expectedCodeValues[i], nonUnary[i / 2].getMachineCode(AddressingMode.X));
            int finalI = i;
            assertThrows(IllegalArgumentException.class, () -> nonUnary[finalI / 2].getMachineCode(AddressingMode.SF));
        }
    }

    @Test
    void testGetMachineCodeWithNonBRAndCALL() {
        String[] expectedValues = {"32", "3A", "42", "4A", "52", "62", "6A", "72", "7A", "82", "8A", "92", "9A", "A2",
                "AA", "B2", "BA", "C2", "CA", "D2", "DA", "E2", "EA", "F2", "FA", "", "", "", ""};
        for (int i = 0; i < expectedValues.length; i++) {
            Mnemonic mnemonic = nonUnary[i + 10];
            assertEquals(expectedValues[i], mnemonic.getMachineCode(AddressingMode.N));
            if (mnemonic == DECI || mnemonic == STRO || mnemonic == CHARI || mnemonic == STA ||
                    mnemonic == STI || mnemonic == STBYTEA || mnemonic == STBYTEI) {
                int finalI = i;
                assertThrows(IllegalArgumentException.class, () -> mnemonic.getMachineCode(AddressingMode.I));
            }
        }
    }

    @Test
    void testToString() {
        assertEquals(".ASCII", Mnemonic.ASCII.toString());
        assertEquals("RET6", Mnemonic.RET6.toString());
    }

    @Test
    void testValueOf() {
        String[] testNames = new String[] {"STOP", "MOVSPA", "MOVFLGA", "BR", "BRLE", "BRLT", "BREQ", "BRNE", "BRGE",
                "BRGT", "BRV", "BRC", "CALL", "NOTA", "NOTI", "NEGA", "NEGI", "ASLA", "ASLI", "ASRA", "ASRI", "ROLA",
                "ROLI", "RORA", "RORI", "DECI", "DECO", "STRO", "CHARI", "CHARO", "RET0", "RET1", "RET2", "RET3",
                "RET4", "RET5", "RET6", "RET7", "ADDSP", "SUBSP", "ADDA", "ADDI", "SUBA", "SUBI", "ANDA", "ANDI",
                "ORA", "ORI", "CPA", "CPI", "LDA", "LDI", "LDBYTEA", "LDBYTEI", "STA", "STI", "STBYTEA", "STBYTEI"};
        Mnemonic[] values = Mnemonic.values();
        for (int i = 0; i < testNames.length; i++) {
            assertEquals(values[i], Mnemonic.valueOf(testNames[i]));
        }
    }
}