package org.filipandrei.pandemic.tests;

import org.filipandrei.pandemic.model.DefaultModel;
import org.filipandrei.pandemic.model.Model;
import org.filipandrei.pandemic.model.configs.Configs;
import org.filipandrei.pandemic.model.db.DataBaseUtils;
import org.filipandrei.pandemic.model.db.SimulationDao;
import org.filipandrei.pandemic.model.entities.ReadOnlySimulation;
import org.filipandrei.pandemic.model.entities.Simulation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class DefaultModelTest {

    Model uut;
    @BeforeEach
    void setup() throws SQLException {
        uut = new DefaultModel();
        if (DataBaseUtils.dbFileNotExist(Configs.get("db.url"))) {
            DataBaseUtils.createTables(Configs.get("db.url"));
        }
    }

    @AfterEach
    void cleanup() {
        uut = null;
        var dbFilePath = Path.of("test_database.db");
        if(Files.exists(dbFilePath) && !DataBaseUtils.dbFileNotExist(Configs.get("db.url"))) {
            try {
                Files.delete(dbFilePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }



    @Test
    void createAndReadActiveSimulation() throws SQLException {
        assertDoesNotThrow(() -> /* Act */ uut.createAndReadActiveSimulation("TEST SIM"));
        // Assert
        assertEquals("TEST SIM", uut.getReadOnlySimulation().getName());
        assertTrue(uut.getActiveSimulationId().isPresent());
        Simulation simulationCopy = new SimulationDao().getById(uut.getActiveSimulationId().get());
        assertNotNull(simulationCopy);
        assertEquals(uut.getReadOnlySimulation().getName(), simulationCopy.getName());
        assertEquals(uut.getReadOnlySimulation().getId(), simulationCopy.getId());
    }

    @Test
    void readActiveSimulation() throws SQLException {
        var dao = new SimulationDao();
        dao.save(new Simulation(1337, "readActiveSimulation", 1337));


        assertTrue(/* Act */ uut.readActiveSimulation(1337));
        // Assert
        assertEquals(1337, uut.getReadOnlySimulation().getId());
        assertEquals(1337, uut.getReadOnlySimulation().getEntityCount());
        assertEquals("readActiveSimulation", uut.getReadOnlySimulation().getName());
    }

    @Test
    void updateActiveSimulation_SavesUnregisteredSim() throws SQLException {
        try {
            Field f = uut.getClass().getDeclaredField("activeSim");
            f.setAccessible(true);
            f.set(uut, new Simulation(1234, "updateActiveSimulation_SavesUnregisteredSim", 4321));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("S-a schimbat numele campului activeSim!");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        // Act
        var status = uut.updateActiveSimulation();
        // Assert
        assertTrue(status);
        Simulation result = new SimulationDao().getById(1234);
        assertEquals(1234, result.getId());
        assertEquals("updateActiveSimulation_SavesUnregisteredSim", result.getName());
        assertEquals(4321, result.getEntityCount());
    }

    @Test
    void updateActiveSimulation_UpdateRow() throws SQLException {

        Simulation initial = new Simulation(7777, "updateActiveSimulation_UpdateRow");
        new SimulationDao().save(initial);
        var expected = new Simulation(7777, "Crazy easter egg", 4321);
        try {
            Field f = uut.getClass().getDeclaredField("activeSim");
            f.setAccessible(true);
            f.set(uut, expected);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("S-a schimbat numele campului activeSim!");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        // Act
        var result = uut.updateActiveSimulation();

        Simulation actual = new SimulationDao().getById(7777);

        // Assert
        assertTrue(result);
        assertEquals(expected.getName(), actual.getName());
        assertNotEquals(expected.getName(), initial.getName());
        assertEquals(expected.getEntityCount(), actual.getEntityCount());
    }

    @Test
    void deleteSimulation_DeleteSimulation_DeleteRowSuccesfully() throws SQLException {
        // ARRANGE

        new SimulationDao().save(new Simulation(92, "test"));

        // ACT
        uut.deleteSimulation(92);

        // ASSERT
        assertNull(new SimulationDao().getById(92));
    }

    @Test
    void getActiveSimulationId_ActiveSimulationIsSet_OptionalHasValue() {
        // Arrange
        var expected = new Simulation(2004, "a");
        try {
            Field f = uut.getClass().getDeclaredField("activeSim");
            f.setAccessible(true);
            f.set(uut, expected);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("numele campului Simulation.activeSim a fost schimbat!");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        // Act
        var actual = uut.getActiveSimulationId();

        // Assert
        assertTrue(actual.isPresent());
        assertEquals(2004, actual.get());
    }

    @Test
    void getActiveSimulationId_ActiveSimulationNotSet_OptionalHasNoValue() {
        assertFalse(uut.getActiveSimulationId().isPresent());
        assertThrows(NoSuchElementException.class, () -> uut.getActiveSimulationId().get());
    }



    @Test
    void getReadOnlySimulation_SimSet_ReturnsValid() {
        // Arrange
        var expected = new Simulation(200, "Millenium Falcon");
        try {
            Field f = uut.getClass().getDeclaredField("activeSim");
            f.setAccessible(true);
            f.set(uut, expected);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("numele campului Simulation.activeSim a fost schimbat!");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        // Act
        ReadOnlySimulation actual = uut.getReadOnlySimulation();
        assertInstanceOf(Simulation.class, actual);
        assertEquals(expected.getSimId(), actual.getSimId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected, actual);
        assertSame(expected, actual);
    }

    @Test
    void setActiveSimulationName() {
        var sim = new Simulation(200, "Luke Skywalker");
        try {
            Field f = uut.getClass().getDeclaredField("activeSim");
            f.setAccessible(true);
            f.set(uut, sim);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("numele campului Simulation.activeSim a fost schimbat!");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        uut.setActiveSimulationName("Darth Schnitzel");

        assertEquals("Darth Schnitzel", sim.getName());
        assertNotEquals("Luke Skywalker", sim.getName());
    }

    @Test
    void getSimulationsNames_SimulationsInDb_ShouldReturnNames() {
        // Arrange
        var expected1 = new Simulation(1000, "Simularea 1000");
        var expected2 = new Simulation(2000, "Simularea 2000");
        var expected3 = new Simulation(3000, "Simularea 3000", 67);

        var dao = new SimulationDao();
        dao.save(expected1);
        dao.save(expected2);
        dao.save(expected3);

        // Act
        var result = uut.getSimulationsNames();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains(expected1.getName()));
        assertTrue(result.contains(expected2.getName()));
        assertTrue(result.contains(expected3.getName()));
    }

    @Test
    void getSimulationsNames_NoSimulations_ShouldReturnEmptyCollection() {

        var result = uut.getSimulationsNames();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    @Test
    void getReadOnlySimulation_NoSimSet_ReturnsNull() {
        assertNull(uut.getReadOnlySimulation());
    }

    @Test
    void endAndSaveActiveSim() {
        var expected = new Simulation(4, "NAME", 32);
        try {
            Field f = uut.getClass().getDeclaredField("activeSim");
            f.setAccessible(true);
            f.set(uut, expected);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("numele campului Simulation.activeSim a fost schimbat!");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        uut.endAndSaveActiveSim();

        var saved = new SimulationDao().getById(4);
        assertNotNull(saved);
        assertEquals(expected.getId(), saved.getId());
        assertEquals(expected.getName(), saved.getName());
        assertEquals(expected.getEntityCount(), saved.getEntityCount());
    }
}