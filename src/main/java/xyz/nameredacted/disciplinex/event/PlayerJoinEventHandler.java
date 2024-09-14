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
        // Make sure to not insert players who have already been inserted into the database before.
        if (p.hasPlayedBefore())
            return;
        DisciplineX.getInstance().getDb().insertPlayer(p);
    }
}
