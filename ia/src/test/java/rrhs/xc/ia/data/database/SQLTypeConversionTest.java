package rrhs.xc.ia.data.database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import rrhs.xc.ia.data.mem.Season;

public class SQLTypeConversionTest {
    
    @Test
    public void testSeason() {
        assertEquals("'0'", SQLTypeConversion.convert(Season.FRESHMAN));
        assertEquals("'1'", SQLTypeConversion.convert(Season.SOPHOMORE));
        assertEquals("'2'", SQLTypeConversion.convert(Season.JUNIOR));
        assertEquals("'3'", SQLTypeConversion.convert(Season.SENIOR));

        assertEquals(Season.FRESHMAN, SQLTypeConversion.getSeason("'0'"));
        assertEquals(Season.SOPHOMORE, SQLTypeConversion.getSeason("'1'"));
        assertEquals(Season.JUNIOR, SQLTypeConversion.getSeason("'2'"));
        assertEquals(Season.SENIOR, SQLTypeConversion.getSeason("'3'"));
        assertEquals(null, SQLTypeConversion.getSeason("'8'"));
    }

    @Test
    public void testInt() {
        assertEquals("'123'", SQLTypeConversion.convert(123));
        assertEquals("'-42'", SQLTypeConversion.convert(-42));

        assertEquals(190, SQLTypeConversion.getInt("'190'"));
        assertEquals(-67, SQLTypeConversion.getInt("'-67'"));
        assertEquals(86, SQLTypeConversion.getInt("' 86    '"));
        assertEquals(9, SQLTypeConversion.getInt("'09'"));

        assertEquals(0, SQLTypeConversion.getInt("'0'"));
    }

    @Test
    public void testDouble() {
        assertEquals("'1.23'", SQLTypeConversion.convert(1.23));
        assertEquals("'-4.2'", SQLTypeConversion.convert(-4.2));
        assertEquals("'-0.2'", SQLTypeConversion.convert(-.2));

        assertEquals(19.0, SQLTypeConversion.getDouble("'19.0'"));
        assertEquals(-0.67, SQLTypeConversion.getDouble("'-.67'"));
        assertEquals(86.0, SQLTypeConversion.getDouble("' 86    '"));
        assertEquals(8.6, SQLTypeConversion.getDouble("' 8.6    '"));
        assertEquals(9.0, SQLTypeConversion.getDouble("'09'"));
        assertEquals(9.5, SQLTypeConversion.getDouble("'09.5'"));

        assertEquals(0.0, SQLTypeConversion.getDouble("'0'"));
    }

    @Test
    public void testBoolean() {
        assertEquals("'0'", SQLTypeConversion.convert(false));
        assertEquals("'1'", SQLTypeConversion.convert(true));

        assertTrue(SQLTypeConversion.getBoolean("'1'"));
        assertFalse(SQLTypeConversion.getBoolean("'0'"));
    }

    @Test
    public void testString() {
        assertEquals("'Jake Herrmann'", SQLTypeConversion.convert("Jake Herrmann"));

        assertEquals("Jake' Herrmann", SQLTypeConversion.getString("'Jake' Herrmann'"));
    }

    @Test
    public void testLocalDate() {
        assertEquals("'2024-01-01'", SQLTypeConversion.convert(LocalDate.of(2024, 1, 1)));
        assertEquals("'2023-12-31'", SQLTypeConversion.convert(LocalDate.of(2023, 12, 31)));

        assertEquals(LocalDate.of(1970, 1, 1), SQLTypeConversion.getDate("'1970-01-01'"));
        assertEquals(LocalDate.of(1894, 11, 19), SQLTypeConversion.getDate("'1894-11-19'"));
    }

}
