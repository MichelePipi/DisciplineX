package xyz.nameredacted.disciplinex.api.db;


import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Blocking;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
import xyz.nameredacted.disciplinex.DisciplineX;
import xyz.nameredacted.disciplinex.api.Punishment;
import xyz.nameredacted.disciplinex.api.PunishmentTypes;
import xyz.nameredacted.disciplinex.api.PunshmentExpirationReasons;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
     *
     * @return
     */
    private Connection createConnection() {
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
     * <p>
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
     * <p>
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
     * <p>
     * This function is run synchronously as it is run once when the plugin is enabled.
     */
    @TestOnly
    private void createPunishmentHistoryTable() {
        Connection conn = createConnection();
        try {
            final PreparedStatement createTable = conn.prepareStatement("CREATE TABLE IF NOT EXISTS punishment_history (" +
                    "punishment_id INT, " +
                    "FOREIGN KEY (punishment_id) REFERENCES active_punishments(punishment_id)," +
                    "action ENUM('NO_EXPIRY', 'EXPIRED', 'MANUALLY_LIFTED') NOT NULL," +
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
     *
     * @param punishment punishment object to insert into database
     */
    public void insertPunishment(final @NotNull Punishment punishment) {
        Connection conn = createConnection();
        try {
            final PreparedStatement insertPunishment = conn.prepareStatement("INSERT INTO active_punishments (player_id, punisher_id, punishment_type, reason, start_date, expiry_date) VALUES (?, ?, ?, ?, ?, ?);");
            insertPunishment.setString(1, String.valueOf(punishment.getPlayerPunished()));
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
     *
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
     *
     * @param punishment punishment object to insert into database
     * @return
     */
    public void mutePlayer(final Punishment punishment) {
        insertPunishment(punishment);
    }

    /**
     * This method is used to retrieve a player from their UUID.
     *
     * @param uuid UUID of the player to retrieve
     * @return Player object of the player with the given UUID
     */
    private Player getPlayerFromUuid(final @NotNull UUID uuid) {
        return Bukkit.getPlayer(uuid);
    }


    /**
     * This method inserts a new player into the players table.
     *
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
     *
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

    /**
     * This function finds the number of active punishments overall.
     * @return int number of active punishments
     */
    public int getNumberOfActivePunishments() {
        Connection conn = createConnection();
        try {
            final PreparedStatement query = conn.prepareStatement("SELECT COUNT(*) FROM active_punishments;");
            final ResultSet rs = query.executeQuery();
            rs.next();
            return rs.getInt(1);
        } catch (SQLException e) {
            DisciplineX.severeError("A severe error has encountered while querying the database. The plugin has been shut down.");
            DisciplineX.getInstance().shutdownPlugin();
        }
        return -1;
    }

    /**
     * This function finds every active punishment overall.
     * @return HashMap of active punishments
     */
    public HashMap<UUID, ArrayList<Punishment>> getActivePunishments() {
        Connection conn = createConnection();
        HashMap<UUID, ArrayList<Punishment>> activePunishments = new HashMap<>();
        try {
            final PreparedStatement query = conn.prepareStatement("SELECT * FROM active_punishments;");
            final ResultSet rs = query.executeQuery();
            while (rs.next()) {
                Punishment punishment = createPunishmentFromResultSet(rs);
                activePunishments.computeIfAbsent(punishment.getPlayerPunished(), k -> new ArrayList<>()).add(punishment);
            }
        } catch (SQLException e) {
            DisciplineX.severeError("A severe error has encountered while querying the database. The plugin has been shut down.");
            DisciplineX.getInstance().shutdownPlugin();
        }
        return activePunishments;
    }

    /**
     * Finds the active punishments for a player.
     * @param player player to find active punishments for
     * @return ArrayList of active punishments for the player
     */
    public ArrayList<Punishment> getActivePunishments(final @NotNull UUID player) {
        Connection conn = createConnection();
        ArrayList<Punishment> activePunishments = new ArrayList<>();
        try {
            final PreparedStatement query = conn.prepareStatement("SELECT * FROM active_punishments WHERE player_id = ?;");
            query.setString(1, player.toString());
            final ResultSet rs = query.executeQuery();
            while (rs.next()) {
                activePunishments.add(createPunishmentFromResultSet(rs));
            }
            rs.close();
            conn.close();
        } catch (SQLException e) {
            DisciplineX.severeError("A severe error has encountered while querying the database. The plugin has been shut down.");
            DisciplineX.getInstance().shutdownPlugin();
        }
        return activePunishments;
    }

    /**
     * This function moves an active punishment to the expired table.
     */
    public void expirePunishment(final @NotNull Punishment punishment, final @NotNull PunshmentExpirationReasons reason) {
        Connection conn = createConnection();
        try {
            final PreparedStatement insertPunishment = conn.prepareStatement("INSERT INTO punishment_history (punishment_id, action, timestamp) VALUES (?, ?, ?);");
            insertPunishment.setString(1, String.valueOf(punishment.getId()));
            insertPunishment.setString(2, reason.toString());
            insertPunishment.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            insertPunishment.execute();

            // delete from active_punishments
            final PreparedStatement deletePunishment = conn.prepareStatement("DELETE FROM active_punishments WHERE player_id = ? AND punisher_id = ? AND punishment_type = ? AND reason = ? AND start_date = ? AND expiry_date = ?;");
            deletePunishment.setString(1, punishment.getPlayerPunished().toString());
            deletePunishment.setString(2, punishment.getBlame().toString());
            deletePunishment.setString(3, punishment.getPunishmentType().toString());
            deletePunishment.setString(4, punishment.getReason());
            deletePunishment.setTimestamp(5, new Timestamp(punishment.getOrigin().getTime()));
            deletePunishment.setTimestamp(6, new Timestamp(punishment.getExpiry().getTime()));
            deletePunishment.execute();
        } catch (SQLException e) {
            DisciplineX.severeError("A severe error has encountered while inserting a punishment into the database. The plugin has been shut down.");
            e.printStackTrace();
            DisciplineX.getInstance().shutdownPlugin();
        }
    }

    /**
     * This function moves an active punishment (based on its id) to the expires table.
     * @param id id of the punishment to expire
     */
    public void expirePunishment(final int id) {
        Connection conn = createConnection();
        try {
            final PreparedStatement insertPunishment = conn.prepareStatement("INSERT INTO punishment_history (punishment_id, action, timestamp) VALUES (?, ?, ?);");
            insertPunishment.setInt(1, id);
            insertPunishment.setString(2, "EXPIRED");
            insertPunishment.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            insertPunishment.execute();
        } catch (SQLException e) {
            DisciplineX.severeError("A severe error has encountered while inserting a punishment into the database. The plugin has been shut down.");
            DisciplineX.getInstance().shutdownPlugin();
        }
    }

    /**
     * This function retrieves every punishment stored in the active punishments table.
     * @return List of punishments
     * @return
     */
    public List<Punishment> loadPunishments() {
        Connection conn = createConnection();
        List<Punishment> punishments = new ArrayList<>();
        try {
            final PreparedStatement query = conn.prepareStatement("SELECT * FROM active_punishments;");
            final ResultSet rs = query.executeQuery();
            while (rs.next()) {
                punishments.add(createPunishmentFromResultSet(rs));
            }
            rs.close();
            conn.close();
        } catch (SQLException e) {
            DisciplineX.severeError("A severe error has encountered while querying the database. The plugin has been shut down.");
            DisciplineX.getInstance().shutdownPlugin();
        }
        return punishments;
    }

    /**
     * This function retrieves every punishment stored in the active punishments table.
     * @return List of punishments
     * @return
     */
    private Punishment createPunishmentFromResultSet(final ResultSet rs) throws SQLException {
        PunishmentTypes type = PunishmentTypes.valueOf(rs.getString("punishment_type"));
        int id = rs.getInt("punishment_id");
        UUID player = UUID.fromString(rs.getString("player_id"));
        UUID punisher = UUID.fromString(rs.getString("punisher_id"));
        Date origin = rs.getDate("start_date");
        Date expiry = rs.getDate("expiry_date");
        String reason = rs.getString("reason");
        return new Punishment(type, player, punisher, origin, expiry, reason, id);
    }

    /**
     * This function retrieves every punishment stored in the active punishments table which were executed
     * by a player.
     * @param player player to find punishments for
     * @return List of punishments
     */
    public List<Punishment> getPunishmentsExecutedByStaffMember(final Player player) {
        Connection conn = createConnection(); // Connect to database
        List<Punishment> punishments = new ArrayList<>(); // Create empty list of punishments
        try {
            final PreparedStatement query = conn.prepareStatement("SELECT * FROM active_punishments WHERE punisher_id = ?;"); // Create query
            query.setString(1, player.getUniqueId().toString()); // Set UUID to find UUID of player
            final ResultSet rs = query.executeQuery(); // Execute query
            while (rs.next()) { // While there are results to query
                punishments.add(createPunishmentFromResultSet(rs)); // Add punishment to list
            }
            rs.close(); // Close result set
            conn.close(); // Close database connection
        } catch (SQLException e) {
            DisciplineX.severeError("A severe error has encountered while querying the database. The plugin has been shut down.");
            DisciplineX.getInstance().shutdownPlugin();
        }
        return punishments; // Send back list
    }

    @TestOnly
    @Blocking
    /**
     * This function sends the entire database (players, active_punishments, punishment_history) to the console/player.
     */
    public void checkDatabase(final @NotNull CommandSender sender) {
        Connection conn = createConnection();
        try {
            final PreparedStatement queryPlayers = conn.prepareStatement("SELECT * FROM players;");
            final ResultSet rsPlayers = queryPlayers.executeQuery();
            sender.sendMessage("Players:");
            while (rsPlayers.next()) {
                sender.sendMessage("Player ID: " + rsPlayers.getInt("player_id") + " | UUID: " + rsPlayers.getString("uuid") + " | Name: " + rsPlayers.getString("name"));
            }
            rsPlayers.close();

            final PreparedStatement queryActivePunishments = conn.prepareStatement("SELECT * FROM active_punishments;");
            final ResultSet rsActivePunishments = queryActivePunishments.executeQuery();
            sender.sendMessage("Active Punishments:");
            while (rsActivePunishments.next()) {
                sender.sendMessage("Punishment ID: " + rsActivePunishments.getInt("punishment_id") + " | Player ID: " + rsActivePunishments.getString("player_id") + " | Punisher ID: " + rsActivePunishments.getString("punisher_id") + " | Type: " + rsActivePunishments.getString("punishment_type") + " | Reason: " + rsActivePunishments.getString("reason") + " | Start Date: " + rsActivePunishments.getDate("start_date") + " | Expiry Date: " + rsActivePunishments.getDate("expiry_date"));
            }
            rsActivePunishments.close();

            final PreparedStatement queryPunishmentHistory = conn.prepareStatement("SELECT * FROM punishment_history;");
            final ResultSet rsPunishmentHistory = queryPunishmentHistory.executeQuery();
            sender.sendMessage("Punishment History:");
            while (rsPunishmentHistory.next()) {
                sender.sendMessage("Punishment ID: " + rsPunishmentHistory.getInt("punishment_id") + " | Action: " + rsPunishmentHistory.getString("action") + " | Timestamp: " + rsPunishmentHistory.getDate("timestamp"));
            }
            rsPunishmentHistory.close();
            conn.close();
        } catch (SQLException e) {
            DisciplineX.severeError("A severe error has encountered while querying the database. The plugin has been shut down.");
            DisciplineX.getInstance().shutdownPlugin();
        }
    }

    /**
     * This function exports the database as a CSV file and saves it to the plugin's resources folder.
     */
    public void exportDatabase() {
        try (Connection connection = createConnection()) {
            String sql = "SELECT * FROM active_punishments";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            FileWriter csvWriter = new FileWriter("./data.csv");

            // Write the header (column names)
            int columnCount = resultSet.getMetaData().getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                csvWriter.append(resultSet.getMetaData().getColumnName(i));
                if (i < columnCount) csvWriter.append(",");
            }
            csvWriter.append("\n");

            // Write the data
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    csvWriter.append(resultSet.getString(i));
                    if (i < columnCount) csvWriter.append(",");
                }
                csvWriter.append("\n");
            }

            csvWriter.flush();
            csvWriter.close();

            System.out.println("CSV file was created successfully!");

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}

