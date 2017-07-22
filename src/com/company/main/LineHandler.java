package com.company.main;

import com.company.main.check.DirHandler;
import com.company.main.check.KeyWordMatch;
import com.company.main.content.Content;
import com.company.main.content.ContentType;
import com.company.main.segmentTable.Segment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.company.main.LinesManager.segmentTable;
import static com.company.main.check.DirHandler.idIsAllowed;
import static com.company.main.check.KeyWords.*;

/**
 * Created by alex6 on 18.05.2017.
 *
 * Checks what keywords the line contains and calls functions
 * from <code>DirHandler</code> class to set the <code>Content</code>
 * table for the line.
 */
public class LineHandler {

    public static String curSegment = null;
    private static String str;
    public static Line curLine;
    public static int commonOffset = 0;

    public static void incCommonOffset(int val){
        commonOffset = commonOffset + val;
    }


    public static void handle(Line line){
        curLine = line;
        str = line.getString();
        curLine.setOffset(commonOffset);

        if(empty())
            return;
        if (line.isEmpty()) //line is empty
            return;
        if (checkSEGMENTAndENDS(line))
            return;
        if(checkDefine())
            return;
        if (sentence())
            return;
        line.assignError();
    }
    private static boolean sentence() {
        if (curSegment == null)
            return false;
        curLine.content = new Content();
        if(label())
            return true;
        if(expression(str))
            return true;
        if(labelExpression())
            return true;
        return false;
    }

    private static boolean label() {
        Pattern usrIdPattern = Pattern.compile("^\\s*("+USR_ID+")\\s*:\\s*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = usrIdPattern.matcher(str);

        if (matcher.find())
            return DirHandler.labelHandler(curLine, matcher.group(1));
        else
            return false;
    }
    private static boolean labelExpression() {
        Pattern usrIdPattern = Pattern.compile("^\\s*("+USR_ID+")\\s*:(.*)$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = usrIdPattern.matcher(str);

        if (matcher.find()){
            boolean expressionRes = expression(matcher.group(2));
            boolean labelRes = DirHandler.labelHandler(curLine, matcher.group(1));
            return expressionRes && labelRes;
        }
        else
            return false;
    }

    private static boolean expression(String possibleExpr) {
        String REGISTERS = REG_8 +"|"+ REG_32;
        String POINTERS = "(?:"+PTR_PRED+")\\s+"+PTR;
        String SEG_CHANGE = "(?:"+REG_SEG+")\\s*:";
        String DIRECT_ADDR = "(?:"+POINTERS+")?\\s*(?:"+SEG_CHANGE+")?\\s*"+USR_ID;
        String BASE_INDEX_MULT = "(?:"+POINTERS+")?\\s*(?:"+SEG_CHANGE+")?\\s*"+"\\[\\s*(?:"+REG_32+")\\s*\\+\\s*(?:"+REG_32+")\\s*\\*\\s*[248]"+"\\s*\\]";
        String OPERAND = "(?:"+REGISTERS+")|(?:"+DIRECT_ADDR+")|(?:"+BASE_INDEX_MULT+")|(?:"+CONSTANT+")";

        String EXPRESSION = "(?:^\\s*("+MNEM +")\\s*$)|" +  // group 1
                "(?:^\\s*(" + MNEM + ")\\s+(" + OPERAND + ")\\s*$)|"+// groups 2, 3
                "(?:^\\s*(" + MNEM + ")\\s+(" + OPERAND + ")\\s*,\\s*(" + OPERAND + ")\\s*$)";// groups 4, 5, 6

        Pattern usrIdPattern = Pattern.compile(""+EXPRESSION+"",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = usrIdPattern.matcher(possibleExpr);
        if(matcher.find()){
            if(matcher.group(1) != null){
                DirHandler.mnemHandler(curLine,matcher.group(1));
                return true;
            }
            if((matcher.group(2) != null)&&(matcher.group(3) != null)){
                DirHandler.mnemOperHandler(curLine,matcher.group(2),matcher.group(3));
                return true;
            }
            if((matcher.group(4) != null)&&(matcher.group(5) != null)&&(matcher.group(6) != null)){
                DirHandler.mnemOperOperHandler(curLine,matcher.group(4),matcher.group(5),matcher.group(6));
                return true;
            }
            return true;
        }
        else
            return false;
    }



    private static boolean labelMnem() {
        return false;
    }

    private static boolean labelMnemOper() {
        return false;
    }

    private static boolean labelMnemOperOper() {
        return false;
    }

    private static boolean checkDefine() {
        if(KeyWordMatch.isDefine(str)){
            DirHandler.defineHandler(curLine);
            return true;
        }
        else
            return false;
    }

    private static boolean empty() {
       if (KeyWordMatch.isEmpty(str)){
           curLine.setEmpty(true);
           return true;
       }
       else
           return false;
    }

    //<------------- check for wrong names (name of the segment is a keyword)
    private static boolean checkSEGMENTAndENDS(Line line) {
        String str = line.getString();
        //SEGMENT found
        String segmentName = KeyWordMatch.isSegmentStart(str);
        if ((segmentName != null)){
            if(curSegment == null){ //No current segment
                if (segmentTable.contains(segmentName)) {//segmentExists
                    line.assignError();
                    return true;
                }
                else{
                    curSegment = segmentName;
                    segmentTable.addSegment(segmentName); // <------- move to Dir Handler
                    line.content = new Content(ContentType.SEGMENT, null,null, null, null);
                }

            }
            else // SEGMENT found inside other segment
                line.assignError();

            return true;
        }

        //SEGMENT ENDS found
        segmentName = KeyWordMatch.isSegmentEnd(str);
        if ((segmentName != null)){
            if(curSegment == null){
                line.assignError();
                return true;
            }
            else if (curSegment.equals(segmentName)){
                if (curSegment != null){
                    Segment segment = segmentTable.getSegmentByName(curSegment);
                    segment.setLength(commonOffset);
                }
                curSegment = null;
                line.content = new Content(ContentType.ENDS, null,null, null, null);
                return true;
            }
            else {
                line.assignError();
                return true;
            }
        }

        return false;
    }

}
