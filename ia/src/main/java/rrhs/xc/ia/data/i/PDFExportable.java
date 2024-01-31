package rrhs.xc.ia.data.i;

import java.io.File;
import java.io.IOException;

import com.itextpdf.text.DocumentException;

public interface PDFExportable {
    public void writeToPDF (File file) throws IOException, DocumentException;   
}
