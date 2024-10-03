package xyz.nameredacted.disciplinex.cmd.moderation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import xyz.nameredacted.disciplinex.DisciplineX;
import xyz.nameredacted.disciplinex.api.PermissionChecks;
import xyz.nameredacted.disciplinex.api.Punishment;
import xyz.nameredacted.disciplinex.api.PunishmentTypes;
import xyz.nameredacted.disciplinex.cmd.Command;
import xyz.nameredacted.disciplinex.exception.IncorrectArgumentException;
import xyz.nameredacted.disciplinex.staticaccess.StaticAccess;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import static xyz.nameredacted.disciplinex.staticaccess.StaticAccess.*;

/**
 * This class contains the code for how /mute works. It also works
 * as a baseline for how implementations of the {@code Command} class
 * should be.
 *
 * @see xyz.nameredacted.disciplinex.cmd.Command
 */
public class MuteCommand extends Command {

    /**
     * This list contains all the players that are currently muted.
     * <p>
     *     This list is used to check whether a player is muted or not.
     *     If the player is muted, they will not be able to send messages.
     *     This list is populated by the {@code DatabaseHandler} class.
     *     </p>
     */
    private ArrayList<Player> onlineMutedPlayers = new ArrayList<>();

    /**
     * This method checks whether the player has the permission to run the command.
     * @param sender who ran the command
     * @return true if the player has permission, false otherwise
     */
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

        if (isPlayerOffline(targetPlayer)) {
            sender.sendMessage(StaticAccess.PLAYER_NOT_FOUND_MSG);
            return COMMAND_FAILED;
        }

        mute(sender, targetPlayer);
        sender.sendMessage(StaticAccess.PLAYER_MUTED.append(Component.text(targetPlayer.getName()).color(NamedTextColor.RED)));
        // Send PLAYER_MUTED to target player, with the reason appended on the end.
        String reason = "No reason provided.";
        targetPlayer.sendMessage(StaticAccess.PLAYER_MUTED.append(Component.text(sender.getName()).color(NamedTextColor.RED)).append(Component.text(" for: " + reason).color(NamedTextColor.WHITE)));


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

    private void mute(final Player player, final Player blame) {
        onlineMutedPlayers.add(player);
        Punishment punishment = new Punishment(PunishmentTypes.MUTE, player.getUniqueId(), blame.getUniqueId(), new Date(), null, null);
        DisciplineX.getInstance().getDb().mutePlayer(punishment);
    }

    /**
     * This method checks whether a player is muted based on their Player object.
     * @param player
     * @return true if the player is muted, false otherwiset
     */
    public boolean isPlayerMuted(final Player player) {
        return onlineMutedPlayers.contains(player);
    }

    /**
     * This method checks whether a player is muted based on their Unique ID.
     * @param player The player to check
     * @return true if the player is muted, false otherwise
     */
    public boolean isPlayerMuted(final UUID player) {
        return onlineMutedPlayers.stream().anyMatch(p -> p.getUniqueId().equals(player));
    }
}

