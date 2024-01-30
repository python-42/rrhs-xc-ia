package rrhs.xc.ia.data.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rrhs.xc.ia.data.Season;
import rrhs.xc.ia.data.TestSQLRowUtil;
import rrhs.xc.ia.data.database.SQLRow;
import rrhs.xc.ia.data.database.SQLTypeConversion.SQLTableInformation;
import rrhs.xc.ia.data.mem.Race;

public class RaceSQLTest {

    private Race race1;
    private Race race2;

    private static SQLRow row1;
    private static SQLRow row2;

    @BeforeAll
    public static void setup() {
        row1 = TestSQLRowUtil.race1;
        row2 = TestSQLRowUtil.race2;

        TestSQLRowUtil.populateRaceRows();
    }

    @BeforeEach
    public void reset() {
        race1 = new Race();
        race2 = new Race();
    }

    @Test
    public void testSQLWrite() {
        race1.loadFromSQL(row1);
        assertEquals("Jake Herrmann", race1.getAthleteName());
        assertEquals("City Conference", race1.getMeetName());
        assertEquals(LocalDate.of(2023, 10, 14), race1.getMeetDate());
        assertEquals(Season.SENIOR, race1.getSeason());
        assertEquals(true, race1.isVarsity());
        assertEquals((17 * 60) + 48.5, race1.getTimeSeconds());
        assertEquals((5 * 60) + 31, race1.getMileOneSplitSeconds());
        assertEquals((5 * 60) + 57, race1.getMileTwoSplitSeconds());

        race2.loadFromSQL(row2);
        assertEquals("Otto", race2.getAthleteName());
        assertEquals("City Conference", race2.getMeetName());
        assertEquals(LocalDate.of(2023, 10, 14), race1.getMeetDate());
        assertEquals(Season.FRESHMAN, race2.getSeason());
        assertEquals(false, race2.isVarsity());
        assertEquals((21*60) + 50.6, race2.getTimeSeconds());
        assertEquals((6 * 60) + 59, race2.getMileOneSplitSeconds());
        assertEquals((6 * 60) + 57, race2.getMileTwoSplitSeconds());


        race1.loadFromSQL(row2);//try and write new data
        assertEquals((17 * 60) + 48.5, race1.getTimeSeconds()); //Assert that data has not changed.
    }

    
    /*
     * This test assumes that writing to the object via the map works.
     */
    @Test 
    public void testSQLRead() {
        //write test data
        race1.loadFromSQL(row1);
        race2.loadFromSQL(row2);

        SQLRow resultRow1 = race1.writeTOSQL();
        assertEquals("'Jake Herrmann'", resultRow1.get(SQLTableInformation.Race.ATHLETE_NAME_STR));
        assertEquals("'City Conference'", resultRow1.get(SQLTableInformation.Race.MEET_NAME_STR));
        assertEquals("'2023-10-14'", resultRow1.get(SQLTableInformation.Race.MEET_DATE_STR));
        assertEquals("'3'", resultRow1.get(SQLTableInformation.Race.SEASON_ENUM));
        assertEquals("'1'", resultRow1.get(SQLTableInformation.Race.VARSITY_BOOL));
        assertEquals("'1068.5'", resultRow1.get(SQLTableInformation.Race.TOTAL_TIME_DBL));
        assertEquals("'331.0'", resultRow1.get(SQLTableInformation.Race.MILE_SPLIT_ONE_DBL));
        assertEquals("'357.0'", resultRow1.get(SQLTableInformation.Race.MILE_SPLIT_TWO_DBL));

        SQLRow resultRow2 = race2.writeTOSQL();
        assertEquals("'Otto'", resultRow2.get(SQLTableInformation.Race.ATHLETE_NAME_STR));
        assertEquals("'City Conference'", resultRow2.get(SQLTableInformation.Race.MEET_NAME_STR));
        assertEquals("'2023-10-14'", resultRow1.get(SQLTableInformation.Race.MEET_DATE_STR));
        assertEquals("'0'", resultRow2.get(SQLTableInformation.Race.SEASON_ENUM));
        assertEquals("'0'", resultRow2.get(SQLTableInformation.Race.VARSITY_BOOL));
        assertEquals("'1310.6'", resultRow2.get(SQLTableInformation.Race.TOTAL_TIME_DBL));
        assertEquals("'419.0'", resultRow2.get(SQLTableInformation.Race.MILE_SPLIT_ONE_DBL));
        assertEquals("'417.0'", resultRow2.get(SQLTableInformation.Race.MILE_SPLIT_TWO_DBL));
    }

}
