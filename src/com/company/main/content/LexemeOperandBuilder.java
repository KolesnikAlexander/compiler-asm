package com.company.main.content;

import com.company.main.check.DirHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.company.main.check.KeyWordMatch.binConst;
import static com.company.main.check.KeyWordMatch.decConst;
import static com.company.main.check.KeyWordMatch.hexConst;
import static com.company.main.check.KeyWords.*;
import static com.company.main.check.KeyWords.REG_32;
import static com.company.main.check.KeyWords.REG_8;


public class LexemeOperandBuilder {

    public static LexemeOperand buildLexemeOperand(String operand) {
        LexemeOperand lexemeOperand = null;
        Pattern pattern;
        Matcher matcher;

        //REG8
        lexemeOperand = reg8(operand);
        if (reg8(operand) != null) return lexemeOperand;

        //REG32
        lexemeOperand = reg32(operand);
        if (lexemeOperand != null) return lexemeOperand;

        //DIRECT ADDRESS
        lexemeOperand = directAdders(operand);
        if (lexemeOperand != null) return lexemeOperand;

        //BASE_INDEX_MULTIPLIER
        lexemeOperand = baseIndexMult(operand);
        if (lexemeOperand != null) return lexemeOperand;

        //BASE_INDEX_MULTIPLIER
        lexemeOperand = constant(operand);
        if (lexemeOperand != null) return lexemeOperand;

        //ELSE
        return null;
    }

    private static LexemeOperand constant(String operand) {
        LexemeOperand lexemeOperand = new LexemeOperand();

        Pattern pattern = Pattern.compile("^(" + TEXT_CONST + ")$",
                Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(operand);
        if (matcher.find()) {
            lexemeOperand.setTextConstant(true);
            String value = matcher.group(1).substring(1, matcher.group(1).length()-1);
            lexemeOperand.setTextConstantValue(DirHandler.toHex(value));
            return lexemeOperand;
        }
        // ALL EXCEPT TEXT
        String number = operand;
        if (decConst(number)) {
            if ((number.charAt(number.length() - 1) == 'd') || (number.charAt(number.length() - 1) == 'D')) {
                number = number.substring(0, number.length()-1);
            }
            lexemeOperand.setConstant(true);
            lexemeOperand.setConstantValue(number);
            return lexemeOperand;
        }
        if (hexConst(number)){
            number = number.substring(0, number.length()-1);
            Long value = Long.parseLong(number, 16);
            String decValue = Long.toString(value);

            lexemeOperand.setConstant(true);
            lexemeOperand.setConstantValue(decValue);
            return lexemeOperand;
        }
        if (binConst(number)){
            number = number.substring(0, number.length()-1);
            Long value = Long.parseLong(number, 2);
            String decValue = Long.toString(value);

            lexemeOperand.setConstant(true);
            lexemeOperand.setConstantValue(decValue);
            return lexemeOperand;
        }



        else
            return null;
    }

    private static LexemeOperand reg8(String operand) {
        LexemeOperand lexemeOperand = new LexemeOperand();

        Pattern pattern = Pattern.compile("^(" + REG_8 + ")$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(operand);

        if (matcher.find()) {
            lexemeOperand.setRegister8(true);
            lexemeOperand.setRegister8Value(matcher.group(1));
            return lexemeOperand;
        } else
            return null;
    }

    private static LexemeOperand reg32(String operand) {
        LexemeOperand lexemeOperand = new LexemeOperand();

        Pattern pattern = Pattern.compile("^(" + REG_32 + ")$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(operand);

        if (matcher.find()) {
            lexemeOperand.setRegister32(true);
            lexemeOperand.setRegister32Value(matcher.group(1));
            return lexemeOperand;
        } else
            return null;
    }

    private static LexemeOperand directAdders(String operand) {
        LexemeOperand lexemeOperand = new LexemeOperand();

        String POINTERS = "(" + PTR_PRED + ")\\s+" + PTR;
        String SEG_CHANGE = "(" + REG_SEG + ")\\s*:";

        String DIRECT_ADDR = "(?:" + POINTERS + ")?\\s*(?:" + SEG_CHANGE + ")?\\s*(" + USR_ID + ")";
        Pattern pattern = Pattern.compile("^(?:" + DIRECT_ADDR + ")$",
                Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(operand);

        if (matcher.find()) {
            lexemeOperand.setDirectAddress(true);
            lexemeOperand.setDirectAdressValue(matcher.group(3));

            if (matcher.group(1)!=null){
                if (matcher.group(1).toUpperCase().equals("BYTE"))
                    lexemeOperand.setBytePtr(true);
                if (matcher.group(1).toUpperCase().equals("WORD"))
                    lexemeOperand.setWordPtr(true);
                if (matcher.group(1).toUpperCase().equals("DWORD"))
                    lexemeOperand.setDwordPtr(true);
                if (matcher.group(1).toUpperCase().equals("FAR"))
                    lexemeOperand.setFarPtr(true);
            }
            if (matcher.group(2)!=null){
                    lexemeOperand.setSegChange(true);
                    lexemeOperand.setSegChangeValue(matcher.group(2).toUpperCase());
            }
            return lexemeOperand;
        } else
            return null;
    }
    private static LexemeOperand baseIndexMult(String operand) {
        LexemeOperand lexemeOperand = new LexemeOperand();

        String POINTERS = "(" + PTR_PRED + ")\\s+" + PTR;
        String SEG_CHANGE = "(" + REG_SEG + ")\\s*:";

        String BASE_INDEX_MULT = "(?:" + POINTERS + ")?\\s*(?:" + SEG_CHANGE + ")?\\s*" + "\\[\\s*(" + REG_32 + ")\\s*\\+\\s*(" + REG_32 + ")\\s*\\*\\s*([248])" + "\\s*\\]";
        Pattern pattern = Pattern.compile("^(?:" + BASE_INDEX_MULT + ")$",
                Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(operand);

        if (matcher.find()) {
            lexemeOperand.setBaseIndexMultiplier(true);

            lexemeOperand.setReg1(true);
            lexemeOperand.setReg1Value(matcher.group(3));

            lexemeOperand.setReg2(true);
            lexemeOperand.setReg2Value(matcher.group(4));

            lexemeOperand.setMultiplier(true);
            lexemeOperand.setMultiplierValue(matcher.group(5));

            if (matcher.group(1)!=null){
                if (matcher.group(1).toUpperCase().equals("BYTE"))
                    lexemeOperand.setBytePtr(true);
                if (matcher.group(1).toUpperCase().equals("WORD"))
                    lexemeOperand.setWordPtr(true);
                if (matcher.group(1).toUpperCase().equals("DWORD"))
                    lexemeOperand.setDwordPtr(true);
                if (matcher.group(1).toUpperCase().equals("FAR"))
                    lexemeOperand.setFarPtr(true);
            }
            if (matcher.group(2)!=null){
                lexemeOperand.setSegChange(true);
                lexemeOperand.setSegChangeValue(matcher.group(2).toUpperCase());
            }
            return lexemeOperand;
        } else
            return null;
    }
}
