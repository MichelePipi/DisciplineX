package xyz.nameredacted.disciplinex.event;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.nameredacted.disciplinex.DisciplineX;
import xyz.nameredacted.disciplinex.api.db.DatabaseHandler;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * This class is used to prevent muted
 * players from sending messages.
 *
 * @see DatabaseHandler
 */
public class AsyncPlayerChatEventHandler implements Listener {

    ArrayList<Player> onlineMutedPlayers = new ArrayList<>();

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
//        final UUID messageSender = event.getPlayer().getUniqueId();
//        DatabaseHandler.asyncIsPlayerMuted(messageSender).thenAccept(rs -> {
//           while (true) {
//               try {
//                   if (!rs.next()) break;
//               } catch (SQLException e) {
//                   throw new RuntimeException(e);
//               }
//               try {
//                   if (rs.getBoolean("muted")) {
//                       return;
//                   }
//               } catch (SQLException e) {
//                   throw new RuntimeException(e);
//               }
//           }
//        });
//
//        event.setCancelled(true); // Stop the player's message from being sent
    }

    public void addMutedPlayer(Player player) {
        onlineMutedPlayers.add(player);
    }

    public void removeMutedPlayer(Player player) {
        onlineMutedPlayers.remove(player);
    }

    public boolean isPlayerMuted(Player player) {
        return onlineMutedPlayers.contains(player);
    }

    /**
     * Find every muted player.
     */
    public void findAllMutedPlayers() {
//        DatabaseHandler.asyncGetAllMutedPlayers().thenAccept(rs -> {
//            while (true) {
//                try {
//                    if (!rs.next()) break;
//                } catch (SQLException e) {
//                    throw new RuntimeException(e);
//                }

        // Synchronous version
        final ResultSet mutedPlayers = DisciplineX.getInstance().getDb().getMutedPlayers();
        while (mutedPlayers.next()) {
    }
}
