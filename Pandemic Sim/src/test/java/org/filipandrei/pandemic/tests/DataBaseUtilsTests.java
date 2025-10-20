package org.filipandrei.pandemic.tests;

import org.filipandrei.pandemic.model.configs.Configs;
import org.filipandrei.pandemic.model.db.DataBaseUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class DataBaseUtilsTests {

    @AfterEach
    void cleanup() {
        var dbFilePath = Path.of("test_database.db");
        if(Files.exists(dbFilePath)) {
            try {
                Files.delete(dbFilePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Test
    void createTables() {
        String dbUrl = Configs.get("db.url");
        Assertions.assertDoesNotThrow( () -> DataBaseUtils.createTables(dbUrl));
        String sqlTest = "SELECT name \n" +
                "FROM sqlite_master \n" +
                "WHERE type='table';";
        try (
            Connection c = DriverManager.getConnection(dbUrl);
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sqlTest);
        ) {
            Set<String> expectedTables = new HashSet<>(Arrays.asList("simulations",
                    "sqlite_sequence",
                    "vehicles",
                    "persons",
                    "families",
                    "family_members"));
            Set<String> actualTables = new HashSet<>();
            while (rs.next()) {
                actualTables.add(rs.getString("name"));
            }
            assertEquals(expectedTables, actualTables);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testReadFromDbWithMock() throws SQLException {
        PreparedStatement stmt = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(stmt.executeQuery()).thenReturn(rs);

        Optional<ResultSet> result = DataBaseUtils.readFromDb(stmt);

        assertTrue(result.isPresent());
        assertEquals(rs, result.get());

        verify(stmt, times(1)).executeQuery();
    }
}
