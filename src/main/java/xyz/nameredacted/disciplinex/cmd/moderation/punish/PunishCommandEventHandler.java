package xyz.nameredacted.disciplinex.cmd.moderation.punish;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import xyz.nameredacted.disciplinex.DisciplineX;

public class PunishCommandEventHandler implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().title().contains(Component.text("Punish"))) { // We now know the player is not in the gui
            return;
        }
        Player exec = (Player) event.getWhoClicked();
        Player target = DisciplineX.getPlayerPunishMap().get(exec);
        event.setCancelled(true);

        // We can now check what the player clicked on, and execute the punishment.
        // ban = iron sword, kick = diamond sword, warn = redstone, mute = feather
        // Ban
        if (event.getCurrentItem().getType().equals(Material.IRON_SWORD)) {
            // Ban the player
            exec.performCommand("/ban " + target.getName());
        }
        // Repeat for each punishment type.
        if (event.getCurrentItem().getType().equals(Material.DIAMOND_SWORD)) {
            // Kick the player
            exec.performCommand("/kick " + target.getName());
        }
        if (event.getCurrentItem().getType().equals(Material.REDSTONE)) {
            // Warn the player
            exec.performCommand("/warn " + target.getName());
        }
        if (event.getCurrentItem().getType().equals(Material.FEATHER)) {
            // Mute the player
            exec.performCommand("/mute " + target.getName());
        }
    }
}
