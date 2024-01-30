package rrhs.xc.ia.data;

import java.time.LocalDate;

import rrhs.xc.ia.data.database.SQLRow;
import rrhs.xc.ia.data.database.SQLTypeConversion.SQLTableInformation;

/**
 * This class holds several SQLRow objects which can be used for testing purposes.
 */
public class TestSQLRowUtil {
    
    public static final SQLRow athlete1 = new SQLRow("Athlete", 0);
    public static final SQLRow athlete2 = new SQLRow("Athlete", 0);

    public static final SQLRow race1 = new SQLRow("Race", 0);
    public static final SQLRow race2 = new SQLRow("Race", 0);
    public static final SQLRow race3 = new SQLRow("Race", 0);

    public static void populateAthleteRows() {
        athlete1.putPair(SQLTableInformation.Athlete.NAME_STR, "Jake Herrmann");
        athlete1.putPair(SQLTableInformation.Athlete.BOYS_TEAM_BOOL, true);
        athlete1.putPair(SQLTableInformation.Athlete.GRADUATION_YEAR_INT, 2024);

        athlete2.putPair(SQLTableInformation.Athlete.NAME_STR, "Will");
        athlete2.putPair(SQLTableInformation.Athlete.BOYS_TEAM_BOOL, false);
        athlete2.putPair(SQLTableInformation.Athlete.GRADUATION_YEAR_INT, 2027);
    }

    public static void populateRaceRows() {
        race1.putPair(SQLTableInformation.Race.ATHLETE_NAME_STR, "Jake Herrmann");
        race1.putPair(SQLTableInformation.Race.MEET_NAME_STR, "City Conference");
        race1.putPair(SQLTableInformation.Race.MEET_DATE_STR, LocalDate.of(2023, 10, 14));
        race1.putPair(SQLTableInformation.Race.SEASON_ENUM, Season.SENIOR);
        race1.putPair(SQLTableInformation.Race.VARSITY_BOOL, true);
        race1.putPair(SQLTableInformation.Race.TOTAL_TIME_DBL, (17*60) + 48.5);
        race1.putPair(SQLTableInformation.Race.MILE_SPLIT_ONE_DBL, (5 * 60) + 31);
        race1.putPair(SQLTableInformation.Race.MILE_SPLIT_TWO_DBL, (5 * 60) + 57);
        race1.putPair(SQLTableInformation.Race.PLACE_INT, 1);
 
        race2.putPair(SQLTableInformation.Race.ATHLETE_NAME_STR, "Otto");
        race2.putPair(SQLTableInformation.Race.MEET_NAME_STR, "City Conference");
        race2.putPair(SQLTableInformation.Race.MEET_DATE_STR, LocalDate.of(2023, 10, 14));
        race2.putPair(SQLTableInformation.Race.SEASON_ENUM, Season.FRESHMAN);
        race2.putPair(SQLTableInformation.Race.VARSITY_BOOL, false);
        race2.putPair(SQLTableInformation.Race.TOTAL_TIME_DBL, (21*60) + 50.6);
        race2.putPair(SQLTableInformation.Race.MILE_SPLIT_ONE_DBL, (6 * 60) + 59);
        race2.putPair(SQLTableInformation.Race.MILE_SPLIT_TWO_DBL, (6 * 60) + 57);
        race2.putPair(SQLTableInformation.Race.PLACE_INT, 6);
        
        race3.putPair(SQLTableInformation.Race.ATHLETE_NAME_STR, "Luighse");
        race3.putPair(SQLTableInformation.Race.MEET_NAME_STR, "City Conference");
        race3.putPair(SQLTableInformation.Race.MEET_DATE_STR, LocalDate.of(2023, 10, 14));
        race3.putPair(SQLTableInformation.Race.SEASON_ENUM, Season.SENIOR);
        race3.putPair(SQLTableInformation.Race.VARSITY_BOOL, true);
        race3.putPair(SQLTableInformation.Race.TOTAL_TIME_DBL, (26*60) + 48.5);
        race3.putPair(SQLTableInformation.Race.MILE_SPLIT_ONE_DBL, (8 * 60) + 15);
        race3.putPair(SQLTableInformation.Race.MILE_SPLIT_TWO_DBL, (8 * 60) + 51);
        race3.putPair(SQLTableInformation.Race.PLACE_INT, 7);
    }

    public static void populateMeetRows() {

    }
}
