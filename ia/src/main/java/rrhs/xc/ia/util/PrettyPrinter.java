package rrhs.xc.ia.util;

public class PrettyPrinter {
    
    public static String formatTime(double timeSeconds) {
        return ((int) timeSeconds / 60) + ":" + (timeSeconds % 60);
    }
}
