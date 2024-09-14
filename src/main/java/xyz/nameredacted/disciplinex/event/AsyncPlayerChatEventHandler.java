package xyz.nameredacted.disciplinex.event;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import xyz.nameredacted.disciplinex.DisciplineX;
import xyz.nameredacted.disciplinex.api.db.DatabaseHandler;

import java.util.ArrayList;

import static org.bukkit.event.EventPriority.HIGHEST;
import static xyz.nameredacted.disciplinex.staticaccess.StaticAccess.PLAYER_IS_MUTED;

/**
 * This class is used to prevent muted
 * players from sending messages.
 *
 * @see DatabaseHandler
 */
public class AsyncPlayerChatEventHandler implements Listener {

    private ArrayList<Player> onlineMutedPlayers;

    @EventHandler(priority = HIGHEST)
    public void onPlayerChat(@NotNull AsyncChatEvent event) {
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
        onlineMutedPlayers = findAllMutedPlayers();
        onlineMutedPlayers.stream().forEach(player -> {
            System.out.println(player.getName());
        });
        Player p = event.getPlayer();
        if (isPlayerMuted(p)) {
            // Send message to player
            p.sendMessage(PLAYER_IS_MUTED);
            event.setCancelled(true);
        }
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
    private ArrayList<Player> findAllMutedPlayers() {
//        DatabaseHandler.asyncGetAllMutedPlayers().thenAccept(rs -> {
//            while (true) {
//                try {
//                    if (!rs.next()) break;
//                } catch (SQLException e) {
//                    throw new RuntimeException(e);
//                }

        // Synchronous version
        return DisciplineX.getInstance().getDb().getOnlineMutedPlayers();
    }
}
