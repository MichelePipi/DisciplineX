package xyz.nameredacted.disciplinex.cmd.admin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.nameredacted.disciplinex.DisciplineX;
import xyz.nameredacted.disciplinex.cmd.Command;

import static xyz.nameredacted.disciplinex.staticaccess.StaticAccess.PLUGIN_PREFIX;

public class RefreshDatabaseCommand extends Command {

    @Override
    protected boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("disciplinex.admin.refreshdatabase");
    }

    @Override
    protected boolean areArgsValid(String[] args) {
        return true;
    }

    @Override
    protected boolean execute(Player player, String[] args) {
        DisciplineX.getInstance().getDb().refreshCredentials();
        player.sendMessage(PLUGIN_PREFIX.append(Component.text("Database credentials have been refreshed. Please check the server console to ensure the credentials were correct.").color(NamedTextColor.GREEN)));
        // Play a success noise to the player.
        player.playSound(player.getLocation(), "entity.experience_orb.pickup", 1.0f, 1.0f);

        return true;
    }

    @Override
    protected TextComponent getUsageMessage() {
        return PLUGIN_PREFIX.append(Component.text("/refreshdatabase").color(NamedTextColor.WHITE));
    }

    @Override
    protected boolean commandIsPlayersOnly() {
        return true;
    }
}
