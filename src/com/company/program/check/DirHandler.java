package com.company.program.check;

import com.company.program.Line;
import com.company.program.content.Content;
import com.company.program.content.ContentType;
import com.company.program.content.LexemeOperand;
import com.company.program.content.LexemeOperandBuilder;

import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.company.program.LineHandler.commonOffset;
import static com.company.program.LineHandler.curSegment;
import static com.company.program.LinesManager.*;
import static com.company.program.check.KeyWords.*;

/**
 * Builds a <code>Content</code> table for the line.
 */
public class DirHandler {

    public static void defineHandler(Line line){
        if(KeyWordMatch.isDB(line.getString())){
            if (KeyWordMatch.textConst(line.getString())){
                dBTextHandler(line);
                return;
            }
            long decimal = constToDecimal(line.getString());
            if((decimal <= -256)||(256 <= decimal)){
                line.assignError();
                return;
            }
            dBHandler(line, decimal);
            return;
        }
        if(KeyWordMatch.isDW(line.getString())){
            long decimal = constToDecimal(line.getString());
            if((decimal <= -65536)||(65536 <= decimal)){
                line.assignError();
                return;
            }
            dWHandler(line, decimal);
            return;
        }
        if(KeyWordMatch.isDD(line.getString())){
            String label = parseLabelVal(line.getString());
            if(label != null){
               dDLabelHandler(line, label);
               return;
           }
            long decimal = constToDecimal(line.getString());
            if((decimal <= -4294967296L)||( 4294967296L <= decimal)){
                line.assignError();
                return;
            }
            dDHandler(line, decimal);
            return;
        }
        line.assignError();
        return;
    }
    private static long constToDecimal(String string){
        String number = null; //const that will be parsed from string
        Pattern usrIdPattern = Pattern.compile("^\\s*"+ USR_ID+"\\s+("+DEF_DIR+")\\s+("+CONST_XPT_TEXT+")\\s*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = usrIdPattern.matcher(string);
        if (matcher.find()){
            number = matcher.group(2);
        }
        else{
            //NOP
        }
        if (KeyWordMatch.decConst(number)){
            if ((number.charAt(number.length()-1) == 'd')||(number.charAt(number.length()-1) == 'D'))
                return Long.parseLong(number.substring(0,number.length()-1));
            else
                return Long.parseLong(number);
        }
        if (KeyWordMatch.hexConst(number)){
            number = number.substring(0, number.length()-1);
            Long value = Long.parseLong(number, 16);
            return value;
        }
        if (KeyWordMatch.binConst(number)){
            number = number.substring(0, number.length()-1);
            Long value = Long.parseLong(number, 2);
            return value;
        }
        return 0; //ERROR
    }

    private static String parseId(String string){
        Pattern usrIdPattern = Pattern.compile("^\\s*("+USR_ID+")\\s+("+DEF_DIR+").*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = usrIdPattern.matcher(string);
        matcher.find();
        return matcher.group(1);
    }
    private static String parseText(String string) {
        Pattern usrIdPattern = Pattern.compile("^.*\"("+".*"+")\".*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = usrIdPattern.matcher(string);
        matcher.find();
        return matcher.group(1);
    }
    private static String parseLabelVal(String string){
        Pattern usrIdPattern = Pattern.compile("^\\s*"+ USR_ID+"\\s+("+"DD"+")\\s+("+USR_ID+")\\s*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = usrIdPattern.matcher(string);
        if(matcher.find())
            return matcher.group(2);
        else
            return null;
    }
    private static void dBTextHandler(Line line){
        String name = parseId(line.getString());
        if (usrIdTable.nameIsForbidden(name)){
            line.assignError();
            return;
        }
        else{
            if (curSegment == null){
                line.assignError();
                return;
            }
            else {
                String text = parseText(line.getString());
                usrIdTable.addId(name,"L BYTE",commonOffset,curSegment);
                text = toHex(text);//CONVERTION TO HEX
                line.content = new Content(ContentType.DB_TEXT, text, null, null,null);//assistant field is text
                return;
            }
        }
    }
    public static String toHex(String arg) {
        return String.format("%x", new BigInteger(1, arg.getBytes(/*YOUR_CHARSET?*/)));
    }
    private static void dBHandler(Line line, long number){
       String name = parseId(line.getString());
        if (usrIdTable.nameIsForbidden(name)){
            line.assignError();
            return;
        }
        else{
            if (curSegment == null){
                line.assignError();
                return;
            }
            else {
                usrIdTable.addId(name,"L BYTE",commonOffset,curSegment);
                line.content = new Content(ContentType.DB, Long.toString(number), null,null, null);
                return;
            }
        }
    }
    private static void dWHandler(Line line, long number){
        String name = parseId(line.getString());
        if (usrIdTable.nameIsForbidden(name)){
            line.assignError();
            return;
        }
        else{
            if (curSegment == null){
                line.assignError();
                return;
            }
            else {
                usrIdTable.addId(name,"L WORD",commonOffset,curSegment);
                line.content = new Content(ContentType.DW, Long.toString(number), null,null, null);
                return;
            }
        }
    }
    private static void dDHandler(Line line, long number){
        String name = parseId(line.getString());
        if (usrIdTable.nameIsForbidden(name)){
            line.assignError();
            return;
        }
        else{
            if (curSegment == null){
                line.assignError();
                return;
            }
            else {
                usrIdTable.addId(name,"L DWORD",commonOffset,curSegment);
                line.content = new Content(ContentType.DD, Long.toString(number), null,null, null);
                return;
            }
        }
    }
    private static void dDLabelHandler(Line line, String label){
        String name = parseId(line.getString());
        if (usrIdTable.nameIsForbidden(name)){
            line.assignError();
            return;
        }
        else{
            if (curSegment == null){
                line.assignError();
                return;
            }
            else {
                usrIdTable.addId(name,"L DWORD",commonOffset,curSegment);
                line.content = new Content(ContentType.DD_LABEL, label, null, null, null); //assistant field is name of label
                return;
            }
        }
    }
    public static boolean idIsAllowed(String string){
        if (usrIdTable.nameIsForbidden(string)){
            return false;
        }
        else{
            if (curSegment == null){
                return false;
            }
            else {
                return true;
            }
        }
    }
    private static LexemeOperand createLexemeOperand(String operand) {
        return LexemeOperandBuilder.buildLexemeOperand(operand);
    }
    public static boolean labelHandler(Line line, String label) {
        if(idIsAllowed(label)){
            usrIdTable.addId(label,"L NEAR",commonOffset,curSegment);
            if (line.content.getContentType() == null)
                line.content.setContentType(ContentType.NOTHING);
            line.content.setAssistField(label);
            return true;
        }
        else{
            return false;
        }
    }
    public static boolean mnemHandler(Line line, String mnem) {
        line.content.setContentType(ContentType.MNEM);
        line.content.setMnem(mnem);
        return true;
    }
    public static boolean mnemOperHandler(Line line, String mnem, String operand) {//operand has right syntax
        line.content.setContentType(ContentType.MNEM_OP);
        line.content.setMnem(mnem);
        line.content.setOp1(createLexemeOperand(operand)); //operand has right syntax
        return true;
    }
    public static boolean mnemOperOperHandler(Line line, String mnem, String operand1, String operand2) {
        line.content.setContentType(ContentType.MNEM_OP_OP);
        line.content.setMnem(mnem);
        line.content.setOp1(createLexemeOperand(operand1)); //operand has right syntax
        line.content.setOp2(createLexemeOperand(operand2)); //operand has right syntax
        return true;
    }
}

