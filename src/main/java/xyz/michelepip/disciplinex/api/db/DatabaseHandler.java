package xyz.michelepip.disciplinex.api.db;


import org.jetbrains.annotations.NotNull;
import xyz.michelepip.disciplinex.DisciplineX;

import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * This class is used to manage connections
 * to the MySQL Database.
 */

public class DatabaseHandler {

    /*
     * TODO: In Sprint 2, make database
     * credentials available to change in config.yml
     */

    private static final String host = "localhost";
    private static final int port = 3306;
    private static final String databaseName = "disciplinex";
    private static final String username = "root";
    private static final String password = "password";

    public static Connection createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + databaseName, username, password);
        } catch (SQLException e) {
            DisciplineX.severeError("The plugin has made an error to connect to the MySQL Database and has been shut down.\nPlease check your configuration.");
            DisciplineX.getInstance().shutdownPlugin();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public void createPunishmentTable() {
        final Connection dbConnection = createConnection();
        try {
            final PreparedStatement createQuery = dbConnection.prepareStatement("CREATE TABLE Punishments(" +
                    "");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Asynchronous ver. (checkmute)
    public static CompletableFuture<ResultSet> asyncIsPlayerMuted(final @NotNull UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            Connection conn = createConnection();
            try {
                PreparedStatement ps = conn.prepareStatement("");
                return ps.executeQuery();
            } catch (SQLException e) {
                DisciplineX.severeError("A severe error has encountered while querying the database. The plugin has been shut down.");
                DisciplineX.getInstance().shutdownPlugin();
                return null;
            }
        });
    }

    // Synchronous ver. (checkmute)
    public static boolean isPlayerMuted(final @NotNull UUID uuid) {
        boolean muted = false;
        Connection dbConnection = createConnection();

        try {
            final PreparedStatement checkPlayerQuery = dbConnection.prepareStatement("SELECT * ");
        } catch (SQLException e) {
            DisciplineX.severeError("A severe error has encountered while querying the database. The plugin has been shut down.");
            DisciplineX.getInstance().shutdownPlugin();
        }

        return muted;
    }
}
