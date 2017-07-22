package com.company.main.lstGen;


import com.company.main.Line;
import com.company.main.LinesManager;
import com.company.main.check.KeyWordMatch;
import com.company.main.content.ContentType;
import com.company.main.content.LexemeOperand;
import com.company.main.usrIdTable.Id;

import static com.company.main.LinesManager.usrIdTable;
import static com.company.main.lstGen.FirstPathProcessor.imm32isOk;
import static com.company.main.lstGen.FirstPathProcessor.imm8isOk;
import static com.company.main.lstGen.OpCodeGenerator.*;

public class SecondPathProcessor {
    private static ContentType contentType;
    private static String assistField;
    private static String mnem;
    private static LexemeOperand op1;
    private static LexemeOperand op2;

    public static void generateOpcode(Line line) {
        if(KeyWordMatch.isEnd(line.getString())){
            return;
        }

        if (line.isEmpty() || line.isError() || line.isDone())
            return;

        contentType = line.content.getContentType();
        assistField = line.content.getAssistField();
        mnem = line.content.getMnem();
        op1 = line.content.getOp1();
        op2 = line.content.getOp2();

        if (contentType == ContentType.DD_LABEL) {
            String value = assistField;
            if (usrIdTable.contains(value)){
                Id id = usrIdTable.getIdByName(value);
                Long offset = Long.valueOf(id.getValue());
                String lst = lstHexRepresent(offset, Type.DOUBLE);
                line.setListing(lst + " R");
                return;
            }
            unsuportedInstr(line);
            return;
        }
        if (op1.isDirectAddress() && !LinesManager.usrIdTable.contains(op1.getDirectAdressValue())) {
            line.assignError();
            return;
        }
        if (op2!=null && op2.isDirectAddress() && !LinesManager.usrIdTable.contains(op2.getDirectAdressValue())) {
            line.assignError();
            return;
        }
        if (contentType == ContentType.MNEM_OP) {
            if (mnem.toUpperCase().equals("JMP")) {
                if (op1.isFarPtr()){
                    if (usrIdTable.contains(op1.getDirectAdressValue())) {
                        Id id = usrIdTable.getIdByName(op1.getDirectAdressValue());
                        if (id.getType().equals("L NEAR")) {
                            farJMP(line, id.getValue());
                            return;
                        }
                    }
                }
                if (usrIdTable.contains(op1.getDirectAdressValue())) {
                    Id id = usrIdTable.getIdByName(op1.getDirectAdressValue());
                    if (id.getType().equals("L DWORD")) {
                       near_DD_JMP(line, id.getValue());
                       return;
                    }
                }
                if (usrIdTable.contains(op1.getDirectAdressValue())){
                    Id id = usrIdTable.getIdByName(op1.getDirectAdressValue());
                    if (id.getType().equals("L NEAR")){
                        nearJMP(line, id.getValue());
                        return;
                    }
                    else
                    {
                        unsuportedInstr(line);
                        return;
                    }
                }
                else {
                    unsuportedInstr(line);
                    return;
                }
            }
            if (mnem.toUpperCase().equals("JNG")) {
                if (usrIdTable.contains(op1.getDirectAdressValue())){
                    Id id = usrIdTable.getIdByName(op1.getDirectAdressValue());
                    if (id.getType().equals("L NEAR")){
                        nearJNG(line, id.getValue());
                        return;
                    }
                    else
                    {
                        unsuportedInstr(line);
                        return;
                    }
                }
                else {
                    unsuportedInstr(line);
                    return;
                }

            } else {
                unsuportedInstr(line);
            }
            line.assignDone();
            return;
        }
        if (contentType == ContentType.MNEM_OP_OP) {
            //SUB REG_MEM
            if (mnem.toUpperCase().equals("SUB")) {
                //DIRECT ADDRESS
                if (op1.isRegister8() && op2.isDirectAddress()) {
                    sub_reg8_dirAddr(line, op1.getRegister8Value(), op2.getDirectAdressValue());
                } else if (op1.isRegister32() && op2.isDirectAddress()) {
                    sub_reg32_dirAddr(line, op1.getRegister32Value(), op2.getDirectAdressValue());
                }
                //BASE INDEX MULTIPLIER DONE
                else {
                    unsuportedInstr(line);
                    return;
                }
                line.assignDone();
                return;
            }
            if (mnem.toUpperCase().equals("CMP")) {
                //DIRECT ADDRESS
                if (op2.isRegister8() && op1.isDirectAddress()) {
                    cmp_dirAddr_reg8(line, op2.getRegister8Value(), op1.getDirectAdressValue());
                } else if (op2.isRegister32() && op1.isDirectAddress()) {
                    cmp_dirAddr_reg32(line, op2.getRegister32Value(), op1.getDirectAdressValue());
                }
                //BASE INDEX MULTIPLIER DONE
                else {
                    unsuportedInstr(line);
                    return;
                }
                line.assignDone();
                return;
            }
            if (mnem.toUpperCase().equals("ADC")) {
                if ((isMem8(op1.getDirectAdressValue())) && op2.isConstant()) {
                    if (imm8isOk(op2.getConstantValue())) {
                        adc_mem8_imm(line, op1.getDirectAdressValue(), op2.getConstantValue());
                        return;
                    } else {
                        unsuportedInstr(line);
                        return;
                    }

                } else if ((isMem32(op1.getDirectAdressValue())) && op2.isConstant()) {
                    if (imm32isOk(op2.getConstantValue())) {
                        adc_mem32_imm(line, op1.getDirectAdressValue(),op2.getConstantValue());
                        return;
                    } else {
                        unsuportedInstr(line);
                        return;
                    }
                } else if ((isMem8(op1.getDirectAdressValue())) && op2.isTextConstant()) {
                    Long decimal = Long.parseLong(op2.getTextConstantValue(), 16);
                    if (imm8isOk(decimal.toString())) {
                        //line.setListing("DONE 8");
                        adc_mem8_imm(line, op1.getDirectAdressValue(),decimal.toString());
                    } else {
                        unsuportedInstr(line);
                        return;
                    }
                } else if ((isMem32(op1.getDirectAdressValue())) && op2.isTextConstant()) {
                    Long decimal = Long.parseLong(op2.getTextConstantValue(), 16);
                    if (imm32isOk(decimal.toString())) {
                        //line.setListing("DONE 32 2");
                        adc_mem32_imm(line, op1.getDirectAdressValue(),decimal.toString());
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
        }
    }

    public static boolean isMem8(String addr) {
        if (addr == null)
            return false;
        else if (LinesManager.usrIdTable.getIdByName(addr).getType().equals("L BYTE"))
            return true;
        else
            return false;
    }

    public static boolean isMem32(String addr) {
        if (addr == null)
            return false;
        else
            return true;
    }
}