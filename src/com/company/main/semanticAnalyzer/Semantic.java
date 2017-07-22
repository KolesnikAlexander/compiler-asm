package com.company.main.semanticAnalyzer;

import com.company.main.Line;
import com.company.main.LinesManager;
import com.company.main.content.Content;
import com.company.main.content.ContentType;
import com.company.main.content.LexemeOperand;
import com.company.main.usrIdTable.UsrIdTable;

import static com.company.main.LinesManager.usrIdTable;

/**
 * Created by alex6 on 07.06.2017.
 */
public class Semantic {
    private static ContentType contentType;
    private static String assistField;
    private static String mnem;
    private static LexemeOperand op1;
    private static LexemeOperand op2;
    public static boolean analyze(Line line) {
        if (line.isEmpty() || line.isError())
            return false;


        contentType = line.content.getContentType();
        assistField = line.content.getAssistField();


        mnem = line.content.getMnem();

        op1 = line.content.getOp1();
        op2 = line.content.getOp2();

        //if ((op1 != null)&&(op2 != null)){ // if both operands are present
        if(contentType == ContentType.MNEM_OP_OP){
            if(isMemMem()){ //meomory-memory command
                line.assignError();
                return false;
            }

            if (!bitCapacityEqual()){ // bitCapacityEqual() is always true
                line.assignError();
                return false;
            }
            if(isFirstOpConst()){
                line.assignError();
                return false;
            }
        }
        /*
        if((op1!=null)&&(op1.isDirectAddress()))
            if(!usrIdTable.contains(op1.getDirectAdressValue())){
                line.assignError();
                return false;
            }
        if((op2!=null)&&(op2.isDirectAddress()))
            if(!usrIdTable.contains(op2.getDirectAdressValue())){
                line.assignError();
                return false;
            }
            */
        return true;
    }

    private static boolean isFirstOpConst() {
        return op1.isConstant() || op1.isTextConstant();
    }

    private static boolean isMemMem() {
        return ((op1.isDirectAddress()||op1.isBaseIndexMultiplier())&&
                (op2.isDirectAddress()||op2.isBaseIndexMultiplier()));
    }

    private static boolean bitCapacityEqual() {
        //change this function if opeands cannot be of different bit capacity
        return true;
    }
}