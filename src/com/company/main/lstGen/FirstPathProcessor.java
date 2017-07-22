package com.company.main.lstGen;

import com.company.main.Line;
import com.company.main.LineHandler;
import com.company.main.check.KeyWordMatch;
import com.company.main.content.ContentType;
import com.company.main.content.LexemeOperand;
import com.company.main.usrIdTable.Id;

import static com.company.main.LineHandler.commonOffset;
import static com.company.main.LineHandler.incCommonOffset;
import static com.company.main.LinesManager.usrIdTable;
import static com.company.main.lstGen.OpCodeGenerator.*;


public class FirstPathProcessor {
    private static ContentType contentType;
    private static String assistField;
    private static String mnem;
    private static LexemeOperand op1;
    private static LexemeOperand op2;
    public static void generateOpcode(Line line) {
        if(KeyWordMatch.isEnd(line.getString()))
            return;
        if (line.isEmpty() || line.isError())
            return;

        contentType = line.content.getContentType();
        assistField = line.content.getAssistField();
        mnem = line.content.getMnem();
        op1 = line.content.getOp1();
        op2 = line.content.getOp2();

        if (contentType == ContentType.NOTHING) {
            //NO LISTING
            line.assignDone();
            return;
        }
        if (contentType == ContentType.SEGMENT) {
            //NO LISTING
            line.assignDone();
            return;
        }
        if (contentType == ContentType.ENDS) {
            //NO LISTING
            LineHandler.commonOffset = 0;
            line.assignDone();
            return;
        }
        if (contentType == ContentType.DB) {
            Long value = Long.parseLong(assistField);
            String lst = lstHexRepresent(value, Type.BYTE);
            line.setListing(lst);
            incCommonOffset(1);
            line.assignDone();
            return;
            // lst = String.format("%16s", Integer.toHexString(value)).replace(' ', '0');
        }
        if (contentType == ContentType.DW) {
            Long value = Long.parseLong(assistField);
            String lst = lstHexRepresent(value, Type.WORD);
            line.setListing(lst);
            incCommonOffset(2);
            line.assignDone();
            return;
        }
        if (contentType == ContentType.DD) {
            Long value = Long.parseLong(assistField);
            String lst = lstHexRepresent(value, Type.DOUBLE);
            line.setListing(lst);
            incCommonOffset(4);
            line.assignDone();
            return;
        }
        if (contentType == ContentType.DD_LABEL) {
            incCommonOffset(4);
            return;
        }
        if (contentType == ContentType.DB_TEXT) {
            //assistField contains HEX representation of text
            incCommonOffset(assistField.length() / 2);
            String lst = splitByBytes(assistField);
            line.setListing(lst);
            line.assignDone();
            return;
        }
        /**
         * -> Second path consideration: DD_LABEL
         */
        if (contentType == ContentType.MNEM) {
            /**
             *LAHF
             */
            if (mnem.toUpperCase().equals("LAHF")) {
                lahf(line);
                line.assignDone();
                return;
            } else {
                unsuportedInstr(line);
                return;
            }

        }
        /**
         * IMUL, JMP, JNG
         * -> Second path consideration: JMP, JNG
         */
        if (contentType == ContentType.MNEM_OP) {
            /**
             * IMUL REG8/REG32
             */
            if (mnem.toUpperCase().equals("IMUL")) {
                if (op1.isRegister8()) {
                    imul_reg8(line, op1.getRegister8Value());
                    line.assignDone();
                    return;
                } else if (op1.isRegister32()) {
                    imul_reg32(line, op1.getRegister32Value());
                    line.assignDone();
                    return;
                } else {
                    unsuportedInstr(line);
                    return;
                }
            }
            else if (mnem.toUpperCase().equals("JMP")) {
                if(op1.isSegChange() ||
                        op1.isBytePtr() || op1.isWordPtr() ||
                        op1.isDwordPtr())
                {
                    unsuportedInstr(line);
                    return;
                }
                if (op1.isFarPtr()){
                    incCommonOffset(7);
                    /**
                     * TO DO
                     */
                    return;
                }
                /**
                 * Short jump
                 */
                if (usrIdTable.contains(op1.getDirectAdressValue())){
                    Id id = usrIdTable.getIdByName(op1.getDirectAdressValue());
                    if (id.getType().equals("L NEAR")){
                        Long val = Long.valueOf(commonOffset - id.getValue() );
                        if (val <= 127L){
                            shortJMP(line, val);
                            incCommonOffset(2);
                            line.assignDone();
                        }
                        else
                            incCommonOffset(6);
                        return;
                    }
                    else if (id.getType().equals("L DWORD")){
                        incCommonOffset(6);
                        return;
                    }
                    else {
                        unsuportedInstr(line);
                        return;
                    }
                }
                else{
                    incCommonOffset(6);
                    return;
                }


            }
            else if (mnem.toUpperCase().equals("JNG")) {
                if(op1.isSegChange() ||
                        op1.isBytePtr() || op1.isWordPtr() ||
                        op1.isDwordPtr() || op1.isFarPtr())
                {
                 unsuportedInstr(line);
                 return;
                }
                if (usrIdTable.contains(op1.getDirectAdressValue())){
                    Id id = usrIdTable.getIdByName(op1.getDirectAdressValue());
                    if (id.getType().equals("L NEAR")){
                        Long val = Long.valueOf(commonOffset - id.getValue() );
                        if (val <= 127L){
                            shortJNG(line, val);
                            incCommonOffset(2);
                            line.assignDone();
                        }
                        else
                            incCommonOffset(6);
                        return;
                    }
                    else {
                        unsuportedInstr(line);
                        return;
                    }
                }
                else{
                    incCommonOffset(6);
                    return;
                }

            }
            unsuportedInstr(line);
            return;
        }
        /**
         * SUB, MOV, CMP, ADC, AND
         * -> Second path consideration: SUB, CMP, ADC
         */
        if (contentType == ContentType.MNEM_OP_OP) {

            if (mnem.toUpperCase().equals("MOV")) {
                if (op1.isRegister8() && op2.isRegister8()) {
                    mov_reg8(line, op1.getRegister8Value(), op2.getRegister8Value());
                } else if (op1.isRegister8() && op2.isRegister32()) {
                    mov_reg8(line, op1.getRegister8Value(), op2.getRegister32Value());
                } else if (op1.isRegister32() && op2.isRegister32()) {
                    mov_reg32(line, op1.getRegister32Value(), op2.getRegister32Value());
                } else if (op1.isRegister32() && op2.isRegister8()) {
                    mov_reg32(line, op1.getRegister32Value(), op2.getRegister8Value());
                } else {
                    unsuportedInstr(line);
                    return;
                }
                line.assignDone();
                return;
            }
            // incCommonOffset(assistField.length()/2);
            // String lst = splitByBytes(assistField);

            // Long value =  Long.parseLong(assistField);
            // String lst = lstHexRepresent(value, Type.BYTE);
            if (mnem.toUpperCase().equals("AND")) {
                if (op1.isRegister8() && op2.isConstant()) {
                    if (imm8isOk(op2.getConstantValue())) {
                        and_reg8_imm(line, op1.getRegister8Value(), op2.getConstantValue());
                    } else {
                        unsuportedInstr(line);
                        return;
                    }

                } else if (op1.isRegister32() && op2.isConstant()) {
                    if (imm32isOk(op2.getConstantValue())) {
                        and_reg32_imm(line, op1.getRegister32Value(), op2.getConstantValue());
                    } else {
                        unsuportedInstr(line);
                        return;
                    }
                } else if (op1.isRegister8() && op2.isTextConstant()) {
                    Long decimal = Long.parseLong(op2.getTextConstantValue(), 16);
                    if (imm8isOk(decimal.toString())) {
                        and_reg8_imm(line, op1.getRegister8Value(), decimal.toString());
                    } else {
                        unsuportedInstr(line);
                        return;
                    }
                } else if (op1.isRegister32() && op2.isTextConstant()) {
                    Long decimal = Long.parseLong(op2.getTextConstantValue(), 16);
                    if (imm32isOk(decimal.toString())) {
                        and_reg32_imm(line, op1.getRegister32Value(), decimal.toString());
                    } else {
                        unsuportedInstr(line);
                        return;
                    }
                } else {
                    unsuportedInstr(line);
                    return;
                }
                line.assignDone();
                return;
            }
            /** SUB REG_MEM*/
            if (mnem.toUpperCase().equals("SUB")) {
                    /** DIRECT ADDRESS*/
                    if ((op1.isRegister8() || op1.isRegister32()) && op2.isDirectAddress()) {
                        incCommonOffset(6);
                        if (op2.isSegChange() && !(op2.getSegChangeValue().toUpperCase().equals("DS"))){
                            incCommonOffset(1);
                        }
                        return; // done is false !
                    }
                    /** BASE INDEX MULTIPLIER*/
                    else if ((op1.isRegister8()||op1.isRegister32()) && op2.isBaseIndexMultiplier()) {
                        if(op2.getReg2Value().toUpperCase().equals("ESP")){
                            unsuportedInstr(line);
                            return;
                        }
                        if (op1.isRegister8())
                            sub_reg8_baseIndexMult(line);
                        else
                            sub_reg32_baseIndexMult(line);
                        line.assignDone();
                        return;
                    }  else {
                        unsuportedInstr(line);
                        return;
                    }
            }
            if (mnem.toUpperCase().equals("CMP")) {
                /** DIRECT ADDRESS*/
                if ((op2.isRegister8() || op2.isRegister32()) && op1.isDirectAddress()) {
                    incCommonOffset(6);
                    if (op1.isSegChange() && !(op1.getSegChangeValue().toUpperCase().equals("DS"))){
                        incCommonOffset(1);
                    }
                    return; // done is false !
                }
                /** BASE INDEX MULTIPLIER*/
                else if ((op2.isRegister8()||op2.isRegister32()) && op1.isBaseIndexMultiplier()) {
                    if(op1.getReg2Value().toUpperCase().equals("ESP")){
                        unsuportedInstr(line);
                        return;
                    }
                    if (op2.isRegister8())
                        cmp_baseIndexMult_reg8(line);
                    else
                        cmp_baseIndexMult_reg32(line);
                    line.assignDone();
                    return;
                }  else {
                    unsuportedInstr(line);
                    return;
                }
            }
            if (mnem.toUpperCase().equals("ADC")) {
                if (op1.isDirectAddress() && (op2.isConstant())) {
                    if (imm8isOk(op2.getConstantValue())) {
                        incCommonOffset(7);
                        //segment Change
                        if (op1.isSegChange()  && !op1.getSegChangeValue().toUpperCase().equals("DS"))
                            incCommonOffset(1);
                        //segment Change
                        return; // is not done!
                    } else if (imm32isOk(op2.getConstantValue())) {
                        incCommonOffset(10);
                        if (op1.isSegChange()  && !op1.getSegChangeValue().toUpperCase().equals("DS"))
                            incCommonOffset(1);
                        return; // is not done!
                    }else {
                        unsuportedInstr(line);
                        return;
                    }
                }
                else if (op1.isDirectAddress() && (op2.isTextConstant())) {
                    Long decimal = Long.parseLong(op2.getTextConstantValue(), 16);

                    if (imm8isOk(decimal.toString())) {
                        incCommonOffset(7);
                        if (op1.isSegChange()  && !op1.getSegChangeValue().toUpperCase().equals("DS"))
                            incCommonOffset(1);
                        return; // is not done!
                    } else if (imm32isOk(decimal.toString())) {
                        incCommonOffset(10);
                        if (op1.isSegChange()  && !op1.getSegChangeValue().toUpperCase().equals("DS"))
                            incCommonOffset(1);
                        return; // is not done!
                    }else {
                        unsuportedInstr(line);
                        return;
                    }
                }
               // line.assignDone();
                unsuportedInstr(line);
                return;
            }
        }
    }
//


