package me.illusion.outcastmoderation.Events;

import me.illusion.outcastmoderation.Moderation.Punishments.Punish;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class PlayerMove implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player ply = e.getPlayer();
        UUID pID = ply.getUniqueId();

        if (Punish.getFrozenPlayerList().contains(ply)) {
            e.setCancelled(true);
        }
    }
}
