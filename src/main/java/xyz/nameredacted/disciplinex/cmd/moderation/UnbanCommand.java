package xyz.nameredacted.disciplinex.cmd.moderation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.nameredacted.disciplinex.DisciplineX;
import xyz.nameredacted.disciplinex.api.Punishment;
import xyz.nameredacted.disciplinex.api.PunishmentTypes;
import xyz.nameredacted.disciplinex.api.PunshmentExpirationReasons;
import xyz.nameredacted.disciplinex.cmd.Command;

import java.util.List;

import static xyz.nameredacted.disciplinex.staticaccess.StaticAccess.*;

public class UnbanCommand extends Command {
    @Override
    protected boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("disciplinex.moderation.unban");
    }

    @Override
    protected boolean areArgsValid(String[] args) {
        return args.length == 1;
    }

    @Override
    protected boolean execute(Player player, String[] args) {
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        // Since we know the target is not null due to the implementation of Command.java, no need to check!
        assert targetPlayer != null;

        // First check if the player is muted
        List<Punishment> playerActivePunishments = DisciplineX.getInstance().getDb().getActivePunishments(targetPlayer.getUniqueId());
        if (playerActivePunishments.isEmpty()) {
            player.sendMessage(PLUGIN_PREFIX.append(Component.text("That player is not muted.").color(NamedTextColor.WHITE)));
            return COMMAND_SUCCESS;
        }

        playerActivePunishments.forEach(punishment -> { // Loop through each punishment
            if (punishment.getPunishmentType().equals(PunishmentTypes.BAN)) { // If a punishment is a mute
                DisciplineX.getInstance().getDb().expirePunishment(punishment, PunshmentExpirationReasons.MANUALLY_LIFTED); // Remove the punishment from the database
                player.sendMessage(PLUGIN_PREFIX.append(Component.text("Player ").color(NamedTextColor.WHITE)) // Send success message to blame
                        .append(Component.text(targetPlayer.getName()).color(NamedTextColor.RED))
                        .append(Component.text(" has been unmuted.").color(NamedTextColor.WHITE)));
            }
        });
        return COMMAND_FAILED;
    }

    @Override
    protected TextComponent getUsageMessage() {
        return PLUGIN_PREFIX.append(Component.text("Usage: /unban <player>").color(NamedTextColor.WHITE));
    }
}
