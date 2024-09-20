package xyz.nameredacted.disciplinex;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import xyz.nameredacted.disciplinex.api.db.DatabaseHandler;
import xyz.nameredacted.disciplinex.cmd.admin.BlameCommand;
import xyz.nameredacted.disciplinex.cmd.admin.RefreshDatabaseCommand;
import xyz.nameredacted.disciplinex.cmd.moderation.*;
import xyz.nameredacted.disciplinex.event.AsyncPlayerChatEventHandler;
import xyz.nameredacted.disciplinex.event.PlayerJoinEventHandler;

import java.util.logging.Logger;

/**
 * This class is the main class of the plugin.
 * It is used to initialise the plugin and
 * to import all the commands.
 */
public final class DisciplineX extends JavaPlugin {

    private static Logger log;
    private static DisciplineX instance;
    private DatabaseHandler db;

    @Override
    public void onEnable() {
        // Plugin startup logic
        // Initialise static variables
        log = getLogger();
        setInstance(this);
        importCommands();
        importEvents();
        saveDefaultConfig(); // Save new config file, if it does not exist.
        info("Attempting to connect to database...");
        db = new DatabaseHandler();
        info("Setting up database...");
        db.setupDatabase();
        info("Database setup ✅");

        /**
         * TODO: First-run startup sequence.
         *
         * First:
         * - Check if the plugin has been run before.
         * - Then, before setting the database up inform the user the plugin has the function to completely wipe the database.
         * - Send a SHA256-encrypted code to the console.
         * - Have the user write it down somewhere, then type the code in.
         * - Setup database
         * - Continue running.
         */
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    /**
     * This class is used to import every command
     * into the plugin. Make sure each command
     * is correctly included in the plugin.yml, or else
     * it will not be recognized by the plugin.
     */
    @SuppressWarnings("ConstantConditions")
    private void importCommands() {
        getCommand("kick").setExecutor(new KickCommand());
        getCommand("ban").setExecutor(new BanCommand());
        getCommand("warn").setExecutor(new WarnCommand());
        getCommand("mute").setExecutor(new MuteCommand());
        getCommand("refreshdatabase").setExecutor(new RefreshDatabaseCommand());
        getCommand("blame").setExecutor(new BlameCommand());
        getCommand("unmute").setExecutor(new UnmuteCommand());
    }

    private void importEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerJoinEventHandler(), this);
        pm.registerEvents(new AsyncPlayerChatEventHandler(), this);
    }

    public static void severeError(final @NotNull String msg) {
        log.severe(msg);
    }

    public void shutdownPlugin() {
        getServer().getPluginManager().disablePlugin(this);
    }

    public static DisciplineX getInstance() {
        return instance;
    }

    private void setInstance(@NotNull DisciplineX instance) {
        DisciplineX.instance = instance;
    }

    public DatabaseHandler getDb() {
        return db;
    }

    private void info(@NotNull String msg) {
        log.info(msg);
    }

    /**
     * Returns a string from the config.yml file, returning an empty quote if the key does not exist.
     * @param key
     * @return string empty if not found.
     */
    public String getStringFromConfig(final String key) {
        return getConfig().getString(key, ""); // def parameter is what will be returned if the key does not exist.
    }

    /**
     * Returns an integer from the config.yml file, returning 0 if the key does not exist.
     * @param key
     * @return integer 0 if not found.
     */
    public int getIntFromConfig(final String key) {
        return getConfig().getInt(key, 0);
    }

    public Object getFromConfig(final String key) {
        return getConfig().get(key);
    }
}
