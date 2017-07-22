package com.company.main.lstGen;

import com.company.main.Line;
import com.company.main.LinesManager;
import com.company.main.content.LexemeOperand;

import static com.company.main.LineHandler.incCommonOffset;
import static com.company.main.LinesManager.usrIdTable;
import static com.company.main.lstGen.RegTable.*;

/**
 * Created by alex6 on 10.06.2017.
 */
public class OpCodeGenerator {
    public static void lahf(Line line){
        line.setListing("9F");
        incCommonOffset(1);
    }
    public static void imul_reg8(Line line, String reg8){
        line.setListing("F6 "+modRM(3, "ebp", reg8));
        incCommonOffset(2);
    }

    public static void imul_reg32(Line line, String reg32){
        line.setListing("F7 "+modRM(3, "ebp", reg32));
        incCommonOffset(2);
    }
    public static void mov_reg8(Line line, String reg8, String reg8_32){
        line.setListing("8A "+modRM(3, reg8, reg8_32));
        incCommonOffset(2);
    }
    public static void mov_reg32(Line line, String reg8, String reg8_32){
        line.setListing("8B "+modRM(3, reg8, reg8_32));
        incCommonOffset(2);
    }
    public static void and_reg8_imm(Line line, String reg8, String imm){
        if(reg8.toUpperCase().equals("AL")){
            line.setListing("24 "+ lstHexRepresent(Long.parseLong(imm), Type.BYTE));
            incCommonOffset(2);
        }
        else {
            line.setListing("80 "+modRM(3, "ESP", reg8) + " "+ lstHexRepresent(Long.parseLong(imm), Type.BYTE));
            incCommonOffset(3);
        }
    }
    public static void and_reg32_imm(Line line, String reg32, String imm){
        if(imm8_32isOk(imm)){
            line.setListing("83 "+modRM(3, "ESP", reg32) + " "+ lstHexRepresent(Long.parseLong(imm), Type.BYTE));
            incCommonOffset(3);
        }
        else{
            if(reg32.toUpperCase().equals("EAX")){
                String hex =  Integer.toHexString((Integer.parseInt(imm)));
                hex = String.format("%"+8+"s", hex).replace(' ', '0');
                line.setListing("25 "+ hex);//+ lstHexRepresent(Long.parseLong(imm), Type.DOUBLE));
                incCommonOffset(5);
                return;
            }
                line.setListing("81 "+modRM(3, "ESP", reg32) + " "+ lstHexRepresent(Long.parseLong(imm), Type.DOUBLE));
                incCommonOffset(6);
        }
    }
    public static void sub_reg8_dirAddr(Line line, String reg8, String dirAddr){
        String segChange = line.content.getOp2().getSegChangeValue();

        String hex =  Integer.toHexString(usrIdTable.getValueByName(dirAddr)).toUpperCase();
        hex = String.format("%"+8+"s", hex).replace(' ', '0');
        line.setListing("2A "+modRM(0, reg8, "EBP") + " "+
                hex +" R");

        //segment change
        if((segChange != null) && !segChange.toUpperCase().equals("DS")){
            String segRegNum = getSegRegNum(segChange);
            line.listing = segRegNum+": " + line.getListing();
        }
    }
    public static void sub_reg32_dirAddr(Line line, String reg32, String dirAddr){
        String segChange = line.content.getOp2().getSegChangeValue();

        String hex =  Integer.toHexString(usrIdTable.getValueByName(dirAddr)).toUpperCase();
        hex = String.format("%"+8+"s", hex).replace(' ', '0');
        line.setListing("2B "+modRM(0, reg32, "EBP") + " "+
                hex +" R");

        //segment change
        if((segChange != null) && !segChange.toUpperCase().equals("DS")){
            String segRegNum = getSegRegNum(segChange);
            line.listing = segRegNum+": " + line.getListing();
        }
    }
    public static void sub_reg32_baseIndexMult(Line line){
        String hex =  baseIndexMultSegCh(line,line.content.getOp1(), line.content.getOp2(), "2B");
        incCommonOffset(1);
        line.setListing(hex);
        //line.setListing("2B "+hex);
    }
    public static void sub_reg8_baseIndexMult(Line line){
        String hex =  baseIndexMultSegCh(line,line.content.getOp1(), line.content.getOp2(), "2A");
        incCommonOffset(1);
        line.setListing(hex);
       // line.setListing("2A "+hex);
    }
    public static void cmp_baseIndexMult_reg32(Line line){
        String hex =  baseIndexMultSegCh(line,line.content.getOp2(), line.content.getOp1(), "39");
        incCommonOffset(1);
        line.setListing(hex);
        //line.setListing("2B "+hex);
    }
    public static void cmp_baseIndexMult_reg8(Line line){
        String hex =  baseIndexMultSegCh(line,line.content.getOp2(), line.content.getOp1(), "38");
        incCommonOffset(1);
        line.setListing(hex);
        // line.setListing("2A "+hex);
    }
    public static void cmp_dirAddr_reg8(Line line, String reg8, String dirAddr){
        String segChange = line.content.getOp1().getSegChangeValue();

        String hex =  Integer.toHexString(usrIdTable.getValueByName(dirAddr)).toUpperCase();
        hex = String.format("%"+8+"s", hex).replace(' ', '0');
        line.setListing("38 "+modRM(0, reg8, "EBP") + " "+
                hex +" R");

        //segment change
        if((segChange != null) && !segChange.toUpperCase().equals("DS")){
            String segRegNum = getSegRegNum(segChange);
            line.listing = segRegNum+": " + line.getListing();
        }
    }
    public static void cmp_dirAddr_reg32(Line line, String reg32, String dirAddr){
        String segChange = line.content.getOp1().getSegChangeValue();

        String hex =  Integer.toHexString(usrIdTable.getValueByName(dirAddr)).toUpperCase();
        hex = String.format("%"+8+"s", hex).replace(' ', '0');
        line.setListing("39 "+modRM(0, reg32, "EBP") + " "+
                hex +" R");

        //segment change
        if((segChange != null) && !segChange.toUpperCase().equals("DS")){
            String segRegNum = getSegRegNum(segChange);
            line.listing = segRegNum+": " + line.getListing();
        }
    }
    public static void adc_mem8_imm(Line line, String mem8, String imm){
       String hex = String.format("%"+8+"s", Integer.toHexString(usrIdTable.getValueByName(mem8))).replace(' ', '0');
            line.setListing("80 "+modRM(0, "EDX", "EBP")+" "+ hex+
                    " R" + " "+ lstHexRepresent(Long.parseLong(imm), Type.BYTE));
           // incCommonOffset(7);
        String segChange = line.content.getOp1().getSegChangeValue();
        //segment change
        if((segChange != null) && !segChange.toUpperCase().equals("DS")){
            String segRegNum = getSegRegNum(segChange);
            line.listing = segRegNum+": " + line.getListing();
        }
    }
    public static void adc_mem32_imm(Line line, String mem32, String imm){
        if(imm8_32isOk(imm)){
            String hex = String.format("%"+8+"s", Integer.toHexString(usrIdTable.getValueByName(mem32))).replace(' ', '0');
            line.setListing("83 "+modRM(0, "EDX", "EBP")+" " + hex+  " R "+ lstHexRepresent(Long.parseLong(imm), Type.BYTE));
        }
        else{
            String hex = String.format("%"+8+"s", Integer.toHexString(usrIdTable.getValueByName(mem32))).replace(' ', '0');
            line.setListing("81 "+modRM(0, "EDX", "EBP")+" " + hex+  " R "+ lstHexRepresent(Long.parseLong(imm), Type.DOUBLE));
        }
        String segChange = line.content.getOp1().getSegChangeValue();
        //segment change
        if((segChange != null) && !segChange.toUpperCase().equals("DS")){
            String segRegNum = getSegRegNum(segChange);
            line.listing = segRegNum+": " + line.getListing();
        }
    }
    public static String baseIndexMultSegCh(Line line,LexemeOperand op1, LexemeOperand op2, String opcCode){
        String result = null;
        String reg = null;
            if (op1.isRegister8())
                reg = op1.getRegister8Value();
            else
                reg = op1.getRegister32Value();

        String modRm;
        String sib;
        String offset;
            if (op2.getReg1Value().toUpperCase().equals("EBP")){
                modRm = modRM(1, reg, "ESP");
                sib = generateSib(op2.getReg1Value(),op2.getReg2Value(),op2.getMultiplierValue());
                offset = "00";
                result = opcCode + " " +modRm+sib+" "+offset;
                incCommonOffset(3);
            }
            else{
                modRm = modRM(0, reg, "ESP");
                sib = generateSib(op2.getReg1Value(),op2.getReg2Value(),op2.getMultiplierValue());
                offset = "";
                result = opcCode + " " +modRm+sib+" "+offset;
                incCommonOffset(2);
            }

        if(op2.isSegChange() && !(op2.getSegChangeValue().equals(defaultSegmentReg(op2.getReg1Value())))){
            result = getSegRegNum(op2.getSegChangeValue()) +
                    ": " + result;
            incCommonOffset(1);
        }
               return result;
    }
    private static String defaultSegmentReg(String reg1) {
        if(reg1.toUpperCase().equals("EBP"))
            return "SS";
        else
            return "DS";
    }
    private static String generateSib(String base, String index, String multiplier) {
        int multDecimal;
        String ssBinary = null;

        if (multiplier == null){
            ssBinary = "00";
        }
        else {
            multDecimal = Integer.parseInt(multiplier);
            switch (multDecimal){
                case 2: ssBinary = "01";
                break;
                case 4: ssBinary = "10";
                break;
                case 8: ssBinary = "11";
                break;
            }
        }

        String indexBinary =  Integer.toBinaryString(number(index));
        String baseBinary =  Integer.toBinaryString(number(base));

        ssBinary = String.format("%"+2+"s", ssBinary).replace(' ', '0');
        indexBinary = String.format("%"+3+"s", indexBinary).replace(' ', '0');
        baseBinary = String.format("%"+3+"s", baseBinary).replace(' ', '0');

        String binary = ssBinary+indexBinary+baseBinary;
        String hex = Integer.toHexString(Integer.parseInt(binary, 2)).toUpperCase();
        return String.format("%"+2+"s", hex).replace(' ', '0');
    }

