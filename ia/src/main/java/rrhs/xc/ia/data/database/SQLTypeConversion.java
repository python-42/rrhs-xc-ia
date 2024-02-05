package rrhs.xc.ia.data.database;

import java.time.LocalDate;

import rrhs.xc.ia.data.mem.Level;
import rrhs.xc.ia.data.mem.Season;

public class SQLTypeConversion {

    private SQLTypeConversion() {}

    public static String convert(double x) {
        return "'" + x + "'";
    }

    public static String convert(int x) {
        return "'" + x + "'";
    }

    public static String convert(String x) {
        return "'" + x + "'";
    }

    public static String convert(LocalDate x) {
        return "'" + x.toString() + "'";
    }

    public static String convert(boolean x) {
        if (x) {
            return "'" + 1 + "'";
        }
        return "'" + 0 + "'";
    }

    public static String convert(Season x) {
        return convert(x.ordinal());
    }

    public static String convert(Level x) {
        return convert(x.ordinal());   
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
        if (getInt(b) == 0) {
            return false;
        }
        return true;
    }

    public static Season getSeason(String s) {
        int ordinal = getInt(s);
        if(ordinal == 0) {
            return Season.FRESHMAN;
        }

        if(ordinal == 1) {
            return Season.SOPHOMORE;
        }

        if(ordinal == 2) {
            return Season.JUNIOR;
        }

        if(ordinal == 3) {
            return Season.SENIOR;
        }

        return null;
    }

    public static Level getLevel(String s) {
        int ordinal = getInt(s);
        if(ordinal == 0) {
            return Level.VARSITY_BOYS;
        }

        if(ordinal == 1) {
            return Level.VARSITY_GIRLS;
        }

        if(ordinal == 2) {
            return Level.JV_BOYS;
        }

        if(ordinal == 3) {
            return Level.JV_GIRLS;
        }

        return null;
    }

    /**
     * Class which contains the names of the rows in the SQL table. This ensures
     * that the table identifiers match for all implemntations and tests.
     */
    public class SQLTableInformation {
        private SQLTableInformation() {
        }

        public class Meet {
            public static final String MEET_NAME_STR = "name";
            public static final String MEET_DATE_DATE = "date";
            public static final String TOTAL_VAR_BOYS_INT = "totalVarBoys";
            public static final String TOTAL_VAR_GIRLS_INT = "totalVarGirls";
            public static final String TOTAL_JV_BOYS_INT = "totalJVBoys";
            public static final String TOTAL_JV_GIRLS_INT = "totalJVGirls";
        }

        public class Race {
            public static final String TOTAL_TIME_DBL = "timeSeconds";
            public static final String MILE_SPLIT_ONE_DBL = "splitOneSeconds";
            public static final String MILE_SPLIT_TWO_DBL = "splitTwoSeconds";
            public static final String PLACE_INT = "place";
            public static final String SEASON_ENUM = "season";
            public static final String LEVEL_ENUM = "level";
        }

        public class Athlete {
            public static final String NAME_STR = "name";
            public static final String GRADUATION_YEAR_INT = "gradYear";
        }
    }

}
