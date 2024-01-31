package rrhs.xc.ia.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfFooter extends PdfPageEventHelper {
    
    private String footerText = "";
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
