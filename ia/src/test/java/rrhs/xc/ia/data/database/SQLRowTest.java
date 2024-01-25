package rrhs.xc.ia.data.database;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class SQLRowTest {
    
    private static SQLRow athlete;
    private static SQLRow race;
    private static SQLRow meet;

    /**
     * Assume that putting strings to the map works as expected.
     */
    @BeforeAll
    public static void setup() {
        athlete = new SQLRow("Athlete", 1);
        athlete.putPair("name", "Jake Herrmann");
        athlete.putPair("gradYear", 2024);
        athlete.putPair("isBoysTeam", true);

        race = new SQLRow("Race", 2);
        race.putPair("time", 17);
        race.putPair("mileOneSplit", 32.1);
        race.putPair("mileTwoSplit", 44.2);
        race.putPair("place", 1);

        meet = new SQLRow("Meet", 3);
        meet.putPair("date", LocalDate.of(2023, 10, 5));
        meet.putPair("name", "Conference 2023");
        meet.putPair("numVarBoys", 45);

    }

    @Test
    public void testInsertString() {
        assertEquals("INSERT INTO Athlete (gradYear, isBoysTeam, name) VALUES ('2024', '1', 'Jake Herrmann');", athlete.getSQLInsertString());
        assertEquals("INSERT INTO Race (mileOneSplit, mileTwoSplit, place, time) VALUES ('32.1', '44.2', '1', '17');", race.getSQLInsertString());
        assertEquals("INSERT INTO Meet (date, name, numVarBoys) VALUES ('2023-10-05', 'Conference 2023', '45');", meet.getSQLInsertString());
    }

    @Test
    public void testUpdateString() {
        assertEquals("UPDATE Athlete SET gradYear = '2024', isBoysTeam = '1', name = 'Jake Herrmann' WHERE id = 1;", athlete.getSQLUpdateString());
        assertEquals("UPDATE Race SET mileOneSplit = '32.1', mileTwoSplit = '44.2', place = '1', time = '17' WHERE id = 2;", race.getSQLUpdateString());
        assertEquals("UPDATE Meet SET date = '2023-10-05', name = 'Conference 2023', numVarBoys = '45' WHERE id = 3;", meet.getSQLUpdateString());
    }

    @Test
    public void testDeleteString() {
        assertEquals("DELETE FROM Athlete WHERE id = 1;", athlete.getSQLDeleteString());
        assertEquals("DELETE FROM Race WHERE id = 2;", race.getSQLDeleteString());
        assertEquals("DELETE FROM Meet WHERE id = 3;", meet.getSQLDeleteString());
    }
}
