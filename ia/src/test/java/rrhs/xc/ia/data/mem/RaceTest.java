package rrhs.xc.ia.data.mem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import rrhs.xc.ia.data.database.SQLRow;
import rrhs.xc.ia.data.database.SQLTypeConversion.SQLTableInformation;

public class RaceTest {
    
    private static Race race1;
    private static Race race2;
    private static Race race3;

    private static SQLRow row1;
    private static SQLRow row2;
    private static SQLRow row3;

    private static final double DELTA = 1e-2;

    /**
     * All tests in this class assume that setup via the SQLSerializable interface works.
     */
    @BeforeAll
    public static void setup() {
        race1 = new Race();
        race2 = new Race();
        race3 = new Race();

        row1 = new SQLRow("", 0);
        row1.putPair(SQLTableInformation.Race.ATHLETE_NAME_STR, "Jake Herrmann");
        row1.putPair(SQLTableInformation.Race.MEET_NAME_STR, "Conference 2023");
        row1.putPair(SQLTableInformation.Race.VARSITY_BOOL, true);
        row1.putPair(SQLTableInformation.Race.TOTAL_TIME_DBL, (17*60) + 48.5);
        row1.putPair(SQLTableInformation.Race.MILE_SPLIT_ONE_DBL, (5 * 60) + 31);
        row1.putPair(SQLTableInformation.Race.MILE_SPLIT_TWO_DBL, (5 * 60) + 57);
        row1.putPair(SQLTableInformation.Race.PLACE_INT, 1);

        row2 = new SQLRow("", 0);
        row2.putPair(SQLTableInformation.Race.ATHLETE_NAME_STR, "Otto");
        row2.putPair(SQLTableInformation.Race.MEET_NAME_STR, "Conference 2023");
        row2.putPair(SQLTableInformation.Race.VARSITY_BOOL, false);
        row2.putPair(SQLTableInformation.Race.TOTAL_TIME_DBL, (21*60) + 50.6);
        row2.putPair(SQLTableInformation.Race.MILE_SPLIT_ONE_DBL, (6 * 60) + 59);
        row2.putPair(SQLTableInformation.Race.MILE_SPLIT_TWO_DBL, (6 * 60) + 57);
        row2.putPair(SQLTableInformation.Race.PLACE_INT, 6);

        row3 = new SQLRow("", 0);
        row3.putPair(SQLTableInformation.Race.ATHLETE_NAME_STR, "Luighse");
        row3.putPair(SQLTableInformation.Race.MEET_NAME_STR, "Conference 2023");
        row3.putPair(SQLTableInformation.Race.VARSITY_BOOL, true);
        row3.putPair(SQLTableInformation.Race.TOTAL_TIME_DBL, (26*60) + 48.5);
        row3.putPair(SQLTableInformation.Race.MILE_SPLIT_ONE_DBL, (8 * 60) + 15);
        row3.putPair(SQLTableInformation.Race.MILE_SPLIT_TWO_DBL, (8 * 60) + 51);
        row3.putPair(SQLTableInformation.Race.PLACE_INT, 7);

        race1.loadFromSQL(row1);
        race2.loadFromSQL(row2);
        race3.loadFromSQL(row3);
    }

    @Test
    public void testAverageSplit() {
        assertEquals((5*60) + 44.677, race1.getAverageSplitSeconds(), DELTA);
        assertEquals((7*60) + 2.774, race2.getAverageSplitSeconds(), DELTA);
        assertEquals((8*60) + 38.871, race3.getAverageSplitSeconds(), DELTA);
    }

    @Test
    public void testMileThreeSplit() {
        assertEquals((5*60) + 45.909, race1.getMileThreeSplitSeconds(), DELTA);
        assertEquals((7*60) + 11.455, race2.getMileThreeSplitSeconds(), DELTA);
        assertEquals((8*60) + 49.545, race3.getMileThreeSplitSeconds(), DELTA);
    }

    @Test
    public void testSplitDifference() {
        assertEquals(26.0, race1.getSplitDifferenceSeconds(), DELTA);
        assertEquals(14.455, race2.getSplitDifferenceSeconds(), DELTA);
        assertEquals(36.0, race3.getSplitDifferenceSeconds(), DELTA);
    }

}
