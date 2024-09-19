package xyz.nameredacted.disciplinex.cmd.admin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.nameredacted.disciplinex.DisciplineX;
import xyz.nameredacted.disciplinex.api.Punishment;
import xyz.nameredacted.disciplinex.cmd.Command;

import java.util.List;

import static xyz.nameredacted.disciplinex.staticaccess.StaticAccess.COMMAND_SUCCESS;
import static xyz.nameredacted.disciplinex.staticaccess.StaticAccess.PLUGIN_PREFIX;

/**
 * This command is used to find every punishment
 * a staff member has executed.
 * @see Command
 */
public class BlameCommand extends Command {
    @Override
    protected boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("disciplinex.admin.blame");
    }

    @Override
    protected boolean areArgsValid(String[] args) {
        return args.length == 1;
    }

    @Override
    protected boolean execute(Player player, String[] args) {
        Player target = Bukkit.getPlayer(args[0]);

        // Get every punishment executed by target staff member
        List<Punishment> punishments = DisciplineX.getInstance().getDb().getPunishmentsExecutedByStaffMember(target);
        // Check whether the list is empty
        if (punishments.isEmpty()) {
            player.sendMessage(PLUGIN_PREFIX.append(Component.text("No punishments executed by ").color(NamedTextColor.WHITE))
                    .append(Component.text(target.getName()).color(NamedTextColor.RED)));
            return COMMAND_SUCCESS;
        }
        punishments.forEach(punishment -> { // Loop through each punishment
            String punishedName = Bukkit.getOfflinePlayer(punishment.getPlayerPunished()).getName();
            assert punishedName != null;
            player.sendMessage(PLUGIN_PREFIX.append(Component.text("Punishment: ").color(NamedTextColor.WHITE))
                    .append(Component.text(punishment.getPunishmentType().toString()).color(NamedTextColor.RED))
                    .append(Component.text(" | Target: ").color(NamedTextColor.WHITE))
                    .append(Component.text(punishedName).color(NamedTextColor.RED))
                    .append(Component.newline())
                    .append(Component.text(" | Reason: ").color(NamedTextColor.WHITE))
                    .append(Component.text(punishment.getReason()).color(NamedTextColor.RED))
                    .append(Component.newline())
                    .append(Component.text(" | Date: ").color(NamedTextColor.WHITE))
                    .append(Component.text(punishment.getOrigin().toString()).color(NamedTextColor.RED))
                    .append(Component.newline())
                    .append(Component.text(" | Expiry: ").color(NamedTextColor.WHITE))
                    .append(Component.text(punishment.getExpiry().toString()).color(NamedTextColor.RED)));
        });

        return COMMAND_SUCCESS;
    }

    @Override
    protected TextComponent getUsageMessage() {
        return PLAYER_PREFIX.append(Component.text("Usage: /blame <playername>").color(NamedTextColor.WHITE));
    }

}
