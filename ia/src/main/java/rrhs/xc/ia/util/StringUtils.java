package rrhs.xc.ia.util;

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

    public static double deFormatTime(String timeStr) {
        int minutesBoundary = timeStr.indexOf(':');
        double minutes = Integer.parseInt(timeStr.substring(0, minutesBoundary)) * 60.0;
        return minutes + Double.parseDouble(timeStr.substring(minutesBoundary + 1));
    }

    public static boolean validTimeFormat(String timeStr) {
        if (!timeStr.contains(":")) {
            return false;
        }

        try {
            Integer.parseInt(timeStr.substring(0, timeStr.indexOf(':')));
            Double.parseDouble(timeStr.substring(timeStr.indexOf(':') + 1));
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    public static boolean matchesRegex(String regex, String test) {
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        return p.matcher(test).find();
    }
}
