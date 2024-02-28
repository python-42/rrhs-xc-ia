package rrhs.xc.ia.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import rrhs.xc.ia.data.mem.Race;

public class RaceTest {
    
    private static Race race1;
    private static Race race2;
    private static Race race3;

    private static final double DELTA = 1e-2;

    @BeforeAll
    public static void setup() {
        race1 = TestRacesUtil.race1;
        race2 = TestRacesUtil.race2;
        race3 = TestRacesUtil.race3;
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
