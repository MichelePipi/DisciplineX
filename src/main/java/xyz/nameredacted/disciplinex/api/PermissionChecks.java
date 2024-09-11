package xyz.nameredacted.disciplinex.api;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * This class is used to check whether a player has
 * access to perform a certain command.
 */
public class PermissionChecks {

    /**
     * This function checks whether some player has
     * access to perform a certain command.
     *
     * @param commandExecutor Who executed the command.
     * @param permission      String
     * @return true if player has permission, false otherwise
     */
    public static boolean hasPermission(final @NotNull CommandSender commandExecutor, final @NotNull String permission) {
        return commandExecutor.hasPermission(permission);
    }
}
