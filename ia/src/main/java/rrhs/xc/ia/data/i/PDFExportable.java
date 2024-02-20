package rrhs.xc.ia.data.i;

import java.io.File;
import java.io.IOException;

import org.controlsfx.control.Notifications;

import com.itextpdf.text.DocumentException;

import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Window;

public interface PDFExportable {
    public void writeToPDF (File file) throws IOException, DocumentException;

        public static void export(String filename, String popupTitle, PDFExportable export) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(popupTitle);
        chooser.setInitialFileName(filename);
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        try {
            File f = chooser.showSaveDialog(Window.getWindows().get(0));
            if (f != null) {
                export.writeToPDF(f);
            }
        } catch (DocumentException | IOException e) {
            Notifications.create()
            .title("Error Ocurred")
            .text("An error occurred while trying to export the PDF: " + e.getMessage() + "\n Please try again.")
            .position(Pos.TOP_RIGHT)
            .showError();

            e.printStackTrace();
        }
    }
}
