package rrhs.xc.ia.data.mem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import rrhs.xc.ia.data.TestSQLRowUtil;
import rrhs.xc.ia.data.database.SQLRow;

public class AthleteTest {

    private static Athlete a1;
    private static Athlete empty;

    private static Race r1;
    private static Race r2;
    private static Race r3;

    @BeforeAll
    public static void setup() {
        SQLRow row1 = TestSQLRowUtil.race1;
        SQLRow row2 = TestSQLRowUtil.race2;
        SQLRow row3 = TestSQLRowUtil.race3;

        TestSQLRowUtil.populateRaceRows();

        r1 = new Race(); //SENIOR, fast
        r2 = new Race(); //FRESHMAN
        r3 = new Race(); //SENIOR, slower

        r1.loadFromSQL(row1);
        r2.loadFromSQL(row2);
        r3.loadFromSQL(row3);

        a1 = new Athlete(List.of(r1, r2, r3));
        empty = new Athlete(List.of());
    }

    @Test
    public void testGetWorstSeasonRace() {
        assertEquals(r3, a1.getWorstSeasonRace(Season.SENIOR));
        assertEquals(r2, a1.getWorstSeasonRace(Season.FRESHMAN));

        assertEquals(null, empty.getWorstSeasonRace(Season.FRESHMAN));
        assertEquals(null, empty.getWorstSeasonRace(Season.SOPHOMORE));
        assertEquals(null, empty.getWorstSeasonRace(Season.JUNIOR));
        assertEquals(null, empty.getWorstSeasonRace(Season.SENIOR));
    }

    @Test
    public void testGetBestSeasonRace() {
        assertEquals(r1, a1.getBestSeasonRace(Season.SENIOR));
        assertEquals(r2, a1.getBestSeasonRace(Season.FRESHMAN));

        assertEquals(null, empty.getBestSeasonRace(Season.FRESHMAN));
        assertEquals(null, empty.getBestSeasonRace(Season.SOPHOMORE));
        assertEquals(null, empty.getBestSeasonRace(Season.JUNIOR));
        assertEquals(null, empty.getBestSeasonRace(Season.SENIOR));
    }

    @Test
    public void testGetWorstCareerRace() {
        assertEquals(r3, a1.getWorstCareerRace());

        assertEquals(null, empty.getWorstCareerRace());
    }

    @Test
    public void testGetBestCareerRace() {
        assertEquals(r1, a1.getBestCareerRace());

        assertEquals(null, empty.getBestCareerRace());
    }

    @Test
    public void testGetCareerTimeDrop() {
        assertEquals((9 * 60), a1.getCareerTimeDropSeconds());  

        assertEquals(-1, empty.getCareerTimeDropSeconds());
    }

    @Test
    public void testSeasonTimeDrop() {
        assertEquals((9 * 60), a1.getSeasonTimeDropSeconds(Season.SENIOR));

        assertEquals(0, a1.getSeasonTimeDropSeconds(Season.FRESHMAN));

        assertEquals(-1, empty.getSeasonTimeDropSeconds(Season.FRESHMAN));
        assertEquals(-1, empty.getSeasonTimeDropSeconds(Season.SOPHOMORE));
        assertEquals(-1, empty.getSeasonTimeDropSeconds(Season.JUNIOR));
        assertEquals(-1, empty.getSeasonTimeDropSeconds(Season.SENIOR));
    }

    @Test
    public void testCareerAverageTime() {
        assertEquals(1329.2, a1.getCareerAverageTime());

        assertEquals(-1, empty.getCareerAverageTime());
    }

    
    @Test
    public void testSeasonAverageTime() {
        assertEquals(1338.5, a1.getSeasonAverageTime(Season.SENIOR));

        assertEquals(-1, empty.getSeasonAverageTime(Season.FRESHMAN));
        assertEquals(-1, empty.getSeasonAverageTime(Season.SOPHOMORE));
        assertEquals(-1, empty.getSeasonAverageTime(Season.JUNIOR));
        assertEquals(-1, empty.getSeasonAverageTime(Season.SENIOR));
    }

 
    @Test
    public void testGetSeasons() {
        assertEquals(2, a1.getSeasons().length);
        assertEquals(Season.FRESHMAN, a1.getSeasons()[0]);
        assertEquals(Season.SENIOR, a1.getSeasons()[1]);

        assertEquals(0, empty.getSeasons().length);
    }

    
    @Test
    public void testGetAverageCareerSplit() {
        assertEquals(415, a1.getAverageCareerSplitSeconds(1));

        assertEquals(-1, empty.getAverageCareerSplitSeconds(1));
        assertEquals(-1, empty.getAverageCareerSplitSeconds(2));
        assertEquals(-1, empty.getAverageCareerSplitSeconds(3));
        assertEquals(-1, empty.getAverageCareerSplitSeconds(4));
    }

    
    @Test
    public void testGetAverageSeasonSplit() {
        assertEquals(413, a1.getAverageSeasonSplitSeconds(Season.SENIOR, 1));
        assertEquals(-1, a1.getAverageSeasonSplitSeconds(Season.JUNIOR, 1));

        assertEquals(-1, empty.getAverageSeasonSplitSeconds(Season.FRESHMAN, 1));
        assertEquals(-1, empty.getAverageSeasonSplitSeconds(Season.SOPHOMORE, 1));
        assertEquals(-1, empty.getAverageSeasonSplitSeconds(Season.JUNIOR, 1));
        assertEquals(-1, empty.getAverageSeasonSplitSeconds(Season.SENIOR, 1));

        assertEquals(-1, empty.getAverageSeasonSplitSeconds(Season.FRESHMAN, 2));
        assertEquals(-1, empty.getAverageSeasonSplitSeconds(Season.SOPHOMORE, 2));
        assertEquals(-1, empty.getAverageSeasonSplitSeconds(Season.JUNIOR, 2));
        assertEquals(-1, empty.getAverageSeasonSplitSeconds(Season.SENIOR, 2));

        assertEquals(-1, empty.getAverageSeasonSplitSeconds(Season.FRESHMAN, 3));
        assertEquals(-1, empty.getAverageSeasonSplitSeconds(Season.SOPHOMORE, 3));
        assertEquals(-1, empty.getAverageSeasonSplitSeconds(Season.JUNIOR, 3));
        assertEquals(-1, empty.getAverageSeasonSplitSeconds(Season.SENIOR, 3));

        assertEquals(-1, empty.getAverageSeasonSplitSeconds(Season.FRESHMAN, 4));
        assertEquals(-1, empty.getAverageSeasonSplitSeconds(Season.SOPHOMORE, 4));
        assertEquals(-1, empty.getAverageSeasonSplitSeconds(Season.JUNIOR, 4));
        assertEquals(-1, empty.getAverageSeasonSplitSeconds(Season.SENIOR, 4));
    }

}
