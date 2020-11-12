package control;

import model.AddressingMode;

public enum Mnemonic {
    STOP, MOVSPA, MOVFLGA, BR, BRLE, BRLT, BREQ, BRNE, BRGE, BRGT, BRV, BRC, CALL, NOTA, NOTI, NEGA, NEGI, ASLA, ASLI,
    ASRA, ASRI, ROLA, ROLI, RORA, RORI, DECI, DECO, STRO, CHARI, CHARO, RET0, RET1, RET2, RET3, RET4, RET5, RET6, RET7,
    ADDSP, SUBSP, ADDA, ADDI, SUBA, SUBI, ANDA, ANDI, ORA, ORI, CPA, CPI, LDA, LDI, LDBYTEA, LDBYTEI, STA, STI,
    STBYTEA, STBYTEI, ASCII, BLOCK, WORD, BYTE, END;


    public String getMachineCode() {
        switch(this) {
            case STOP -> {return "00";}
            case MOVSPA -> {return "02";}
            case MOVFLGA -> {return "03";}
            case NOTA -> {
                return "18";
            }
            case NOTI -> {
                return "19";
            }
            case NEGA -> {
                return "1A";
            }
            case NEGI -> {
                return "1B";
            }
            case ASLA -> {
                return "1C";
            }
            case ASLI -> {
                return "1D";
            }
            case ASRA -> {
                return "1E";
            }
            case ASRI -> {
                return "1F";
            }
            case ROLA -> {
                return "20";
            }
            case ROLI -> {
                return "21";
            }
            case RORA -> {
                return "22";
            }
            case RORI -> {
                return "23";
            }
            case RET0 -> {
                return "58";
            }
            case RET1 -> {
                return "59";
            }
            case RET2 -> {
                return "5A";
            }
            case RET3 -> {
                return "5B";
            }
            case RET4 -> {
                return "5C";
            }
            case RET5 -> {
                return "5D";
            }
            case RET6 -> {
                return "5E";
            }
            case RET7 -> {
                return "5F";
            }
            case END, ASCII, BLOCK, WORD, BYTE -> {
                return "";
            }
            default -> throw new IllegalArgumentException("Mnemonic requires addressing mode");
        }
    }

