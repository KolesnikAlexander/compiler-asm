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

/**
 * Contains primary filters for preventing the lines that are
 * definitely wrong from the following processing.
 */
public class PrimaryFilters {
    private static ContentType contentType;
    private static String assistField;
    private static String mnem;
    private static LexemeOperand op1;
    private static LexemeOperand op2;

    public static boolean filter(Line line) {
        if (line.isEmpty() || line.isError())
            return false;

        contentType = line.content.getContentType();
        assistField = line.content.getAssistField();
        mnem = line.content.getMnem();
        op1 = line.content.getOp1();
        op2 = line.content.getOp2();

        if(contentType == ContentType.MNEM_OP_OP){
            if(isMemMem()){ //memory-memory command is forbidden
                line.assignError();
                return false;
            }
            if (!bitCapacityEqual()){ // bitCapacityEqual() is always true
                line.assignError();
                return false;
            }
            if(isFirstOpConst()){ //first operand cannot be a constant
                line.assignError();
                return false;
            }
        }
        return true;
    }

    /**
     * @return true if the first operand is a constant
     */
    private static boolean isFirstOpConst() {
        return op1.isConstant() || op1.isTextConstant();
    }

    /**
     * @return true if the current operation is memory-memory
     */
    private static boolean isMemMem() {
        return ((op1.isDirectAddress()||op1.isBaseIndexMultiplier())&&
                (op2.isDirectAddress()||op2.isBaseIndexMultiplier()));
    }
    private static boolean bitCapacityEqual() {
        //change this function if operands cannot be of the different bit capacity
        return true;
    }
}