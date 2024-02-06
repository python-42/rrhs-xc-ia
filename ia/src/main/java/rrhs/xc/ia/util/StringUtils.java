package rrhs.xc.ia.util;

public class StringUtils {
    
    public static String formatTime(double timeSeconds) {
        String seconds = ((timeSeconds % 60) + 0.005) + "";

        if(((timeSeconds % 60) + 0.005) < 10) {
            seconds = "0" + seconds;
        }

        if(seconds.length() > 5) {
            seconds = seconds.substring(0, 5);
        }

        return ((int) timeSeconds / 60) + ":" + seconds;
    }
}
