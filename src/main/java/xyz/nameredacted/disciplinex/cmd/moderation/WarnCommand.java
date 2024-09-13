package xyz.nameredacted.disciplinex.cmd.moderation;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import static xyz.nameredacted.disciplinex.staticaccess.StaticAccess.COMMAND_FAILED;
import static xyz.nameredacted.disciplinex.staticaccess.StaticAccess.PLAYERS_ONLY_MSG;

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
        return false;
    }

    @Override
    protected boolean areArgsValid(String[] args) {
        return false;
    }

    @Override
    protected boolean execute(Player player, String[] args) {
        return false;
    }

    @Override
    protected TextComponent getUsageMessage() {
        return null;
    }
}
