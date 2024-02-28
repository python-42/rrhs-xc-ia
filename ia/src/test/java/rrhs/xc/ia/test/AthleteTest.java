package rrhs.xc.ia.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import rrhs.xc.ia.data.mem.Athlete;
import rrhs.xc.ia.data.mem.Race;
import rrhs.xc.ia.data.mem.Season;


public class AthleteTest {

    private static Athlete a1;
    private static Athlete empty;

    private static Race r1;
    private static Race r2;
    private static Race r3;

    @BeforeAll
    public static void setup() {

        r1 = TestRacesUtil.race1;
        r2 = TestRacesUtil.race2;
        r3 = TestRacesUtil.race3;

        a1 = new Athlete(List.of(r1, r2, r3), "Test", 2024, -1, true);
        empty = new Athlete(List.of(), "Nobody", 1970, -1, true);
    }

    @Test
    public void testDetermineSeason() {
        assertEquals(Season.SENIOR, a1.determineSeason(LocalDate.of(2023, Month.OCTOBER, 14)));
        assertEquals(Season.SENIOR, a1.determineSeason(LocalDate.of(2024, Month.MARCH, 3)));
        assertEquals(Season.JUNIOR, a1.determineSeason(LocalDate.of(2022, Month.AUGUST, 2)));
        assertEquals(Season.JUNIOR, a1.determineSeason(LocalDate.of(2022, Month.JULY, 2)));
        assertEquals(Season.FRESHMAN, a1.determineSeason(LocalDate.of(2020, Month.OCTOBER, 17)));
        assertEquals(null, a1.determineSeason(LocalDate.of(2024, Month.OCTOBER, 18)));
        assertEquals(null, a1.determineSeason(LocalDate.of(2007, Month.MARCH, 29)));
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
