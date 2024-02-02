package rrhs.xc.ia.data.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.itextpdf.text.DocumentException;

import rrhs.xc.ia.data.TestSQLRowUtil;
import rrhs.xc.ia.data.database.SQLRow;
import rrhs.xc.ia.data.mem.Athlete;
import rrhs.xc.ia.data.mem.Meet;
import rrhs.xc.ia.data.mem.Race;

public class PDFCreationTest {
    
    @Test
    public void testAthletePDF() throws DocumentException, IOException {
        SQLRow row1 = TestSQLRowUtil.race1;
        SQLRow row2 = TestSQLRowUtil.race4;
        SQLRow row3 = TestSQLRowUtil.race5;

        SQLRow row4 = TestSQLRowUtil.race6;
        SQLRow row5 = TestSQLRowUtil.race7;

        SQLRow athleteRow = TestSQLRowUtil.athlete1;

        TestSQLRowUtil.populateRaceRows();
        TestSQLRowUtil.populateAthleteRows();

        Race r1 = new Race();
        Race r2 = new Race();
        Race r3 = new Race();
        Race r4 = new Race();
        Race r5 = new Race();

        r1.loadFromSQL(row1);
        r2.loadFromSQL(row2);
        r3.loadFromSQL(row3);
        r4.loadFromSQL(row4);
        r5.loadFromSQL(row5);

        Athlete a1 = new Athlete(List.of(r1, r2, r3, r4, r5));

        a1.loadFromSQL(athleteRow);

        File f = new File("AthleteTestPDF.pdf");
        a1.writeToPDF(f);

        assertTrue(f.exists());
        assertTrue(f.isFile());
        assertTrue(f.canRead());
    }

    @Test
    public void testMeetPDF() throws IOException, DocumentException{
        SQLRow row1 = TestSQLRowUtil.race1;
        SQLRow row2 = TestSQLRowUtil.race2;
        SQLRow row3 = TestSQLRowUtil.race3;
        SQLRow row4 = TestSQLRowUtil.race4;
        SQLRow row5 = TestSQLRowUtil.race5;

        SQLRow meetRow = TestSQLRowUtil.meet1;

        TestSQLRowUtil.populateRaceRows();
        TestSQLRowUtil.populateMeetRows();

        Race r1 = new Race();
        Race r2 = new Race();
        Race r3 = new Race();
        Race r4 = new Race();
        Race r5 = new Race();

        r1.loadFromSQL(row1);
        r2.loadFromSQL(row2);
        r3.loadFromSQL(row3);
        r4.loadFromSQL(row4);
        r5.loadFromSQL(row5);

        Meet m1 = new Meet(List.of(r1, r2, r3, r4, r5));

        m1.loadFromSQL(meetRow);

        File f = new File("MeetTestPDF.pdf");
        m1.writeToPDF(f);

        assertTrue(f.exists());
        assertTrue(f.isFile());
        assertTrue(f.canRead());
    }

}
