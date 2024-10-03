package xyz.nameredacted.disciplinex.cmd.dev;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.nameredacted.disciplinex.DisciplineX;
import xyz.nameredacted.disciplinex.cmd.Command;

import static xyz.nameredacted.disciplinex.staticaccess.StaticAccess.COMMAND_SUCCESS;

public class CheckDbCommand extends Command {
    @Override
    protected boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("disciplinex.dev.checkdb");
    }

    @Override
    protected boolean areArgsValid(String[] args) {
        return true; // The command does not require any arguments.
    }

    @Override
    protected boolean execute(Player player, String[] args) {
        DisciplineX.getInstance().getDb().checkDatabase(player);
        return COMMAND_SUCCESS;
    }

    @Override
    protected TextComponent getUsageMessage() {
        return null;
    }

    @Override
    protected boolean commandIsPlayersOnly() {
        return false; // Console can execute this too
    }
}
