package org.filipandrei.pandemic.tests;

import org.filipandrei.pandemic.model.configs.Configs;
import org.filipandrei.pandemic.model.db.DataBaseUtils;
import org.filipandrei.pandemic.model.db.SimulationDao;
import org.filipandrei.pandemic.model.entities.Simulation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SimulationDaoTest {

    @BeforeEach
    void setup() throws SQLException {
        if (DataBaseUtils.dbFileNotExist(Configs.get("db.url"))) {
            DataBaseUtils.createTables(Configs.get("db.url"));
        }
    }

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
        void saveReturnsFalseWhenSimulationIsInvalid() {
            SimulationDao dao = new SimulationDao();
            Simulation invalidSimulation = new Simulation(-1, "", -5);

            assertFalse(dao.save(invalidSimulation));
        }

        @Test
        void saveReturnsTrueWhenSimulationIsValid() {
            SimulationDao dao = new SimulationDao();
            Simulation validSimulation = new Simulation(1, "Valid Simulation", 10);

            assertTrue(dao.save(validSimulation));
        }

        @Test
        void createNewSimulationReturnsMinusOneWhenNameIsNull() {
            SimulationDao dao = new SimulationDao();

            int id = dao.createNewSimulation(null);

            assertEquals(-1, id);
        }

        @Test
        void getByIdReturnsNullWhenIdIsNegative() {
            SimulationDao dao = new SimulationDao();

            assertNull(dao.getById(-1));
        }

        @Test
        void setNameToIdThrowsRuntimeExceptionWhenNameIsNull() {
            SimulationDao dao = new SimulationDao();
            int id = dao.createNewSimulation("Simulation");

            assertThrows(RuntimeException.class, () -> dao.setNameToId(id, null));
        }
}