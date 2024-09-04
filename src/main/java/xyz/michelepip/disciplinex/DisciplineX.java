package xyz.michelepip.disciplinex;

import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import xyz.michelepip.disciplinex.api.db.DatabaseHandler;
import xyz.michelepip.disciplinex.cmd.moderation.BanCommand;
import xyz.michelepip.disciplinex.cmd.moderation.KickCommand;
import xyz.michelepip.disciplinex.cmd.moderation.MuteCommand;
import xyz.michelepip.disciplinex.cmd.moderation.WarnCommand;

import java.util.logging.Logger;

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
        importClasses();
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
    private void importClasses() {
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
