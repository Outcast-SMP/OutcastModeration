package me.illusion.outcastmoderation.Events;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import me.illusion.outcastmoderation.Moderation.Punishments.Punish;
import me.illusion.outcastmoderation.OutcastModeration;
import me.illusion.outcastmoderation.Util.Config.ConfigData;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Map;
import java.util.UUID;

public class PlayerBlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player ply = e.getPlayer();
        UUID pID = ply.getUniqueId();

        String bypassPermission = new ConfigData(OutcastModeration.getInstance().antixray).getStringFromConfig("moderation.permissions.antixray-bypass");

        if (ply.isOp() || ply.hasPermission(bypassPermission) || ply.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        if (!Punish.mineBanned(e, ply)) {
            Punish.oreCheck(e, ply);
        }
    }
}
