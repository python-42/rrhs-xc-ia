package rrhs.xc.ia.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import rrhs.xc.ia.util.StringUtils;

public class StringUtilsTest {

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
