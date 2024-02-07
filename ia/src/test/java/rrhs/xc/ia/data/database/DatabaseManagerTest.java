package rrhs.xc.ia.data.database;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.SQLException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

@TestMethodOrder(OrderAnnotation.class)
public class DatabaseManagerTest {
    
    private static DatabaseManager db;

    @BeforeAll
    public static void setup() {
        db = new DatabaseManager(":memory:");
    }

    @Test
    @Order(1)
    public void testConnection() throws SQLException {
        db.connect();
    }

    @Test
    @Order(2)
    public void testCreateDB() throws SQLException {
        db.createDatabase();
    }

    @Test
    @Order(3)
    public void testInvalidRequests() throws SQLException {
        assertEquals(null, db.getAthlete("not real"));
        assertEquals(null, db.getMeet("does not exist"));
    }

}
