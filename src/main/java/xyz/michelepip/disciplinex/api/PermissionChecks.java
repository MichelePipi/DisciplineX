package xyz.michelepip.disciplinex.api;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PermissionChecks {

    /**
     * This function checks whether some player has
     * access to perform a certain command.
     * @param p Player
     * @param permission String
     * @return true if player has permission, false otherwise
     */
    public static boolean hasPermission(final @NotNull Player p, final @NotNull String permission) {
        return p.hasPermission(permission);
    }
}
