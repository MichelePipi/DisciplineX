package xyz.nameredacted.disciplinex.cmd;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static xyz.nameredacted.disciplinex.staticaccess.StaticAccess.*;

/**
 * Given that commands consistently repeat the same code across
 * virtually every instance, this class is used to abstract the process
 * and reduce redundancy. See command classes for implementation.
 *
 * Note: The "protected" keyword means that functions in the class can
 * only be accessed by classes in the same package, or subpackages. Therefore,
 * each command will be stored in the same package as this class.
 * @see xyz.nameredacted.disciplinex.cmd.moderation.MuteCommand
 */
public abstract class Command implements CommandExecutor {


    /**
     * This abstract method is used to handle the command logic.
     * As previously mentioned in the Javadocs, this method encapsulates
     * the repeated command logic across the plugin, to simplify and reduce code complexity.
     * See other command classes for appropriate implementation.
     *
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return true if successful execution, false otherwise.
     * @see xyz.nameredacted.disciplinex.cmd.moderation.MuteCommand
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command,
                             @NotNull String label, @NotNull String[] args) {

        if (!ifPlayer(sender)) {
            sender.sendMessage(PLAYERS_ONLY_MSG);
            return COMMAND_FAILED;
        }
        Player player = (Player) sender; // Since we now know the executor is a player, explicitly cast sender as a player.

        // Check if player does not have the required permission
        if (!hasPermission(player)) {
            player.sendMessage(PERMISSION_ERROR);
            // Play a low pitched cat growl sound, as a warning to the player. Make it random, but always at least low pitched.
            player.playSound(player.getLocation(), "minecraft:entity.cat.hiss", 1.0f, 0.5f);
            return COMMAND_FAILED;
        }

        // Check if the arguments are invalid
        if (!areArgsValid(args)) {
            player.sendMessage(getUsageMessage());
            return COMMAND_FAILED; // Cancel command
        }

        // Only check whether the target is a player if the command requires it
        if (requiresTargetPlayer() && !isPlayerOnline(getPlayer(args[0]))) { // Check whether command requires a target
                                                                             // player and if the target player is online.
            player.sendMessage(PLAYER_NOT_FOUND_MSG);
            return COMMAND_FAILED;
        }

        // Call the abstract method to handle the specific command logic
        return execute(player, args);
    }

    /**
     * This method is used to check if the command executor is a player.
     * @param sender who ran the command
     * @return true if the command executor is a player, false otherwise
     */
    private boolean ifPlayer(CommandSender sender) {
        return sender instanceof org.bukkit.entity.Player;
    }

    /**
     * This method checks, given some Player, whether it is online.
     * To prevent exceptions, it checks whether the player exists before
     * running the Player#isOnline() function.
     * @param p
     * @return true if player is online, false if it either does not exist or is not online.
     */
    protected boolean isPlayerOnline(final Player p) {
        if (!doesPlayerExist(p)) {
            return false;
        }
        return p.isOnline();
    }

    /**
     * This method checks whether some Player is online.
     * @param p
     * @return true if the player exists, false otherwise.
     */
    private boolean doesPlayerExist(final Player p) {
        return p != null;
    }

    /**
     * This method is used to check if the command executor has permission.
     * @param sender who ran the command
     * @return true if the command executor has permission, false otherwise
     */
    protected abstract boolean hasPermission(CommandSender sender);

    /**
     * This method checks whether the arguments provided by the player are valid.
     * @param args the arguments provided by the player
     */
    protected abstract boolean areArgsValid(String[] args);

    /**
     * This method will have the command logic.
     */
    protected abstract boolean execute(CommandSender sender, String[] args);

    /**
     * This method will have the message containing
     * the correct arguments that the executor did not provide.
     * @return TextComponent containing the correct arguments
     */
    protected abstract TextComponent getUsageMessage();

    /**
     * Determines whether the target player existing check will be run.
     * @return false unless overrided by implementation
     */
    protected boolean requiresTargetPlayer() {
        return false;
    }

    private Player getPlayer(final @NotNull String str) {
        return Bukkit.getPlayer(str);
    }
}
