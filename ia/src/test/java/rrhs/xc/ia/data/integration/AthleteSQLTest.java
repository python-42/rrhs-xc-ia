package rrhs.xc.ia.data.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rrhs.xc.ia.data.database.SQLRow;
import rrhs.xc.ia.data.database.SQLTypeConversion.SQLTableInformation;
import rrhs.xc.ia.data.mem.Athlete;

public class AthleteSQLTest {

    private Athlete a1;
    private Athlete a2;

    private static SQLRow row1;
    private static SQLRow row2;

    @BeforeAll
    public static void setup() {
        row1 = new SQLRow("Athlete", 0);
        row2 = new SQLRow("Athlete", 1);

        row1.putPair(SQLTableInformation.Athlete.NAME_STR, "Jake Herrmann");
        row1.putPair(SQLTableInformation.Athlete.BOYS_TEAM_BOOL, true);
        row1.putPair(SQLTableInformation.Athlete.GRADUATION_YEAR_INT, 2024);

        row2.putPair(SQLTableInformation.Athlete.NAME_STR, "Will");
        row2.putPair(SQLTableInformation.Athlete.BOYS_TEAM_BOOL, false);
        row2.putPair(SQLTableInformation.Athlete.GRADUATION_YEAR_INT, 2027);
    }

    @BeforeEach
    public void reset() {
        a1 = new Athlete(null);
        a2 = new Athlete(null);
    }

    @Test
    public void testSQLWrite() {
        a1.loadFromSQL(row1);
        assertEquals("Jake Herrmann", a1.getName());
        assertEquals(true, a1.isBoysTeam());
        assertEquals(2024, a1.getGradYear());

        a2.loadFromSQL(row2);
        assertEquals("Will", a2.getName());
        assertEquals(false, a2.isBoysTeam());
        assertEquals(2027, a2.getGradYear());

        a1.loadFromSQL(row2);//try and write new data
        assertEquals("Jake Herrmann", a1.getName()); //Assert that data has not changed.
    }

    
    /*
     * This test assumes that writing to the object via the map works.
     */
    @Test 
    public void testSQLRead() {
        //write test data
        a1.loadFromSQL(row1);
        a2.loadFromSQL(row2);

        SQLRow resultRow1 = a1.writeTOSQL();
        assertEquals("'Jake Herrmann'",resultRow1.get(SQLTableInformation.Athlete.NAME_STR));
        assertEquals("'1'", resultRow1.get(SQLTableInformation.Athlete.BOYS_TEAM_BOOL));
        assertEquals("'2024'", resultRow1.get(SQLTableInformation.Athlete.GRADUATION_YEAR_INT));

        SQLRow resultRow2 = a2.writeTOSQL();
        assertEquals("'Will'",resultRow2.get(SQLTableInformation.Athlete.NAME_STR));
        assertEquals("'0'", resultRow2.get(SQLTableInformation.Athlete.BOYS_TEAM_BOOL));
        assertEquals("'2027'", resultRow2.get(SQLTableInformation.Athlete.GRADUATION_YEAR_INT));
    }

}