    private static String getSegRegNum(String segChange) {
        if (segChange.toUpperCase().equals("CS"))
            return "2E";
        if (segChange.toUpperCase().equals("DS"))
            return "3E";
        if (segChange.toUpperCase().equals("ES"))
            return "26";
        if (segChange.toUpperCase().equals("SS"))
            return "36";
        return "";
    }

    private static String constLstFormat() {
        return null;
    }

    public static void unsuportedInstr(Line line){
        line.assignError();
    }

    public static String lstHexRepresent(Long decimal, Type type){
        Long additionalDecimal;
        Long maxValue = 0L;
        switch (type){
            case BYTE: maxValue = 256L;
                break;
            case WORD: maxValue = 65536L;
                break;
            case DOUBLE: maxValue = 4294967296L;
                break;
        }
        if (decimal < 0)
            additionalDecimal = maxValue - Math.abs(decimal);
        else
            additionalDecimal = decimal;
        String result = Long.toHexString(additionalDecimal).toUpperCase();

        int numOfCharacters = 8;
        switch (type){
            case BYTE: numOfCharacters = 2;
                break;
            case WORD: numOfCharacters = 4;
                break;
            case DOUBLE: numOfCharacters = 8;
        }
        result = String.format("%"+numOfCharacters+"s", result).replace(' ', '0');
/*
        if (type == Type.DOUBLE){ //Special format for double
            char reversed[] = new char[8];
            for (int i = 0; i < 4; i++){
                reversed[numOfCharacters - 2*i - 2] =  result.charAt(2*i);
                reversed[numOfCharacters - 2*i - 1] =  result.charAt(2*i + 1);
            }
            result = String.valueOf(reversed);
        }
        */
        return  result;
    }

