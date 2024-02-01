package rrhs.xc.ia.data.mem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import rrhs.xc.ia.data.TestSQLRowUtil;
import rrhs.xc.ia.data.database.SQLRow;

public class MeetTest {
    
    private static Meet m1;
    private static double DELTA = 1e-2;

    @BeforeAll
    public static void setup() {
        SQLRow row1 = TestSQLRowUtil.race1;
        SQLRow row3 = TestSQLRowUtil.race3;
        SQLRow row4 = TestSQLRowUtil.race4;

        Race r1 = new Race(); // var boys
        Race r3 = new Race(); // var girls
        Race r4 = new Race(); // var boys, meet date and name dont match but we can safely ignore this for the test

        TestSQLRowUtil.populateRaceRows();

        r1.loadFromSQL(row1);
        r3.loadFromSQL(row3);
        r4.loadFromSQL(row4);

        m1 = new Meet(List.of(r1, r3, r4));

    }

    @Test 
    public void testGetAverageTime() {        
        assertEquals((26 * 60) + 48.5, m1.getAverageTimeSeconds(Level.VARSITY_GIRLS));

        assertEquals(1079.43, m1.getAverageTimeSeconds(Level.VARSITY_BOYS), DELTA);

        assertEquals(-1, m1.getAverageTimeSeconds(Level.JV_BOYS));
    }

    @Test
    public void testGetAverageSplit() {
        assertEquals((8 * 60) + 15, m1.getAverageSplitSeconds(1, Level.VARSITY_GIRLS));

        assertEquals(337, m1.getAverageSplitSeconds(1, Level.VARSITY_BOYS));

        assertEquals(-1, m1.getAverageSplitSeconds(1, Level.JV_BOYS));
    }

    @Test
    public void testGetTimeSpreadSeconds() {
        assertEquals(0, m1.getTimeSpreadSeconds(Level.VARSITY_GIRLS));

        assertEquals(21.86, m1.getTimeSpreadSeconds(Level.VARSITY_BOYS), DELTA);

        assertEquals(-1, m1.getTimeSpreadSeconds(Level.JV_BOYS));
    }

    @Test
    public void testGetPlaceSpread() {
        assertEquals(0, m1.getPlaceSpread(Level.VARSITY_GIRLS));

        assertEquals(17, m1.getPlaceSpread(Level.VARSITY_BOYS));

        assertEquals(-1, m1.getPlaceSpread(Level.JV_BOYS));
    }

}
