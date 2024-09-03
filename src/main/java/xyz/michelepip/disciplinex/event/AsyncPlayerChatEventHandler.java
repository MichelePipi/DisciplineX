package xyz.michelepip.disciplinex.event;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.michelepip.disciplinex.api.db.DatabaseHandler;

import java.sql.SQLException;
import java.util.UUID;

/**
 * This class is used to prevent muted
 * players from sending messages.
 *
 * @see xyz.michelepip.disciplinex.api.db.DatabaseHandler
 */
public class AsyncPlayerChatEventHandler implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        final UUID messageSender = event.getPlayer().getUniqueId();
        DatabaseHandler.asyncIsPlayerMuted(messageSender).thenAccept(rs -> {
           while (true) {
               try {
                   if (!rs.next()) break;
               } catch (SQLException e) {
                   throw new RuntimeException(e);
               }
               try {
                   if (rs.getBoolean("muted")) {
                       return;
                   }
               } catch (SQLException e) {
                   throw new RuntimeException(e);
               }
           }
        });

        if (!DatabaseHandler.asyncIsPlayerMuted()) { // First check whether the player is muted
            return;
        }
        event.setCancelled(true); // Stop the player's message from being sent
    }
}