    private static String modRM(int mod, String reg, String rM) {
        String modBinary = Integer.toBinaryString(mod);
        String regBinary =  Integer.toBinaryString(number(reg));
        String rMBinary =  Integer.toBinaryString(number(rM));

        modBinary = String.format("%"+2+"s", modBinary).replace(' ', '0');
        regBinary = String.format("%"+3+"s", regBinary).replace(' ', '0');
        rMBinary = String.format("%"+3+"s", rMBinary).replace(' ', '0');

        String binary = modBinary+regBinary+rMBinary;
        String hex = Integer.toHexString(Integer.parseInt(binary, 2)).toUpperCase();
        return String.format("%"+2+"s", hex).replace(' ', '0');
    }
    public static boolean shortJNG(Line line, Long offset) {
        line.setListing("7E "+ lstHexRepresent(offset, Type.BYTE));
        return true;
    }
    public static boolean nearJNG(Line line, int addr) {
        line.setListing("0F 8E"+ lstHexRepresent(Long.valueOf(addr), Type.DOUBLE));
        return true;
    }
    public static boolean shortJMP(Line line, Long offset) {
        line.setListing("EB "+ lstHexRepresent(offset, Type.BYTE));
        return true;
    }
    public static boolean nearJMP(Line line, int addr) {
        line.setListing("E9 "+ lstHexRepresent(Long.valueOf(addr), Type.DOUBLE) + " R");
        return true;
    }
    public static boolean near_DD_JMP(Line line, int addr) {
        line.setListing("FF 25 "+ lstHexRepresent(Long.valueOf(addr), Type.DOUBLE) + " R");
        return true;
    }
    public static boolean farJMP(Line line, int addr) {
        line.setListing("EA "+ lstHexRepresent(Long.valueOf(addr), Type.DOUBLE) + " ---- R");
        return true;
    }
    public static boolean imm8_32isOk(String imm) {
        Long decimal = Long.parseLong(imm);
        if((-128 <= decimal)&&(decimal <= 127)){
            return true;
        }
        else
            return false;
    }

}
