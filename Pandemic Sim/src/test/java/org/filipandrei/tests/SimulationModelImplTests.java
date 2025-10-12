package org.filipandrei.tests;

import org.filipandrei.pss.model.SimulationModelImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class SimulationModelImplTests {

    private String DB_FILE = "testdatabase.db";
    private String DB_URL = "jdbc:sqlite:file:" + DB_FILE;

    @AfterEach
    void cleanup() {
        File dbFile = new File(DB_FILE);
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }

    @Test
    void testDbCreation() throws NoSuchMethodException {
        Method mut = SimulationModelImpl.class.getDeclaredMethod("createDb");
        mut.setAccessible(true);
        assertDoesNotThrow( () -> mut.invoke(null));
    }

    @Test
    void testSimSave() throws Exception {
        // Create the instance
        SimulationModelImpl uut = new SimulationModelImpl();

        // Access the static final field
        Field field = SimulationModelImpl.class.getDeclaredField("DB_URL");
        field.setAccessible(true);

        // Set new value
        field.set(null, DB_URL); // DB_URL is your test DB URL

        // Now you can call methods that use DB_URL
        uut.createModel("Test sim");
        Method mut = SimulationModelImpl.class.getDeclaredMethod("saveSimulation", String.class);
        mut.setAccessible(true);
        assertDoesNotThrow(() -> mut.invoke(uut, DB_URL));

        // Check DB
        Connection conn = DriverManager.getConnection(DB_URL);
        String sql = "SELECT sim_id FROM simulations WHERE name == \"Test sim\";";
        Statement stmt = conn.createStatement();
        var rs = stmt.executeQuery(sql);
        assertTrue(rs.next());
        assertTrue( rs.getInt("sim_id") > 0);
        sql = "SELECT name FROM simulations WHERE sim_id == 1";
        rs = stmt.executeQuery(sql);
        assertTrue(rs.next());
        assertEquals("Test sim", rs.getString("name"));
        conn.close();
    }
}
