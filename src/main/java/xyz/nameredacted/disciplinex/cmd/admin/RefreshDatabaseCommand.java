package xyz.nameredacted.disciplinex.cmd.admin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.nameredacted.disciplinex.DisciplineX;
import xyz.nameredacted.disciplinex.cmd.Command;

import java.util.ArrayList;

import static xyz.nameredacted.disciplinex.staticaccess.StaticAccess.COMMAND_SUCCESS;
import static xyz.nameredacted.disciplinex.staticaccess.StaticAccess.PLUGIN_PREFIX;

public class RefreshDatabaseCommand extends Command {

    // Contains every player who has run /refreshdatabase <key>, but have not confirmed yet.
    private ArrayList<Player> pendingConfirms = new ArrayList<>();

    @Override
    protected boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("disciplinex.admin.refreshdatabase");
    }

    @Override
    protected boolean areArgsValid(String[] args) {
        return args.length == 1;
    }

    @Override
    protected boolean execute(Player player, String[] args) {
        String key = args[0];
        if (!key.equals(DisciplineX.getInstance().getStringFromConfig("database_reset_key"))) {
            player.sendMessage(PLUGIN_PREFIX.append(Component.text("You have selected the incorrect wipe key. Please contact your server administrator if you believe this is an error.").color(NamedTextColor.RED)));
            return COMMAND_SUCCESS;
        }

        DisciplineX.getInstance().getDb().refreshCredentials();
        player.sendMessage(PLUGIN_PREFIX.append(Component.text("Database credentials have been refreshed. Please check the server console to ensure the credentials were correct.").color(NamedTextColor.GREEN)));

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
