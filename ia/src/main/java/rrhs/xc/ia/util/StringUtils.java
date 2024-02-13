package rrhs.xc.ia.util;

import java.io.File;
import java.util.regex.Pattern;

public class StringUtils {
    
    public static String formatTime(double timeSeconds) {
        if (timeSeconds == -1) {
            return "0";
        }

        String seconds = ((timeSeconds % 60) + 0.005) + "";

        if(((timeSeconds % 60) + 0.005) < 10) {
            seconds = "0" + seconds;
        }

        if(seconds.length() > 5) {
            seconds = seconds.substring(0, 5);
        }

        return ((int) timeSeconds / 60) + ":" + seconds;
    }

    public static boolean matchesRegex(String regex, String test) {
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        return p.matcher(test).find();
    }

    public static String removeFilExtension(File f) {
        String rtn = f.getName();
        return rtn.substring(0, rtn.lastIndexOf('.'));
    }
}
