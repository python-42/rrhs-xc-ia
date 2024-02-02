package rrhs.xc.ia.data;

import java.time.LocalDate;

import rrhs.xc.ia.data.mem.Level;
import rrhs.xc.ia.data.mem.Race;
import rrhs.xc.ia.data.mem.Season;

public class TestRacesUtil {

    public static final Race race1 = new Race(
            "Jake Herrmann",
            "City Conference",
            LocalDate.of(2023, 10, 14),
            Level.VARSITY_BOYS,
            Season.SENIOR,
            (17 * 60) + 48.5,
            (5 * 60) + 31.0,
            (5 * 60) + 57.0,
            1
        );

    public static final Race race2 = new Race(

            "Otto",
            "City Conference",
            LocalDate.of(2023, 10, 14),
            Level.JV_BOYS,
            Season.FRESHMAN,
            (21 * 60) + 50.6,
            (6 * 60) + 59,
            (6 * 60) + 57,
            6
        );

    public static final Race race3 = new Race(
            "Luighse",
            "City Conference",
            LocalDate.of(2023, 10, 14),
            Level.VARSITY_GIRLS,
            Season.SENIOR,
            (26 * 60) + 48.5,
            (8 * 60) + 15,
            (8 * 60) + 51,
            7
        );

    public static final Race race4 = new Race(
            "Jake Herrmann",
            "Nightfall",
            LocalDate.of(2023, 9, 29),
            Level.VARSITY_BOYS,
            Season.SENIOR,
            (18 * 60) + 10.36,
            (5 * 60) + 43,
            (6 * 60) + 1,
            18
        );

    public static final Race race5 = new Race(
            "Jake Herrmann",
            "Shorewood",
            LocalDate.of(2023, 10, 6),
            Level.VARSITY_BOYS,
            Season.SENIOR,
            (17 * 60) + 49.7,
            (5 * 60) + 31,
            (5 * 60) + 45,
            22
        );

    public static final Race race6 = new Race(
            "Jake Herrmann",
            "Purgolder Invite",
            LocalDate.of(2021, 10, 2),
            Level.VARSITY_BOYS,
            Season.SOPHOMORE,
            (19 * 60) + 44.6,
            (6 * 60) + 6,
            (6 * 60) + 28,
            57
        );

    public static final Race race7 = new Race(
            "Jake Herrmann",
            "Sectionals",
            LocalDate.of(2021, 10, 23),
            Level.VARSITY_BOYS,
            Season.SOPHOMORE,
            (18 * 60) + 54.4,
            (5 * 60) + 59,
            (6 * 60) + 12,
            32
        );

}
