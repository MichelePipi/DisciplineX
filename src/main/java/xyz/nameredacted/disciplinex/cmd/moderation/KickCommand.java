package xyz.nameredacted.disciplinex.cmd.moderation;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.nameredacted.disciplinex.api.PermissionChecks;

import static xyz.nameredacted.disciplinex.staticaccess.StaticAccess.*;

public class KickCommand implements CommandExecutor {

    private final String PLAYERS_ONLY = "Only players may execute this command.";
    private final String INCORRECT_USAGE_MSG = "Please enter the command correctly.";


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd,
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
}