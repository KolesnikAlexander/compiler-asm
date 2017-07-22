package com.company.program.check;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.company.program.check.KeyWords.*;


/**
 * Check for a keywords match using regular expressions.
 */
public class KeyWordMatch {
    public static boolean isEmpty(String string) {
        Pattern usrIdPattern = Pattern.compile("^\\s*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = usrIdPattern.matcher(string);

        if (matcher.find())
            return true;
        else
            return false;
    }
    public static String isSegmentStart(String string){
        Pattern usrIdPattern = Pattern.compile("^\\s*("+ USR_ID+")\\s+"+SEGMENT_DIR+"\\s*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = usrIdPattern.matcher(string);

        if (matcher.find())
            return matcher.group(1);
        else
            return null;
    }
    public static String isSegmentEnd(String string){
        Pattern usrIdPattern = Pattern.compile("^\\s*("+ USR_ID+")\\s+"+ENDS_DIR+"\\s*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = usrIdPattern.matcher(string);

        if (matcher.find())
            return matcher.group(1);
        else
            return null;
    }
    public static boolean isDefine(String string){
        Pattern usrIdPattern = Pattern.compile("^\\s*"+ USR_ID+"\\s+("+DEF_DIR+")\\s+("+CONSTANT+"|"+USR_ID+")\\s*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = usrIdPattern.matcher(string);

        if (matcher.find()){
            return true;
        }
        else
            return false;
    }

    /**
     * Checks whether db is present
     * @param string
     * @return <tt>true</tt> if db is present
     */
    public static boolean isDB(String string){
        Pattern usrIdPattern = Pattern.compile("^\\s*"+ USR_ID+"\\s+("+"DB"+")\\s+("+CONSTANT+")\\s*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = usrIdPattern.matcher(string);

        if (matcher.find()){
            return true;
        }
        else
            return false;
    }
    /**
     * Checks whether dw is present
     * @param string
     * @return <tt>true</tt> if dw is present
     */
    public static boolean isDW(String string){
        Pattern usrIdPattern = Pattern.compile("^\\s*"+ USR_ID+"\\s+("+"DW"+")\\s+("+CONST_XPT_TEXT+")\\s*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = usrIdPattern.matcher(string);

        if (matcher.find()){
            return true;
        }
        else
            return false;
    }
    /**
     * Checks whether dd is present
     * @param string
     * @return <tt>true</tt> if dd is present
     */
    public static boolean isDD(String string){
        Pattern usrIdPattern = Pattern.compile("^\\s*"+ USR_ID+"\\s+("+"DD"+")\\s+("+CONST_XPT_TEXT+"|"+USR_ID+")\\s*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = usrIdPattern.matcher(string);

        if (matcher.find()){
            return true;
        }
        else
            return false;
    }

    public static boolean isEnd(String string) {
        Pattern usrIdPattern = Pattern.compile("^\\s*END\\s*$",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = usrIdPattern.matcher(string);

        if (matcher.find())
            return true;
        else
            return false;
    }

    static boolean textConst(String number) {
        if (number == null){
            return true;
        }
        Pattern usrIdPattern = Pattern.compile("^\\s*"+ USR_ID+"\\s+("+DEF_DIR+")\\s+("+TEXT_CONST+")\\s*$",
                Pattern.CASE_INSENSITIVE);
        return  usrIdPattern.matcher(number).find();
    }

    public static boolean binConst(String number) {
        if (number == null){
            return true;
        }
        Pattern usrIdPattern = Pattern.compile("^"+BIN_CONST+"$",
                Pattern.CASE_INSENSITIVE);
        return  usrIdPattern.matcher(number).find();
    }

    public static boolean hexConst(String number) {
        if (number == null){
            return true;
        }
        Pattern usrIdPattern = Pattern.compile("^"+HEX_CONST+"$",
                Pattern.CASE_INSENSITIVE);
        return  usrIdPattern.matcher(number).find();
    }

    public static boolean decConst(String number) {
        String res = null;
        if (number == null){
            return true;
        }
        Pattern usrIdPattern = Pattern.compile("^("+DEC_CONST+")$",
                Pattern.CASE_INSENSITIVE);
        return  usrIdPattern.matcher(number).find();
    }
}
