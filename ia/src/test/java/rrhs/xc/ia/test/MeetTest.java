package rrhs.xc.ia.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import rrhs.xc.ia.data.mem.Level;
import rrhs.xc.ia.data.mem.Meet;

public class MeetTest {
    
    private static Meet m1;
    private static double DELTA = 1e-2;

    @BeforeAll
    public static void setup() {
        m1 = new Meet(List.of(TestRacesUtil.race1, TestRacesUtil.race3, TestRacesUtil.race4), "Test", null, -1, true);
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

    @Test 
    public void testGetLevel() {
        assertEquals(Level.VARSITY_BOYS,  m1.getLevels()[0]);
        assertEquals(Level.VARSITY_GIRLS,  m1.getLevels()[1]);
    }

}
