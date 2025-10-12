package org.filipandrei.pss.model;

import org.filipandrei.pss.model.entities.*;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Collection;

public class SimulationModelImpl extends SimulationModel {
    World world;
    boolean ended;
    boolean running;

    private static String DB_URL = "jdbc:sqlite:database.db";

    @Override
    public void createModel(String simName) {

        int simId = 0; // TODO: ADAUGA IN BAZA DE DATE SIMULAREA CU NUMELE NAME, OBTINE INAPOI ID UL PE CARE TI L-A OFERIT AUTOMAT
        this.world = new World(simId, simName);
        this.ended = false;
        this.running = false;
        createDb();
    }

    @Override
    public void destroyModel() {
        this.world = null;
        this.ended = true;
        this.running = false;
    }

    @Override
    public boolean addEntity(@NotNull Entity e) {
        return world.registerEntity(e);
    }

    @Override
    public boolean addEntities(Collection<Entity> entities) {
        for (Entity e : entities) {
            if (!world.registerEntity(e)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void startSimulation() {
        this.running = true;
    }

    @Override
    public void pauseSimulation() {
        this.running = false;
    }

    @Override
    public void killSimulation() {
        this.running = false;
        world = null;
    }

    @Override
    public boolean loadSimulation(int simId) {
        return false;
    }

    @Override
    public void saveSimulation(String dbUrl) {
        try (Connection conn = DriverManager.getConnection(dbUrl)){
            conn.setAutoCommit(false);
            String sql = "INSERT OR IGNORE INTO simulations (name)" +
                    "VALUES( ?);";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, world.simulationName);
            pstmt.execute();

            for (var entity : world.entities.values()) {
                switch(entity) {
                    case Person p -> savePerson(conn, p);
                    case Building b -> saveBuilding(conn, b);
                    case Family f -> saveFamily(conn, f);
                    default -> { }
                }
            }
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Doesn't commit transaction
     * @param conn
     * @param p
     * @throws SQLException
     */
    private void savePerson(Connection conn, Person p) throws SQLException {
        String sql = "INSERT INTO persons(sim_id, id, maxHp, hp, x, y, age, infectionState, familyId, firstName)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" +
                "ON CONFLICT(sim_id, id) " +
                "    DO UPDATE SET " +
                "        maxHp = excluded.maxHp," +
                "        hp = excluded.hp," +
                "        x = excluded.x," +
                "        y = excluded.y," +
                "        age = excluded.age," +
                "        infectionState = excluded.infectionState," +
                "        familyId = excluded.familyId," +
                "        firstName = excluded.firstName;";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, world.simulationId);                // sim_id
            pstmt.setInt(2, p.id);                              // id
            pstmt.setInt(3, p.maxHp);                           // maxHp
            pstmt.setInt(4, p.hp);                              // hp
            pstmt.setDouble(5, p.pos.x);                        // x
            pstmt.setDouble(6, p.pos.y);                        // y
            pstmt.setString(7, p.ageToString());               // age
            pstmt.setString(8, p.infectionState.toString());    // infectionState
            pstmt.setInt(9, p.familyId);                        // familyId
            pstmt.setString(10, p.firstName);                   // firstName
            pstmt.execute();
        }
    }

    /**
     * Doesn't commit
     * @param conn
     * @param b
     * @throws SQLException
     */
    private void saveBuilding(Connection conn, Building b) throws SQLException {
        String sql = "INSERT INTO buildings(sim_id, id, x, y, w, h, type)" +
                "VALUES(?, ?, ?, ?, ?, ?, ?)" +
                "ON CONFLICT(sim_id, id)" +
                "DO UPDATE SET " +
                "x = excluded.x, " +
                "y = excluded.y, " +
                "w = excluded.w, " +
                "h = excluded.h," +
                "type = excluded.type;";
        try(var pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, world.simulationId);
            pstmt.setInt(2, b.id);
            pstmt.setDouble(3, b.pos.x);
            pstmt.setDouble(4, b.pos.y);
            pstmt.setDouble(5, b.dimensions.x);
            pstmt.setDouble(6, b.dimensions.y);
            pstmt.setString(7, b.type());
            pstmt.execute();
        }
    }


    /**
     * Doesn't commit
     * @param conn
     * @param f
     * @throws SQLException
     */
    private void saveFamily(Connection conn, Family f) throws SQLException {
        String sql = "INSERT INTO families(sim_id, family_id, member_id)" +
                "VALUES(?, ?, ?)" +
                "ON CONFLICT(sim_id, member_id)" +
                "DO UPDATE SET " +
                "family_id = excluded.family_id;";
        try (var pstmt = conn.prepareStatement(sql)) {
            for (var memberId : f.membersIds) {
                pstmt.setInt(1, world.simulationId);
                pstmt.setInt(2, f.id);
                pstmt.setInt(3, memberId);
                pstmt.addBatch();
            }
            pstmt.executeBatch(); // execute all at once
        }
    }

    @Override
    public void tick() {

    }

    @Override
    public World getWorld() {
        return world;
    }

    /**
     * Creates the db
     * If it already exists, it has no effect
     */
    private static void createDb() {
        try(Connection conn = DriverManager.getConnection(DB_URL)) {
            InputStream is = SimulationModelImpl.class.getClassLoader()
                    .getResourceAsStream("create_db.sql");
            if (is == null) {
                throw new FileNotFoundException("create_db.sql nu a fost găsit în resources");
            }
            String sql = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            Statement stmt = conn.createStatement();
            String[] instrs = sql.split(";");
            for (String instr : instrs) {
                if (!instr.trim().isEmpty()) {
                    stmt.execute(instr);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
