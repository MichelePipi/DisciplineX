package xyz.nameredacted.disciplinex.event;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.nameredacted.disciplinex.DisciplineX;
import xyz.nameredacted.disciplinex.api.Punishment;
import xyz.nameredacted.disciplinex.api.PunishmentTypes;

import java.util.List;

public class PlayerJoinEventHandler implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        // Check the database to see if the player has played before.
        if (DisciplineX.getInstance().getDb().playerHasPlayedBefore(p))
            return;
        DisciplineX.getInstance().getDb().insertPlayer(p);

        List<Punishment> activePunishments = DisciplineX.getInstance().getDb().getActivePunishments(p.getUniqueId());
        if (activePunishments.isEmpty())
            return;
        activePunishments.forEach(punishment -> { // Loop through each punishment
            if (punishment.getPunishmentType().equals(PunishmentTypes.BAN)) { // If a punishment is a ban
                p.kick(Component.text(punishment.getReason())); // Kick the player with the reason
            }
        });
    }
}
