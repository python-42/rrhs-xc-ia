package rrhs.xc.ia.data.database;

import java.time.LocalDate;

public class SQLTypeConversion {
    
    private SQLTypeConversion() {}

    public static String convert(double x) {
        return "'"+x+"'";
    }

    public static String convert(int x) {
        return "'"+x+"'";
    }

    public static String convert(String x) {
        return "'"+x+"'";
    }

    public static String convert(LocalDate x) {
        return "'" + x.toString() + "'";
    }

    public static String convert(boolean x) {
        if(x) {
            return "'"+1+"'";
        }
        return "'"+0+"'";
    }

    public static String getString(String s) {
        return s.replaceFirst("'", "").substring(0, s.length() - 2);

    }

    public static double getDouble(String d) {
        return Double.parseDouble(d.replaceAll("'", "").replaceAll(" ", ""));
    }

    public static int getInt(String i) {
        return Integer.parseInt(i.replaceAll("'", "").replaceAll(" ", ""));
    }

    public static LocalDate getDate(String d) {
        d = d.replaceAll("'", "").replaceAll(" ", "");
        return LocalDate.of(getInt(d.substring(0, 4)), getInt(d.substring(5, 7)), getInt(d.substring(8, 10)));
    }

    public static boolean getBoolean(String b) {
        if(getInt(b) == 0) {
            return false;
        }
        return true;
    }

}
