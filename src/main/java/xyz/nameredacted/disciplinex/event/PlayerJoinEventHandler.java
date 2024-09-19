package xyz.nameredacted.disciplinex.event;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xyz.nameredacted.disciplinex.DisciplineX;

public class PlayerJoinEventHandler implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        // Check the database to see if the player has played before.
        if (DisciplineX.getInstance().getDb().playerHasPlayedBefore(p))
            return;
        DisciplineX.getInstance().getDb().insertPlayer(p);
    }
}
