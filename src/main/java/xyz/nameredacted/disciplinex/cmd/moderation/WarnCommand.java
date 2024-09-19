package xyz.nameredacted.disciplinex.cmd.moderation;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import static xyz.nameredacted.disciplinex.staticaccess.StaticAccess.*;

public class WarnCommand extends xyz.nameredacted.disciplinex.cmd.Command {
    @ApiStatus.Obsolete
    public boolean obseleteOnCommand(@NotNull CommandSender sender, @NotNull Command cmd,
                             @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(PLAYERS_ONLY_MSG);
            return COMMAND_FAILED;
        }

        Player p = (Player) sender;


        return COMMAND_FAILED;
    }

    @Override
    protected boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("disciplinex.moderation.warn");
    }

    @Override
    protected boolean areArgsValid(String[] args) {
        return args.length >= 1;
    }

    @Override
    protected boolean execute(Player player, String[] args) {
        Player target = Bukkit.getPlayer(args[0]);

        // Warn player
        if (args.length == 1) { // We know the target is not null, and that no reason was given.
            target.sendMessage(WARNED_PLAYER);
            player.sendMessage(WARNED_PLAYER);
        } else {
            target.sendMessage(WARNED_PLAYER + " Reason: " + args[1]);
            player.sendMessage(WARNED_PLAYER + " Reason: " + args[1]);

        }

        return COMMAND_SUCCESS;
    }

    @Override
    protected TextComponent getUsageMessage() {
        return null;
    }
}
