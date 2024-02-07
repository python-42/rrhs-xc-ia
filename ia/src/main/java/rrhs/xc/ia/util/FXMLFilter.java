package rrhs.xc.ia.util;

import java.io.File;
import java.io.FileFilter;

public class FXMLFilter implements FileFilter {

    @Override
    public boolean accept(File arg0) {
        return arg0.isFile() && arg0.canRead() && fileExtensionIsFXML(arg0);
    }

    private boolean fileExtensionIsFXML(File f) {
        String s = f.getName();
        return s.substring(s.lastIndexOf('.')).toLowerCase().equals(".fxml");
    }
    
}
