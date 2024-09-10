package xyz.nameredacted.disciplinex.cmd.moderation;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.nameredacted.disciplinex.api.PermissionChecks;
import xyz.nameredacted.disciplinex.exception.IncorrectArgumentException;

import static xyz.nameredacted.disciplinex.staticaccess.StaticAccess.*;

public class MuteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd,
                             @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(PLAYERS_ONLY_MSG);
            return COMMAND_FAILED;
        }

        final Player p = (Player) sender;
        // Check whether player can access /mute
        if (!PermissionChecks.hasPermission(p, "disciplinex.moderation.mute")) {
            p.sendMessage(PERMISSION_ERROR);
            return true;
        }

        if (args.length == 0) {
            try {
                throw new IncorrectArgumentException(Component.text("a"), p);
            } catch (IncorrectArgumentException e) {
                throw new RuntimeException(e);
            }
        }

        return COMMAND_FAILED;
    }
}
