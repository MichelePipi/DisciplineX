package xyz.michelepip.disciplinex.cmd.other.staffchat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static xyz.michelepip.disciplinex.staticaccess.StaticAccess.PLAYERS_ONLY_MSG;

public class StaffChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(PLAYERS_ONLY_MSG);
            return true;
        }

        Player p = (Player) sender;


        return false;
    }
}
