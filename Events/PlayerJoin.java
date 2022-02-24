package me.illusion.outcastmoderation.Events;

import me.illusion.outcastmoderation.Moderation.Utilities;
import me.illusion.outcastmoderation.OutcastModeration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player ply = e.getPlayer();
        UUID pID = ply.getUniqueId();

        for (Player vanishedPlayers : Utilities.getVanishedPlayerList()) {
            ply.hidePlayer(OutcastModeration.getInstance(), vanishedPlayers);
        }
    }
}
