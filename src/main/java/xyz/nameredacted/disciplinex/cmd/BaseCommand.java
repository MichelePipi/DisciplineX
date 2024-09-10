package xyz.nameredacted.disciplinex.cmd;

import org.bukkit.command.Command;
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
public abstract class BaseCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {

        if (!ifPlayer(sender)) {
            sender.sendMessage(PLAYERS_ONLY_MSG);
            return COMMAND_FAILED;
        }
        Player player = (Player) sender;

        // Check if player has the required permission
        if (!hasPermission(player)) {
            player.sendMessage(PERMISSION_ERROR);
            return true;
        }

        // Check if the arguments are valid
        if (!areArgsValid(args)) {
            player.sendMessage(getUsageMessage());
            return COMMAND_FAILED;
        }

        // Call the abstract method to handle the specific command logic
        return execute(player, args);
    }

    /**
     * This method is used to check if the command executor is a player.
     * @param sender
     * @return true if the command executor is a player, false otherwise
     */
    private boolean ifPlayer(CommandSender sender) {
        return sender instanceof org.bukkit.entity.Player;
    }

    /**
     * This method is used to check if the command executor has permission.
     * @param sender
     * @return true if the command executor has permission, false otherwise
     */
    protected abstract boolean hasPermission(CommandSender sender);

    /**
     * This method checks whether the arguments provided by the player are valid.
     * @param args
     */
    protected abstract boolean areArgsValid(String[] args);

    /**
     * This method will have the command logic.
     */
    protected abstract boolean execute(CommandSender sender, String[] args);

    /**
     * This method will have the correct arguments.
     */
    protected abstract String getUsageMessage();
}
