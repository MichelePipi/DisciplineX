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
import xyz.nameredacted.disciplinex.exception.IncorrectArgumentException;
import xyz.nameredacted.disciplinex.staticaccess.StaticAccess;

import static xyz.nameredacted.disciplinex.staticaccess.StaticAccess.*;

/**
 * This class contains the code for how /mute works. It also works
 * as a baseline for how implementations of the {@code Command} class
 * should be.
 *
 * @see xyz.nameredacted.disciplinex.cmd.Command
 */
public class MuteCommand extends Command {

    @Override
    protected boolean hasPermission(CommandSender sender) {
        return PermissionChecks.hasPermission(sender, "disciplinex.moderation.mute");
    }

    @Override
    protected boolean areArgsValid(String[] args) {
        return args.length == 1;
    }

    /**
     * This method will only allow the end-user to
     * provide a currently online player.
     * <p>
     * TODO: Work wiih player name, then check db, then perform db query.
     *
     * @param sender
     * @param args
     * @return true if ran correctly, false if otherwise
     */
    @Override
    protected boolean execute(Player sender, String[] args) {
        Player targetPlayer = Bukkit.getPlayer(args[0]);

        if (!isPlayerOnline(targetPlayer)) {
            sender.sendMessage(StaticAccess.PLAYER_NOT_FOUND_MSG);
            return COMMAND_FAILED;
        }

        return StaticAccess.COMMAND_SUCCESS;
    }

    @Override
    protected TextComponent getUsageMessage() {
        return StaticAccess.PLUGIN_PREFIX
                .append(Component.text("Correct arguments: /mute <player_name>"));
    }

    @ApiStatus.Obsolete
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd,
                             @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(PLAYERS_ONLY_MSG);
            return COMMAND_FAILED;
        }

        final Player p = (Player) sender;
        // Check whether player can access /mute
        if (PermissionChecks.hasPermission(p, "disciplinex.moderation.mute")) {
            if (args.length == 0) {
                try {
                    throw new IncorrectArgumentException(Component.text("a"), p);
                } catch (IncorrectArgumentException e) {
                    throw new RuntimeException(e);
                }
            }

            return COMMAND_FAILED;
        } else {
            p.sendMessage(PERMISSION_ERROR);
            return true;
        }

    }
}
