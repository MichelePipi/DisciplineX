package xyz.nameredacted.disciplinex.api.db;

import org.bukkit.Bukkit;
import xyz.nameredacted.disciplinex.DisciplineX;
import xyz.nameredacted.disciplinex.api.Punishment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


/**
 * This class contains the code for managing punishments.
 */
public class PunishmentManager {


    // List of active punishments for every player.
    private HashMap<UUID, List<Punishment>> activePunishments = new HashMap<>();

    private DatabaseHandler dbHandler;

    /**
     * This function asynchronously loads the punishments from the database using
     * the {@code DatabaseHandler} class and stores them in the {@code activePunishments} map.
     */
    public void loadPunishments() {
        Bukkit.getScheduler().runTaskAsynchronously(DisciplineX.getInstance(), () -> {
            List<Punishment> punishments = dbHandler.loadPunishments();
            for (Punishment punishment : punishments) {
                if (!punishment.isActive()) {
                    // asyncRemovePunishment(punishment);
                } else {
                    addActivePunishment(punishment.getPlayerPunished(), punishment);
                }
            }
        });
    }

    /**
     * Adds active punishment to cache.
     */
    public void addActivePunishment(final UUID player, final Punishment punishment) {
        activePunishments.computeIfAbsent(punishment.getPlayerPunished(), k -> new ArrayList<>())
                .add(punishment);
    }

    /**
     * This method loads all active punishments for a player.
     * @param player The UUID of the player
     */
    public List<Punishment> loadActivePunishment(final UUID player) {
        removeInactivePunishments(player);
        return activePunishments.get(player);
    }

    /**
     * This method removes the inactive punishments for a player from the active punishments list.
     * @param player The UUID of the player
     */
    private void removeInactivePunishments(final UUID player) {
        activePunishments.get(player).removeIf(punishment -> !punishment.isActive());
    }
}
