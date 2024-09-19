package xyz.nameredacted.disciplinex.cmd.moderation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import xyz.nameredacted.disciplinex.cmd.Command;

import static xyz.nameredacted.disciplinex.staticaccess.StaticAccess.*;

public class BanCommand extends Command {

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
    protected boolean execute(Player sender, String[] args) {
        return false;
    }


    @Override
    protected boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("disciplinex.moderation.ban");
    }

    @Override
    protected boolean areArgsValid(String[] args) {
        return args.length == 1;
    }

    @Override
    protected TextComponent getUsageMessage() {
        return PLUGIN_PREFIX.append(Component.text("Correct arguments: /ban <player_name>").color(NamedTextColor.WHITE));
    }
}