    ///

    public static boolean imm32isOk(String constantValue) {
        Long decimal = Long.parseLong(constantValue);

        if((decimal <= -4294967296L)||( 4294967296L <= decimal)){
            return false;
        }
        else
            return true;
    }

    public static boolean imm8isOk(String constantValue) {
        Long decimal = Long.parseLong(constantValue);
        if((decimal <= -256)||(256 <= decimal)){
            return false;
        }
        else
            return true;
    }

    private static boolean immIsOk() {
        return false;
    }

    //CREATE SPLITTING BY BYTES FOR BEAUTIFUL OUTPUT!!!
    private static String splitByBytes(String noSpacesString) {
      /*  String str1 = new StringBuilder(noSpacesString).insert(noSpacesString.length()-2, ".").toString();
        StringBuilder str = new StringBuilder(noSpacesString);
        for (int i = 2; i < noSpacesString.length()/2 - 1; i+=2){

        }

    */
      return noSpacesString;
    }
    /**
     * Hex representation  in the listing
     * @return
     */
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

        if (type == Type.DOUBLE){ //Special format for double
            char reversed[] = new char[8];
            for (int i = 0; i < 4; i++){
                reversed[numOfCharacters - 2*i - 2] =  result.charAt(2*i);
                reversed[numOfCharacters - 2*i - 1] =  result.charAt(2*i + 1);
            }
            result = String.valueOf(reversed);
        }
        return  result;
    }


}
