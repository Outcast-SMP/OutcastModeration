package me.illusion.outcastmoderation.Events;

import me.illusion.outcastmoderation.Moderation.Punishments.Punish;
import me.illusion.outcastmoderation.Moderation.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

public class PlayerChat implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player ply = e.getPlayer();
        UUID pID = ply.getUniqueId();

        if (Utilities.getNickedPlayerList().containsKey(ply.getName())) {
            String nickName = Utilities.getNickedPlayerList().get(ply.getName());
            ply.setDisplayName(nickName + ChatColor.RESET);
        }

        Punish.muteCheck(e, ply);
        Punish.chatDelayCheck(e, ply);
    }
}
