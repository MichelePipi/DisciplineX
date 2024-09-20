package xyz.nameredacted.disciplinex.cmd.moderation.punish;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import xyz.nameredacted.disciplinex.cmd.Command;

import static xyz.nameredacted.disciplinex.staticaccess.StaticAccess.COMMAND_SUCCESS;
import static xyz.nameredacted.disciplinex.staticaccess.StaticAccess.PLUGIN_PREFIX;

/**
 * This is an implementation of every punishment command, and abstracts it into
 * an easy-to-use Graphical User Interface. It constructs a GUI based on what arguments the
 * staff member inputs:
 *
 * No arguments: Display a GUI with every player.
 * One argument (Player to punish has been added): Skip the first step, and display the punishments the player can execute.
 *
 * The player executing this command MUST have permissions to execute this command, and at leas
 */
public class PunishCommand extends Command {

    @Override
    protected boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("disciplinex.moderation.punish");
    }

    @Override
    protected boolean areArgsValid(String[] args) {
        return true; // We know they will ALWAYS at least use one argument.
    }

    @Override
    protected boolean execute(Player player, String[] args) {
        // Since we don't know whether the arguments contain a player, we need to check.
        if (args.length == 0) {
            // Display GUI with every player
            return COMMAND_SUCCESS;
        }
        // Since we now know the player is an argument, we can display the punishments the player can execute.
        /*
        What should be in the Player GUI [8x4 gui]:

        Player head in the top-middle
        1. Warn in the second row
        2. Mute in the second row
        3. Ban in the second row
        4. Kick in the second row

        The punishments will appear as barrier blocks, to show the user cannot execute them. This will occur when and ONLY when:
        - The player does not have the permission [disciplinex.moderation.warn, mute, ...]
        - The player ALREADY has one of the punishments. (You cannot ban someone twice!) Warn and kick is excluded from these as they can occur more than once.
         */

        return COMMAND_SUCCESS;
    }

    @Override
    protected TextComponent getUsageMessage() {
        return PLUGIN_PREFIX.append(Component.text("Usage: /punish <player> [OPTIONAL]").color(NamedTextColor.WHITE));
    }

    private Inventory createPunishmentSelectionGui(Player p, Player target) {
        Inventory inv = p.getServer().createInventory(null, 36, "Punish " + target.getName());
        // Add items to the inventory
        inv.setItem(10, getSkullItem(target)); // Player's head

        return inv;
    }

    /**
     * This function generates an ItemStack (an item) with some
     * player's head (given by target)
     * @param target The player whose head we want to get
     * @return An ItemStack with the player's head
     */
    private ItemStack getSkullItem(@NotNull Player target) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD); // Create item
        SkullMeta skullMeta = (SkullMeta)item.getItemMeta(); // Get item metadata
        skullMeta.setOwningPlayer(target); // Set skull to player's head.
        item.setItemMeta(skullMeta); // Set the item's metadata to the skull metadata

        return item;
    }
}
