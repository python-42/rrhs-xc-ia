package rrhs.xc.ia.data.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.Test;

import rrhs.xc.ia.util.StringUtils;

public class StringUtilsTest {

    @Test
    public void testRemoveFileExt() {
        assertEquals("file", StringUtils.removeFilExtension(new File("file.fxml")));

        assertEquals("cool file!", StringUtils.removeFilExtension(new File("cool file!.txt")));

        assertEquals("i.use.dots", StringUtils.removeFilExtension(new File("i.use.dots.png")));
    }

    @Test
    public void testFormatTime() {
        assertEquals("17:40.50", StringUtils.formatTime(1060.5));
        
        assertEquals("19:23.72", StringUtils.formatTime(1163.72));
    }
    
    @Test
    public void testDeFormatTime() {
        assertEquals(1060.5, StringUtils.deFormatTime("17:40.50"));
    }
}