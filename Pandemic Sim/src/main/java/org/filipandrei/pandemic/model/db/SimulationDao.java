package org.filipandrei.pandemic.model.db;

import org.filipandrei.pandemic.model.configs.Configs;
import org.filipandrei.pandemic.model.entities.Entity;
import org.filipandrei.pandemic.model.entities.Simulation;
import org.jetbrains.annotations.Contract;

import java.sql.*;
import java.util.*;

public class SimulationDao implements DataAccessObject<Simulation> {

    private static final String url = Configs.get("db.url");

    public SimulationDao() {

    }

    /**
     *
     * @param
     * @return {@code true} on succes, {@code false} otherwise
     */
    @Override
    public boolean save(Simulation simulation) {
        if (!validateSimulation(simulation)) {
            return false;
        }
        try (
            var con = DataBaseUtils.createDbConnection(url);
            PreparedStatement stmt = con.prepareStatement("INSERT OR REPLACE INTO simulations (id, name, entity_number) VALUES (?, ?, ?);")
        ) {
            stmt.setInt(1, simulation.getId());
            stmt.setString(2, simulation.getName());
            stmt.setInt(3, simulation.getEntityCount());
            stmt.execute();
            con.commit();
        } catch (SQLException ex) {
            return false;
        }
        return true;

    }

    @Override
    public void deleteById(int id) {
        try (
            var con = DataBaseUtils.createDbConnection(url);
            PreparedStatement stmt = con.prepareStatement("DELETE FROM simulations WHERE id = ?;")
        ) {
            stmt.setInt(1, id);
            stmt.execute();
            con.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Creates a new simulation in the "simulations" table.
     * <p>
     * This method inserts a new row with the given {@code name} into the table.
     * It returns the {@code id} of the newly created simulation if successful.
     * </p>
     *
     * @param name the name of the new simulation
     * @return the ID of the newly created simulation, or -1 if insertion failed
     * @throws RuntimeException if a database access error occurs or multiple rows are affected unexpectedly
     */
    public int createNewSimulation(String name) {
        String insertSql = "INSERT INTO simulations(name) VALUES (?);";
        String selectSql = "SELECT id FROM simulations WHERE name = ? AND entity_number = 0;";

        try {
            DataBaseUtils.createTablesIfNotExists(url);
        } catch (SQLException e) {
            return -1;
        }

        try (var con = DataBaseUtils.createDbConnection(url);
             var insertStmt = con.prepareStatement(insertSql);
        ) {

            insertStmt.setString(1, name);
            DataBaseUtils.writeToDb(con, insertStmt, false);

            // Fetch the ID of the newly inserted simulation
            try (var selectStmt = con.prepareStatement(selectSql)) {
                selectStmt.setString(1, name);
                try (ResultSet rs = DataBaseUtils.readFromDb(selectStmt).orElseThrow()) {
                    if (rs.next()) {
                        int resId = rs.getInt("id");
                        assert resId >= 0;
                        return resId;
                    } else {
                        return -1; // row not found
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return -1;
        }
    }


    /**
     * Retrieves a {@link Simulation} domain object from the database by its ID.
     * <p>
     * This method establishes a connection to the database using the configured URL
     * and executes a SQL script to fetch the simulation with the specified {@code id}.
     * If no simulation exists with the given ID, the method returns {@code null}.
     * </p>
     *
     * @param id the ID of the simulation to retrieve from the database
     * @return the {@link Simulation} object corresponding to the given ID,
     *         or {@code null} if no such simulation exists in the database
     * @throws RuntimeException if the SQL script cannot be found at {@code sql/get_sim_by_id.sql} or cannot be read
     */
    @Override
    public Simulation getById(int id) {
        try (Connection c = DataBaseUtils.createDbConnection(url);
        ) {
            String sql = DataBaseUtils.getSqlFromScript("/sql/get_sim_by_id.sql");
            var stmt = c.prepareStatement(sql);
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rsToSim(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves a {@link java.util.Collection<Simulation>} with domain objects representing simulations from the database.
     * <p>
     * If no simulations exist, returns an empty collection.
     * </p>
     *
     * @return a {@link java.util.Collection<Simulation>} object with domain objects representing simulations from database
     * @throws RuntimeException if the SQL script cannot be found at {@code sql/get_all_sims.sql} or cannot be read
     */
    @Override
    public Collection<Simulation> getAll() {
        Set<Simulation> res = new HashSet<>();
        try (Connection c = DataBaseUtils.createDbConnection(url);
             Statement stmt = c.createStatement()) {

            String sql = DataBaseUtils.getSqlFromScript("/sql/get_all_sims.sql");
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    res.add(rsToSim(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     *
     * @param rs IMPORTANT: {@link ResultSet} object must contain all columns to work! The object is created from only the first result
     * @return a {@link Simulation} object representing the first row of the {@link ResultSet}, or {@code null} if the {@link ResultSet} is empty.
     * @throws SQLException If the {@link ResultSet} doesn't have all the columns.
     */
    private Simulation rsToSim(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int entityCount = rs.getInt("entity_number");
        String name = rs.getString("name");
        return new Simulation(id, name, entityCount);
    }

    /**
     *
     * @param id
     * @param name
     * @return {@code true} if successfully, {@code false} if no row was modified
     */
    public boolean setNameToId(int id, String name) {
        try (Connection conn = DataBaseUtils.createDbConnection(url);
            PreparedStatement stmt = conn.prepareStatement(    "INSERT INTO simulations(id, name) VALUES (?, ?) " +
                    "ON CONFLICT(id) DO UPDATE SET name = excluded.name");) {
            stmt.setInt(1, id);
            stmt.setString(2, name);
            int affectedRows = DataBaseUtils.writeToDb(conn, stmt, true);
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validateSimulation(Simulation simulation) {
        return simulation.getId() >= 0 && simulation.getName() != null && !simulation.getName().isEmpty() && simulation.getEntityCount() >= 0;
    }
}
