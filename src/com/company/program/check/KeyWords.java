package com.company.program.check;


public class KeyWords {
    public static String MNEM = "LAHF|SUB|MOV|CMP|ADC|IMUL|AND|JNG|JMP";
    public static String REG_8 = "al|bl|cl|dl|ah|bh|ch|dh";
    public static String REG_32 = "eax|ebx|ecx|edx|ebp|esp|esi|edi";
    public static String DEF_DIR ="DB|DW|DD";
    public static String REG_SEG = "cs|ds|es|ss";
    public static String USR_ID ="[a-z_][a-z_0-9]{0,3}";
    public static String SEGMENT_DIR ="SEGMENT";
    public static String ENDS_DIR ="ENDS";
    public static String END ="END";
    public static String DEC_CONST = "\\-?\\d+d?";
    public static String BIN_CONST = "\\-?[01]+b?";
    public static String HEX_CONST = "\\-?[0-9][a-f0-9]*h";
    public static String TEXT_CONST = "\".*\"";
    public static String PTR_PRED = "BYTE|WORD|DWORD|FAR";
    public static String PTR = "PTR";

    public static String CONSTANT = DEC_CONST +"|"+BIN_CONST+"|"+HEX_CONST+"|"+TEXT_CONST;
    public static String CONST_XPT_TEXT = DEC_CONST +"|"+BIN_CONST+"|"+HEX_CONST;
    public static String RESERVED_WORDS = MNEM +"|"+ REG_8 +"|"+ REG_32 +"|"+ DEF_DIR+"|"+
            REG_SEG  +"|"+ SEGMENT_DIR +"|"+ENDS_DIR;

}
