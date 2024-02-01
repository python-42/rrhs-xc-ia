package rrhs.xc.ia.data.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import rrhs.xc.ia.data.TestSQLRowUtil;
import rrhs.xc.ia.data.database.SQLRow;
import rrhs.xc.ia.data.database.SQLTypeConversion.SQLTableInformation;
import rrhs.xc.ia.data.mem.Meet;

public class MeetSQLTest {
    private Meet m1;
    private Meet m2;

    private static SQLRow row1;
    private static SQLRow row2;

    @BeforeAll
    public static void setup() {
        row1 = TestSQLRowUtil.meet1;
        row2 = TestSQLRowUtil.meet2;

        TestSQLRowUtil.populateMeetRows();
    }

    @BeforeEach
    public void reset() {
        m1 = new Meet(null);
        m2 = new Meet(null);
    }

    @Test
    public void testSQLWrite() {
        m1.loadFromSQL(row1);
        assertEquals("Conference 2023", m1.getName());
        assertEquals(LocalDate.of(2023, 10, 14), m1.getDate());
        assertEquals(40, m1.getTotalAthleteCounts()[0]);
        assertEquals(38, m1.getTotalAthleteCounts()[1]);
        assertEquals(55, m1.getTotalAthleteCounts()[2]);
        assertEquals(6, m1.getTotalAthleteCounts()[3]);

        m2.loadFromSQL(row2);
        assertEquals("Sectionals 2022", m2.getName());
        assertEquals(LocalDate.of(2022, 10, 22), m2.getDate());
        assertEquals(56, m2.getTotalAthleteCounts()[0]);
        assertEquals(54, m2.getTotalAthleteCounts()[1]);
        assertEquals(0, m2.getTotalAthleteCounts()[2]);
        assertEquals(0, m2.getTotalAthleteCounts()[3]);

        m1.loadFromSQL(row2);//try and write new data
        assertEquals("Conference 2023", m1.getName()); //Assert that data has not changed.
    }

    
    /*
     * This test assumes that writing to the object via the map works.
     */
    @Test 
    public void testSQLRead() {
        //write test data
        m1.loadFromSQL(row1);
        m2.loadFromSQL(row2);

        SQLRow resultRow1 = m1.writeTOSQL();
        assertEquals("'Conference 2023'", resultRow1.get(SQLTableInformation.Meet.MEET_NAME_STR));
        assertEquals("'2023-10-14'", resultRow1.get(SQLTableInformation.Meet.MEET_DATE_DATE));
        assertEquals("'40'", resultRow1.get(SQLTableInformation.Meet.TOTAL_VAR_BOYS_INT));
        assertEquals("'38'", resultRow1.get(SQLTableInformation.Meet.TOTAL_VAR_GIRLS_INT));
        assertEquals("'55'", resultRow1.get(SQLTableInformation.Meet.TOTAL_JV_BOYS_INT));
        assertEquals("'6'", resultRow1.get(SQLTableInformation.Meet.TOTAL_JV_GIRLS_INT));

        SQLRow resultRow2 = m2.writeTOSQL();
        assertEquals("'Sectionals 2022'", resultRow2.get(SQLTableInformation.Meet.MEET_NAME_STR));
        assertEquals("'2022-10-22'", resultRow2.get(SQLTableInformation.Meet.MEET_DATE_DATE));
        assertEquals("'56'", resultRow2.get(SQLTableInformation.Meet.TOTAL_VAR_BOYS_INT));
        assertEquals("'54'", resultRow2.get(SQLTableInformation.Meet.TOTAL_VAR_GIRLS_INT));
        assertEquals("'0'", resultRow2.get(SQLTableInformation.Meet.TOTAL_JV_BOYS_INT));
        assertEquals("'0'", resultRow2.get(SQLTableInformation.Meet.TOTAL_JV_GIRLS_INT));
    }    
}
