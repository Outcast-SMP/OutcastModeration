package me.illusion.outcastmoderation.Events;

import me.illusion.outcastmoderation.Moderation.Utilities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerLeave implements Listener {
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player ply = e.getPlayer();
        UUID pID = ply.getUniqueId();

        Utilities.getVanishedPlayerList().remove(ply);
    }
}
