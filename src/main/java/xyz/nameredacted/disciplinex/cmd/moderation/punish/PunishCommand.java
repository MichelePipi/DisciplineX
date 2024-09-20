package xyz.nameredacted.disciplinex.cmd.moderation.punish;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.nameredacted.disciplinex.DisciplineX;
import xyz.nameredacted.disciplinex.api.Punishment;
import xyz.nameredacted.disciplinex.cmd.Command;

import java.util.List;

import static xyz.nameredacted.disciplinex.staticaccess.StaticAccess.COMMAND_SUCCESS;
import static xyz.nameredacted.disciplinex.staticaccess.StaticAccess.PLUGIN_PREFIX;

/**
 * This is an implementation of every punishment command, and abstracts it into
 * an easy-to-use Graphical User Interface. It constructs a GUI based on what arguments the
 * staff member inputs:
 * <p>
 * No arguments: Display a GUI with every player.
 * One argument (Player to punish has been added): Skip the first step, and display the punishments the player can execute.
 * <p>
 * The player executing this command MUST have permissions to execute this command, and at leas
 */
public class PunishCommand extends Command {

    private final List<Component> noPermissionOrCannotUse = List.of(
            Component.text("Either you do not have the required permissions to execute this punishment, or the player already has this punishment.").color(NamedTextColor.RED)
    );

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
        inv.setItem(4, getSkullItem(target)); // Player's head

        // Get every active punishment for target player.
        List<Punishment> activePunishments = DisciplineX.getInstance().getDb().getActivePunishments(target.getUniqueId());
        // Check if the player has an active punishment for each type.
        boolean isMuted = false;
        boolean isBanned = false;

        for (final Punishment activePunishment : activePunishments) {
            switch (activePunishment.getPunishmentType()) {
                case MUTE:
                    isMuted = true;
                    break;
                case BAN:
                    isBanned = true;
                    break;
                default:
                    break;
            }
        }

        // Create each item corresponding to warn, kick, ban, mute. If the player is already muted/banned, add a barrier block. If the executor does not have permissions to one of the punishments, add a barrier block.
        // Mute
        if (!isMuted && p.hasPermission("disciplinex.moderation.mute")) {
            addItemToInventory(inv, createItemStack(Material.FEATHER, "Mute", null), 10);
        } else {
            addItemToInventory(inv, createItemStack(Material.BARRIER, "Mute", noPermissionOrCannotUse), 10);
        }
        // Repeat for other punishments
        // Ban
        if (!isBanned && p.hasPermission("disciplinex.moderation.ban")) {
            addItemToInventory(inv, createItemStack(Material.IRON_SWORD, "Ban", null), 12);
        } else {
            addItemToInventory(inv, createItemStack(Material.BARRIER, "Ban", noPermissionOrCannotUse), 12);
        }
        // Kick
        if (p.hasPermission("disciplinex.moderation.kick")) {
            addItemToInventory(inv, createItemStack(Material.DIAMOND_SWORD, "Kick", null), 14);
        } else {
            addItemToInventory(inv, createItemStack(Material.BARRIER, "Kick", noPermissionOrCannotUse), 14);
        }
        // Warn
        if (p.hasPermission("disciplinex.moderation.warn")) {
            addItemToInventory(inv, createItemStack(Material.REDSTONE, "Warn", null), 16);
        } else {
            addItemToInventory(inv, createItemStack(Material.BARRIER, "Warn", noPermissionOrCannotUse), 16);
        }

        return inv;
    }

    /**
     * This function generates an ItemStack (an item) with some
     * player's head (given by target)
     *
     * @param target The player whose head we want to get
     * @return An ItemStack with the player's head
     */
    private ItemStack getSkullItem(@NotNull Player target) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD); // Create item
        SkullMeta skullMeta = (SkullMeta) item.getItemMeta(); // Get item metadata
        skullMeta.setOwningPlayer(target); // Set skull to player's head.
        item.setItemMeta(skullMeta); // Set the item's metadata to the skull metadata

        return item;
    }

    /**
     * This function generates an itemstack given some parameters.
     * @param material The material of the item
     * @param name The name of the item
     * @param lore The lore of the item
     * @return An ItemStack with the given parameters
     */
    private ItemStack createItemStack(@NotNull Material material, @Nullable String name, @Nullable List<Component> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        if (name == null)
            meta.displayName();
        else
            meta.displayName(Component.text(name).color(NamedTextColor.WHITE));
        if (lore != null)
            meta.lore(lore);
        item.setItemMeta(meta);

        return item;
    }

    /**
     * This function adds an item to an inventory.
     * @param item to add
     */
    private void addItemToInventory(@NotNull Inventory inv, @NotNull ItemStack item, int index) {
        inv.setItem(index, item);
    }
}
