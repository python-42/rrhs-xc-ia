package rrhs.xc.ia.util;

import java.time.LocalDate;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

public class PdfFooter extends PdfPageEventHelper {
    
    private String footerText = "Generated on " + LocalDate.now().toString() + " by JCrossCountry Tracker. Go Huskies!";
    private int x;
    private int y;

    public PdfFooter(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String getFooterText() {
        return footerText;
    }

    public void setFooterText(String text) {
        footerText = text;
    }

    public void setFooterPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document doc) {
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(footerText), x, y, 0);
    }
}
