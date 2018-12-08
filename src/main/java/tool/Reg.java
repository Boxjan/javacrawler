package tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Dreaming, fixed later
 * I am not sure why this works but it fixes the problem.
 * User: Boxjan
 * Datetime: Dec 03, 2018 16:08
 */
public class Reg {
    public static String regFind(String text, String reg, int i){
        Matcher m = Pattern.compile(reg, Pattern.CASE_INSENSITIVE).matcher(text);
        return m.find() ? m.group(i) : "";
    }

    public static String regFind(String text, String reg){
        return regFind(text, reg, 1);
    }

    public static String regFindCaseSensitive(String text, String reg, int i){
        Matcher m = Pattern.compile(reg).matcher(text);
        return m.find() ? m.group(i) : "";
    }

    public static String regFindCaseSensitive(String text, String reg){
        return regFindCaseSensitive(text, reg, 1);
    }
}
