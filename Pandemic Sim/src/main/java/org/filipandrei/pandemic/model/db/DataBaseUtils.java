package org.filipandrei.pandemic.model.db;

import org.filipandrei.pandemic.model.configs.Configs;
import org.filipandrei.pandemic.model.configs.Paths;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public final class DataBaseUtils {
    private DataBaseUtils() {

    }

    /**
     * Establishes a new connection to the configured database.
     * <p>
     * The returned {@link java.sql.Connection} has auto-commit mode disabled
     * via {@link java.sql.Connection#setAutoCommit(boolean)}.
     * You must explicitly call {@link java.sql.Connection#commit()} after executing your statements
     * to persist the changes.
     * </p>
     *
     * @return a new {@link java.sql.Connection} instance with auto-commit disabled
     * @throws java.sql.SQLException if a database access error occurs or the connection cannot be established
     */
    public static Connection createDbConnection(String dbUrl) throws SQLException {
        var conn = DriverManager.getConnection(dbUrl);
        conn.setAutoCommit(false);
        return conn;
    }

    private static final Object writeLock = new Object();

    /**
     * Executes a write operation (INSERT, UPDATE, DELETE) on the database in a synchronized manner.
     * <p>
     * Only one thread can perform write operations at a time.
     * The method commits changes after successful execution.
     * The {@link PreparedStatement} is always closed. The {@link Connection} is optionally closed
     * depending on the <code>closeConn</code> parameter.
     * </p>
     *
     * @param conn      the database connection, should be created via {@link #createDbConnection(String)}
     * @param stmt      the {@link PreparedStatement} to execute
     * @param closeConn if true, the connection will be closed after execution; otherwise it remains open
     * @return the number of rows affected by the operation
     * @throws SQLException              if a database access error occurs
     * @throws IllegalArgumentException  if the statement execution returns a ResultSet (failed write or read attempt)
     */
    public static int writeToDb(@NotNull Connection conn, @NotNull PreparedStatement stmt, boolean closeConn) throws SQLException {
        synchronized (writeLock) {
            try (stmt) {
                return executeAndCommit(conn, stmt);
            } finally {
                if (closeConn) {
                    conn.close();
                }
            }
        }
    }

    /**
     * Executes a read operation (SELECT) on the database.
     * <p>
     * The method returns an {@link Optional} containing the {@link ResultSet} if the query
     * returns results, or an empty {@link Optional} if no results are found.
     * The caller is responsible for closing the {@link ResultSet} and the associated
     * {@link PreparedStatement} and {@link Connection}.
     * </p>
     *
     * @param stmt the {@link PreparedStatement} to execute
     * @return an {@link Optional} containing the {@link ResultSet} if results are found, or empty if no results
     * @throws SQLException if a database access error occurs
     */
    public static Optional<ResultSet> readFromDb(@NotNull PreparedStatement stmt) throws SQLException {
        ResultSet rs = stmt.executeQuery();
        return rs != null ? Optional.of(rs) : Optional.empty();
    }

    /** Internal helper to execute statement and commit */
    private static int executeAndCommit(Connection conn, PreparedStatement stmt) throws SQLException {
        if (stmt.execute()) { // returned ResultSet inseamna invalid write
            throw new IllegalArgumentException("Failed to execute write operation to database.");
        }
        conn.commit();
        return stmt.getUpdateCount();
    }

    public synchronized static void createTables(String dbUrl) throws SQLException {
        try (Connection conn = createDbConnection(dbUrl)) {
            String sql = getSqlFromScript(Paths.createTablesSqlScript);
            DataBaseUtils.prepareStatements(conn, sql)
                    .forEach(ps -> {
                        try {
                            writeToDb(conn, ps, false);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
            conn.commit();
        }
    }

    public static boolean dbFileNotExist(String url) {
        if (url == null || !url.contains(":")) {
            throw new IllegalArgumentException("url is not a valid url");
        }
        var temp = url.split(":");
        var path = temp[temp.length-1];
        File file = new File(path);
        return !file.exists() || file.isDirectory();
    }

    public static String getSqlFromScript(String path) {
        byte[] bytes;
        try {
            bytes = DataBaseUtils.class.getResourceAsStream(path).readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static void createTablesIfNotExists(String url) throws SQLException {
        synchronized (writeLock) {
            if (DataBaseUtils.dbFileNotExist(Configs.get("db.url"))) {
                // Database does not exist, create tables
                DataBaseUtils.createTables(Configs.get("db.url"));
            }
        }
    }

    private static Stream<PreparedStatement> prepareStatements(Connection conn, String sql) throws SQLException {
        String[] statements = sql.split(";");
        return Arrays.stream(statements)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> {
                    try {
                        return conn.prepareStatement(s);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
