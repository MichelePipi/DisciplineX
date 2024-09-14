package xyz.nameredacted.disciplinex.api.db;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
import xyz.nameredacted.disciplinex.DisciplineX;
import xyz.nameredacted.disciplinex.api.Punishment;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * This class is used to handle the database
 * connection and to perform queries on the database.
 */
public class DatabaseHandler {

    /*
     * TODO: In Sprint 2, make database
     * credentials available to change in config.yml
     */

    private String host;
    private int port;
    private String databaseName;
    private String username;
    private String password;
    private final DisciplineX instance = DisciplineX.getInstance();

//    private static final String host = "localhost";
//    private static final int port = 3306;
//    private static final String databaseName = "disciplinex";
//    private static final String username = "disciplinex";
//    private static final String password = "password";

    /**
     * Retrieve db credentials from config.yml
     */
    public DatabaseHandler() {
        refreshCredentials();
    }


    public void refreshCredentials() {
        host = instance.getConfig().getString("database.host");
        port = instance.getConfig().getInt("database.port");
        databaseName = instance.getConfig().getString("database.database");
        username = instance.getConfig().getString("database.user");
        password = instance.getConfig().getString("database.passphrase");
    }

    /**
     * This function is used to connect to the database.
     * TODO: In Sprint 2, make database credentials available to change in config.yml
     * @return
     */
    private Connection createConnection() {
        System.out.println("Database Credentials");
        System.out.println(host);
        System.out.println(port);
        System.out.println(databaseName);
        System.out.println(username);
        System.out.println(password);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + databaseName, username, password);
        } catch (SQLException e) {
            for (int i = 0; i < 3; i++) {
                DisciplineX.severeError("The plugin has made an error to connect to the MySQL Database and has been shut down." +
                        "\nPlease check your configuration. This is most likely due to incorrect credentials.");
            }
//            e.printStackTrace();
            DisciplineX.getInstance().shutdownPlugin();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    public void setupDatabase() {
        createPlayersTable();
        createActivePunishmentTable();
        createPunishmentHistoryTable();
    }

    /**
     * This function creates the players
     * table for the database. Database Schema:
     * <pre>
     *     {@code
     *     player_id INT	PRIMARY KEY, AUTO_INCREMENT
     *     uuid	VARCHAR(36)	UNIQUE, NOT NULL
     *     name	VARCHAR(16)	NOT NULL
     *     }
     * </pre>
     *
     * This function is run synchronously as it is run once when the plugin is enabled.
     */
    @TestOnly
    private void createPlayersTable() {
        Connection conn = createConnection();
        try {
            final PreparedStatement createTable = conn.prepareStatement("CREATE TABLE IF NOT EXISTS players (" +
                    "player_id INT PRIMARY KEY AUTO_INCREMENT," +
                    "uuid VARCHAR(36) UNIQUE NOT NULL," +
                    "name VARCHAR(16) NOT NULL);");
            createTable.execute();
        } catch (SQLException e) {
            DisciplineX.severeError("A severe error has encountered while creating the players table. The plugin has been shut down.");
            DisciplineX.getInstance().shutdownPlugin();
        }
    }

    /**
     * This function creates the players
     * table for the database. Database Schema:
     * <pre>
     *     {@code
     *     punishment_id	INT	PRIMARY KEY, AUTO_INCREMENT
     *     player_id	INT	FOREIGN KEY REFERENCES players(player_id)
     *     punisher_id	INT	FOREIGN KEY REFERENCES players(player_id)
     *     type	ENUM	('BAN', 'MUTE', 'KICK', 'WARN'), NOT NULL
     *     reason	TEXT
     *     start_date	DATETIME	NOT NULL
     *     expiry_date	DATETIME
     *     expiry_date_actual	DATETIME
     *     }
     * </pre>
     *
     * This function is run synchronously as it is run once when the plugin is enabled.
     */
    @TestOnly
    private void createActivePunishmentTable() {
        Connection conn = createConnection();
        try {
            final PreparedStatement createTable = conn.prepareStatement("CREATE TABLE IF NOT EXISTS active_punishments (" +
                    "punishment_id INT PRIMARY KEY AUTO_INCREMENT," +
                    "player_id VARCHAR(36), " +
                    "FOREIGN KEY (player_id) REFERENCES players(uuid)," +
                    "punisher_id VARCHAR(36), " +
                    "FOREIGN KEY (punisher_id) REFERENCES players(uuid)," +
                    "punishment_type ENUM('BAN', 'MUTE', 'KICK', 'WARN') NOT NULL," +
                    "reason TEXT," +
                    "start_date DATETIME NOT NULL," +
                    "expiry_date DATETIME)");
            /**
             * Old query: Issue (11-09-2024)
             * "CREATE TABLE IF NOT EXISTS active_punishments (" +
             *                     "punishment_id INT PRIMARY KEY AUTO_INCREMENT," +
             *                     "player_id INT FOREIGN KEY (player_id) REFERENCES players(player_id)," +
             *                     "punisher_id INT FOREIGN KEY (punisher_id) REFERENCES players(player_id)," +
             *                     "punishment_type ENUM('BAN', 'MUTE', 'KICK', 'WARN') NOT NULL," +
             *                     "reason TEXT," +
             *                     "start_date DATETIME NOT NULL," +
             *                     "expiry_date DATETIME," +
             *                     "expiry_date_actual DATETIME);");
             */
            createTable.execute();
        } catch (SQLException e) {
            DisciplineX.severeError("A severe error has encountered while executing a database query. The plugin has been shut down.");
            e.printStackTrace();
            DisciplineX.getInstance().shutdownPlugin();
        }
    }

    /**
     * This function creates the players
     * table for the database. Database Schema:
     * <pre>
     *     {@code
     *     id	INT	PRIMARY KEY, AUTO_INCREMENT
     *     punishment_id	INT	FOREIGN KEY REFERENCES punishments(punishment_id)
     *     action	ENUM	('CREATED', 'EXPIRED', 'LIFTED'), NOT NULL
     *     timestamp	DATETIME	NOT NULL
     *     }
     * </pre>
     *
     * This function is run synchronously as it is run once when the plugin is enabled.
     */
    @TestOnly
    private void createPunishmentHistoryTable() {
        Connection conn = createConnection();
        try {
            final PreparedStatement createTable = conn.prepareStatement("CREATE TABLE IF NOT EXISTS punishment_history (" +
                    "punishment_id INT, " +
                    "FOREIGN KEY (punishment_id) REFERENCES active_punishments(punishment_id)," +
                    "action ENUM('CREATED', 'EXPIRED', 'LIFTED') NOT NULL," +
                    "timestamp DATETIME NOT NULL);");
            createTable.execute();
            /**
             * Old query:
             * "CREATE TABLE IF NOT EXISTS punishment_history (" +
             *                     "id INT PRIMARY KEY AUTO_INCREMENT," +
             *                     "punishment_id INT FOREIGN KEY (punishment_id) REFERENCES active_punishments(punishment_id)," +
             *                     "action ENUM('CREATED', 'EXPIRED', 'LIFTED') NOT NULL," +
             *                     "timestamp DATETIME NOT NULL);");
             */
        } catch (SQLException e) {
            DisciplineX.severeError("A severe error has encountered while creating the players table. The plugin has been shut down.");
            e.printStackTrace();
            DisciplineX.getInstance().shutdownPlugin();
        }
    }

    // Asynchronous ver. (checkmute)
    @TestOnly
    public CompletableFuture<ResultSet> asyncIsPlayerMuted(final @NotNull UUID uuid) {
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
    public boolean isPlayerMuted(final @NotNull UUID uuid) {
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

    // Insert punishment into database (synchronous) (permanent)

    /**
     * This method is used to insert a punishment into the database.
     * This method is synchronous. In the future (iteration 3) it will
     * be converted to be an asynchronous implementation.
     * @param punishment punishment object to insert into database
     */
    public void insertPunishment(final @NotNull Punishment punishment) {
        Connection conn = createConnection();
        try {
            final PreparedStatement insertPunishment = conn.prepareStatement("INSERT INTO active_punishments (player_id, punisher_id, punishment_type, reason, start_date, expiry_date) VALUES (?, ?, ?, ?, ?, ?);");
            insertPunishment.setString(1, punishment.getPlayerPunished());
            insertPunishment.setString(2, punishment.getBlame());
            insertPunishment.setString(3, punishment.getPunishmentType().toString());
//            insertPunishment.setString(4, punishment.getReason());
            insertPunishment.setTimestamp(5, new Timestamp(punishment.getOrigin().getTime()));
            // If the punishment does not have an expiry, it is permanent.
            if (punishment.getExpiry() == null) {
                // insert date with year 9999, so it never expires.
                insertPunishment.setTimestamp(6, new Timestamp(253402214400000L));
            } else // If the punishment has an expiry, insert the expiry date.
                insertPunishment.setTimestamp(6, new Timestamp(punishment.getExpiry().getTime()));
            if (punishment.getReason() == null) { // If there is no reason provided
                insertPunishment.setString(4, "No reason provided.");
            } else
                insertPunishment.setString(4, punishment.getReason());

            insertPunishment.execute();
            //            insertPunishment.setInt(1, punishment.getPlayerId());
//            insertPunishment.setInt(2, punishment.getPunisherId());
//            insertPunishment.setString(3, punishment.getPunishmentType().toString());
//            insertPunishment.setString(4, punishment.getReason());
//            insertPunishment.setTimestamp(5, new Timestamp(punishment.getStartDate().getTime()));
//            insertPunishment.setTimestamp(6, new Timestamp(punishment.getExpiryDate().getTime()));
//            insertPunishment.setTimestamp(7, new Timestamp(punishment.getExpiryDateActual().getTime()));
//            insertPunishment.execute();
        } catch (SQLException e) {
            DisciplineX.severeError("A severe error has encountered while inserting a punishment into the database. The plugin has been shut down.");
            DisciplineX.getInstance().shutdownPlugin();
            e.printStackTrace();
        }
    }

    /**
     * This method will retrieve every user which is currently muted.
     * @return ResultSet containing every user which is currently muted.
     */
    public ArrayList<Player> getOnlineMutedPlayers() {
        Connection conn = createConnection();
        assert conn != null;
        ArrayList<Player> mutedPlayers = new ArrayList<>();
        try {
            final PreparedStatement query = conn.prepareStatement("SELECT player_id FROM active_punishments WHERE punishment_type = 'MUTE';");
            final ResultSet rs = query.executeQuery();
            while (rs.next()) { // Get every player who is muted
                Player p = getPlayerFromUuid(UUID.fromString(rs.getString("player_id"))); // Initialise variable for player
                if (p == null) // Check if player is online before adding to list
                    continue;
                mutedPlayers.add(p); // Add player to list
            }
        } catch (SQLException e) {
            DisciplineX.severeError("A severe error has encountered while querying the database. The plugin has been shut down.");
            DisciplineX.getInstance().shutdownPlugin();
        }
        return mutedPlayers;
    }

    /**
     * Perma-mute player
     * @param punishment punishment object to insert into database
     * @return
     */
    public void mutePlayer(final Punishment punishment) {
        insertPunishment(punishment);
    }

    /**
     * This method is used to retrieve a player from their UUID.
     * @param uuid UUID of the player to retrieve
     * @return Player object of the player with the given UUID
     */
    private Player getPlayerFromUuid(final @NotNull UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }


    /**
     * This method inserts a new player into the players table.
     * @param player player to insert into the database
     */
    public void insertPlayer(final @NotNull Player player) {
        Connection conn = createConnection();
        try {
            final PreparedStatement insertPlayer = conn.prepareStatement("INSERT INTO players (uuid, name) VALUES (?, ?);");
            insertPlayer.setString(1, player.getUniqueId().toString());
            insertPlayer.setString(2, player.getName());
            insertPlayer.execute();
        } catch (SQLException e) {
            DisciplineX.severeError("A severe error has encountered while inserting a player into the database. The plugin has been shut down.");
            DisciplineX.getInstance().shutdownPlugin();
        }
    }

    /**
     * This function checks whether a player is already in the players table.
     * This function is used to prevent duplicate entries in the database.
     * @param player player to check
     * @return true if played has played before, false otherwise.
     */
    public boolean playerHasPlayedBefore(final @NotNull Player player) {
        Connection conn = createConnection();
        try {
            final PreparedStatement query = conn.prepareStatement("SELECT * FROM players WHERE uuid = ?;");
            query.setString(1, player.getUniqueId().toString());
            final ResultSet rs = query.executeQuery();
            return rs.next(); // Query will only have results if the player has played before. Therefore, if this is true then the player is in the database.
        } catch (SQLException e) {
            DisciplineX.severeError("A severe error has encountered while querying the database. The plugin has been shut down.");
            DisciplineX.getInstance().shutdownPlugin();
        }
        return false;
    }
}
