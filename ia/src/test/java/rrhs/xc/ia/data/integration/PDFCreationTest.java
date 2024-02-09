package rrhs.xc.ia.data.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.itextpdf.text.DocumentException;

import rrhs.xc.ia.data.TestRacesUtil;
import rrhs.xc.ia.data.mem.Athlete;
import rrhs.xc.ia.data.mem.Meet;

public class PDFCreationTest {
    
    @Test
    public void testAthletePDF() throws DocumentException, IOException {
        Athlete a1 = new Athlete(
            List.of(TestRacesUtil.race1, TestRacesUtil.race4, TestRacesUtil.race5, TestRacesUtil.race6, TestRacesUtil.race7),
            "Jake Herrmann",
            2024,
            -1,
            true
        );

        File f = new File("AthleteTestPDF.pdf");
        a1.writeToPDF(f);

        assertTrue(f.exists());
        assertTrue(f.isFile());
        assertTrue(f.canRead());
    }

    @Test
    public void testMeetPDF() throws IOException, DocumentException{
        TestRacesUtil.race1.setMeetDate(null); //In real execution the meet information will be null for races owned by a meet object.
        TestRacesUtil.race2.setMeetDate(null);
        TestRacesUtil.race3.setMeetDate(null);
        TestRacesUtil.race4.setMeetDate(null);
        TestRacesUtil.race5.setMeetDate(null);

        Meet m1 = new Meet(
            List.of(TestRacesUtil.race1, TestRacesUtil.race2, TestRacesUtil.race3, TestRacesUtil.race4, TestRacesUtil.race5),
            "Conference 2023", 
            LocalDate.of(2023, 10, 14),
            40,
            27,
            55,
            6,
            -1,
            true
        );

        File f = new File("MeetTestPDF.pdf");
        m1.writeToPDF(f);

        assertTrue(f.exists());
        assertTrue(f.isFile());
        assertTrue(f.canRead());
    }

}
