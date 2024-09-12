package xyz.nameredacted.disciplinex.cmd.moderation;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static xyz.nameredacted.disciplinex.staticaccess.StaticAccess.COMMAND_FAILED;
import static xyz.nameredacted.disciplinex.staticaccess.StaticAccess.PLAYERS_ONLY_MSG;

public class WarnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd,
                             @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(PLAYERS_ONLY_MSG);
            return COMMAND_FAILED;
        }

        Player p = (Player) sender;


        return COMMAND_FAILED;
    }
}
