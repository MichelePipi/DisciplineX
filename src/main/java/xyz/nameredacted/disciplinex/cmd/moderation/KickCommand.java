package xyz.nameredacted.disciplinex.cmd.moderation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import xyz.nameredacted.disciplinex.api.PermissionChecks;
import xyz.nameredacted.disciplinex.cmd.Command;

import static xyz.nameredacted.disciplinex.staticaccess.StaticAccess.*;

/**
 * This class contains the code for the /kick command.
 *
 * @see Command
 */
public class KickCommand extends Command {

    @ApiStatus.Obsolete
    private final String PLAYERS_ONLY = "Only players may execute this command.";
    @ApiStatus.Obsolete
    private final String INCORRECT_USAGE_MSG = "Please enter the command correctly.";


    @ApiStatus.Obsolete
    public boolean obseleteOnCommand(@NotNull CommandSender sender, @NotNull Command cmd,
                             @NotNull String s, @NotNull String[] args) {
        // check whether player or console executed the command
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may execute this command.");
            return COMMAND_FAILED;
        }
        // Explicitly cast sender variable as player type
        Player player = (Player) sender;

        // Check if player input the correct number of arguments
        if (args.length != 1) {
            player.sendMessage("Please enter the command in the form: /kick <playername>");
            return COMMAND_FAILED;
        }

        if (!PermissionChecks.hasPermission(player, "disciplinex.moderation.kick")) {
            player.sendMessage(PERMISSION_ERROR);
            return true;
        }

        // Get the player to kick from the argument
        String targetPlayerName = args[0];
        Player target = Bukkit.getPlayer(targetPlayerName);
        if (target == null) { // Player is offline
            player.sendMessage("Cannot kick an online player.");
            return COMMAND_FAILED;
        }

        target.kick(Component.text("You have been kicked."));
        player.sendMessage("Kicking " + target.getName());
        return COMMAND_SUCCESS;
    }

    @Override
    protected boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("disciplinex.moderation.kick");
    }

    @Override
    protected boolean areArgsValid(String[] args) {
        return args.length == 1;
    }

    @Override
    protected TextComponent getUsageMessage() {
        return PLUGIN_PREFIX
                .append(Component.text("Correct arguments: /kick <player_name>"));
    }

    @Override
    protected boolean execute(CommandSender sender, String[] args) {
        Player p = (Player)sender;
        Player target = Bukkit.getPlayer(args[0]); // We know that both this command requires a target, and that this target is valid due to
                                                    // the implementation of Command.java
        // Message which says to the player and target that the player has kicked the target, and the target has been kicked. Also add a full stop to both messages.
        p.sendMessage(PLUGIN_PREFIX.append(Component.text("You have kicked " + target.getName() + ".")));
        target.kick(Component.text("You have been kicked."));

        return COMMAND_SUCCESS;
    }
}