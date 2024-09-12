package xyz.nameredacted.disciplinex;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import xyz.nameredacted.disciplinex.api.db.DatabaseHandler;
import xyz.nameredacted.disciplinex.cmd.moderation.BanCommand;
import xyz.nameredacted.disciplinex.cmd.moderation.KickCommand;
import xyz.nameredacted.disciplinex.cmd.moderation.MuteCommand;
import xyz.nameredacted.disciplinex.cmd.moderation.WarnCommand;

import java.util.logging.Logger;

/**
 * This class is the main class of the plugin.
 * It is used to initialise the plugin and
 * to import all the commands.
 */
public final class DisciplineX extends JavaPlugin {

    private static @NotNull Logger log;
    private static DisciplineX instance;
    private DatabaseHandler db;

    @Override
    public void onEnable() {
        // Plugin startup logic
        // Initialise static variables
        log = getLogger();
        setInstance(this);
        importCommands();
        db = new DatabaseHandler();
        db.setupDatabase();
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

    public static void setInstance(DisciplineX instance) {
        DisciplineX.instance = instance;
    }
}