    public String getMachineCode(AddressingMode mode) {
        switch(this) {
            case BR -> {
                if (mode == AddressingMode.I) return "04";
                if (mode == AddressingMode.X) return "05";
                else throw new IllegalArgumentException("Not a valid addressing mode for a branch instruction");
            }
            case BRLE -> {
                if (mode == AddressingMode.I) return "06";
                if (mode == AddressingMode.X) return "07";
                else throw new IllegalArgumentException("Not a valid addressing mode for a branch instruction");
            }
            case BRLT -> {
                if (mode == AddressingMode.I) return "08";
                if (mode == AddressingMode.X) return "09";
                else throw new IllegalArgumentException("Not a valid addressing mode for a branch instruction");
            }
            case BREQ -> {
                if (mode == AddressingMode.I) return "0A";
                if (mode == AddressingMode.X) return "0B";
                else throw new IllegalArgumentException("Not a valid addressing mode for a branch instruction");
            }
            case BRNE -> {
                if (mode == AddressingMode.I) return "0C";
                if (mode == AddressingMode.X) return "0D";
                else throw new IllegalArgumentException("Not a valid addressing mode for a branch instruction");
            }
            case BRGE -> {
                if (mode == AddressingMode.I) return "0E";
                if (mode == AddressingMode.X) return "0F";
                else throw new IllegalArgumentException("Not a valid addressing mode for a branch instruction");
            }
            case BRGT -> {
                if (mode == AddressingMode.I) return "10";
                if (mode == AddressingMode.X) return "11";
                else throw new IllegalArgumentException("Not a valid addressing mode for a branch instruction");
            }
            case BRV -> {
                if (mode == AddressingMode.I) return "12";
                if (mode == AddressingMode.X) return "13";
                else throw new IllegalArgumentException("Not a valid addressing mode for a branch instruction");
            }
            case BRC -> {
                if (mode == AddressingMode.I) return "14";
                if (mode == AddressingMode.X) return "15";
                else throw new IllegalArgumentException("Not a valid addressing mode for a branch instruction");
            }
            case CALL -> {
                if (mode == AddressingMode.I) return "16";
                if (mode == AddressingMode.X) return "17";
                else throw new IllegalArgumentException("Not a valid addressing mode for a call instruction");
            }
            case DECI -> {
                if (mode == AddressingMode.I) throw new IllegalArgumentException("Not a valid addressing mode for " +
                        "an input instruction");
                int i = mode.ordinal();
                return "3" + Integer.toHexString(i).toUpperCase();
            }
            case DECO -> {
                int i = mode.ordinal() + 8;
                return "3" + Integer.toHexString(i).toUpperCase();
            }
            case STRO -> {
                if (mode != AddressingMode.D && mode != AddressingMode.N && mode != AddressingMode.SF)
                    throw new IllegalArgumentException("Not a valid addressing mode for a string out instruction");
                int i = mode.ordinal();
                return "4" + Integer.toHexString(i).toUpperCase();
            }
            case CHARI -> {
                if (mode == AddressingMode.I) throw new IllegalArgumentException("Not a valid addressing mode for " +
                        "an input instruction");
                int i = mode.ordinal() + 8;
                return "4" + Integer.toHexString(i).toUpperCase();
            }
            case CHARO -> {
                int i = mode.ordinal();
                return "5" + Integer.toHexString(i).toUpperCase();
            }
            case ADDSP -> {
                int i = mode.ordinal();
                return "6" + Integer.toHexString(i).toUpperCase();
            }
            case SUBSP -> {
                int i = mode.ordinal() + 8;
                return "6" + Integer.toHexString(i).toUpperCase();
            }
            case ADDA -> {
                int i = mode.ordinal();
                return "7" + Integer.toHexString(i).toUpperCase();
            }
            case ADDI -> {
                int i = mode.ordinal() + 8;
                return "7" + Integer.toHexString(i).toUpperCase();
            }
            case SUBA -> {
                int i = mode.ordinal();
                return "8" + Integer.toHexString(i).toUpperCase();
            }
            case SUBI -> {
                int i = mode.ordinal() + 8;
                return "8" + Integer.toHexString(i).toUpperCase();
            }
            case ANDA -> {
                int i = mode.ordinal();
                return "9" + Integer.toHexString(i).toUpperCase();
            }
            case ANDI -> {
                int i = mode.ordinal() + 8;
                return "9" + Integer.toHexString(i).toUpperCase();
            }
            case ORA -> {
                int i = mode.ordinal();
                return "A" + Integer.toHexString(i).toUpperCase();
            }
            case ORI -> {
                int i = mode.ordinal() + 8;
                return "A" + Integer.toHexString(i).toUpperCase();
            }
            case CPA -> {
                int i = mode.ordinal();
                return "B" + Integer.toHexString(i).toUpperCase();
            }
            case CPI -> {
                int i = mode.ordinal() + 8;
                return "B" + Integer.toHexString(i).toUpperCase();
            }
            case LDA -> {
                int i = mode.ordinal();
                return "C" + Integer.toHexString(i).toUpperCase();
            }
            case LDI -> {
                int i = mode.ordinal() + 8;
                return "C" + Integer.toHexString(i).toUpperCase();
            }
            case LDBYTEA -> {
                int i = mode.ordinal();
                return "D" + Integer.toHexString(i).toUpperCase();
            }
            case LDBYTEI -> {
                int i = mode.ordinal() + 8;
                return "D" + Integer.toHexString(i).toUpperCase();
            }
            case STA -> {
                if (mode == AddressingMode.I) throw new IllegalArgumentException("Not a valid addressing mode for " +
                        "a store instruction");
                int i = mode.ordinal();
                return "E" + Integer.toHexString(i).toUpperCase();
            }
            case STI -> {
                if (mode == AddressingMode.I) throw new IllegalArgumentException("Not a valid addressing mode for " +
                        "an store instruction");
                int i = mode.ordinal() + 8;
                return "E" + Integer.toHexString(i).toUpperCase();
            }
            case STBYTEA -> {
                if (mode == AddressingMode.I) throw new IllegalArgumentException("Not a valid addressing mode for " +
                        "an store instruction");
                int i = mode.ordinal();
                return "F" + Integer.toHexString(i).toUpperCase();
            }
            case STBYTEI -> {
                if (mode == AddressingMode.I) throw new IllegalArgumentException("Not a valid addressing mode for " +
                        "an store instruction");
                int i = mode.ordinal() + 8;
                return "F" + Integer.toHexString(i).toUpperCase();
            }
            case ASCII, WORD, BLOCK, BYTE, END -> {
                return "";
            }
            default -> throw new IllegalArgumentException("Error making machine code");
        }
    }

    @Override
    public String toString() {
        return switch (this) {
            case ASCII -> ".ASCII";
            case BLOCK -> ".BLOCK";
            case WORD -> ".WORD";
            case BYTE -> ".BYTE";
            case END -> ".END";
            default -> this.name();
        };
    }
}
