package me.illusion.outcastmoderation.Events;

import me.illusion.outcastmoderation.Moderation.Punishments.Punish;
import me.illusion.outcastmoderation.Util.Communication.LogMe;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.UUID;

public class PlayerLogin implements Listener {
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {
        Player ply = e.getPlayer();
        UUID pID = ply.getUniqueId();

        Punish.banCheck(e, ply);
    }
}
