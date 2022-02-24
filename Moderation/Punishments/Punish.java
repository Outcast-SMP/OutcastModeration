package me.illusion.outcastmoderation.Moderation.Punishments;

import com.google.common.collect.Maps;
import me.illusion.outcastmoderation.OutcastModeration;
import me.illusion.outcastmoderation.Util.Communication.Chat;
import me.illusion.outcastmoderation.Util.Communication.LogMe;
import me.illusion.outcastmoderation.Util.Config.ConfigData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.ArrayList;
import java.util.Map;

public class Punish {
    private static Map<String, Long> bannedPlayers = Maps.newHashMap();
    private static Map<String, String> banReasons = Maps.newHashMap();
    private static Map<String, String> warnedPlayers = Maps.newHashMap();
    private static Map<String, Long> mutedPlayers = Maps.newHashMap();
    private static Map<String, Long> delayedChat = Maps.newHashMap();
    private static Map<String, Long> playerHasChatted = Maps.newHashMap();
    private static ArrayList<Player> frozePlayers = new ArrayList<>();

    private String chatPermission = new ConfigData(OutcastModeration.getInstance().staff).getStringFromConfig("moderation.permissions.staff-chat");
    private String chatWatermark = new ConfigData(OutcastModeration.getInstance().staff).getStringFromConfig("moderation.staff-chat.prefix");

    private Player punishPlayer;
    private String playerName;

    public Punish(String playerName) {
        if (Bukkit.getPlayer(playerName) != null) {
            this.punishPlayer = Bukkit.getPlayer(playerName);
            this.playerName = punishPlayer.getName();
        }
    }

    public static Map<String, Long> getBannedPlayerList() {
        return bannedPlayers;
    }

    public static Map<String, String> getBanReasonList() {
        return banReasons;
    }

    public static Map<String, String> getWarnedPlayerList() {
        return warnedPlayers;
    }

    public static Map<String, Long> getMutedPlayerList() {
        return mutedPlayers;
    }

    public static Map<String, Long> getChatDelayList() {
        return delayedChat;
    }

    public static Map<String, Long> getPlayerChattedList() {
        return playerHasChatted;
    }

    public static ArrayList<Player> getFrozenPlayerList() {
        return frozePlayers;
    }

    public static void banCheck(PlayerLoginEvent e, Player ply) {
        if (!getBannedPlayerList().containsKey(ply.getName())) {
            return;
        }

        long checkTime = getBannedPlayerList().get(ply.getName()) - System.currentTimeMillis();

        if (!(checkTime > 0)) {
            getBannedPlayerList().remove(ply.getName());
            return;
        }

        String getBanReason = getBanReasonList().get(ply.getName());

        if ((checkTime / 1000) >= 604800) {
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "\nYou have been permanently banned!" + ChatColor.RESET + "\n\nReason: " + getBanReason + "\n\nDuration: " + ChatColor.RED + Cooldown.getBanTimeFormat(checkTime) + "\n ");
            return;
        }

        e.disallow(PlayerLoginEvent.Result.KICK_OTHER, ChatColor.RED + "\nYou are currently banned!" + ChatColor.RESET + "\n\nReason: " + getBanReason + "\n\nDuration: " + ChatColor.RED + Cooldown.getBanTimeFormat(checkTime) + "\n ");
    }

    public static void muteCheck(AsyncPlayerChatEvent e, Player ply) {
        if (!getMutedPlayerList().containsKey(ply.getName())) {
            return;
        }

        long checkTime = mutedPlayers.get(ply.getName()) - System.currentTimeMillis();

        if (!(checkTime > 0)) {
            e.setCancelled(false);
            getMutedPlayerList().remove(ply.getName());
            return;
        }

        new Chat(ply, "&cYou are muted for " + Cooldown.getBanTimeFormat(checkTime)).message(true);
        e.setCancelled(true);
    }

    public static void chatDelayCheck(AsyncPlayerChatEvent e, Player ply) {
        if (getChatDelayList().isEmpty()) {
            return;
        }

        if (!getPlayerChattedList().containsKey(ply.getName()) && !ply.hasPermission(new ConfigData(OutcastModeration.getInstance().config).getStringFromConfig("moderation.permissions.chatdelay-bypass"))) {
            getPlayerChattedList().put(ply.getName(), System.currentTimeMillis());
            return;
        }

        long checkTime = getChatDelayList().get("Delay");
        long getPlayerTime = getPlayerChattedList().get(ply.getName());

        if (getPlayerTime + checkTime > System.currentTimeMillis()) {
            e.setCancelled(true);
            new Chat(ply, "&7You can only send messages once every &c&l" + Cooldown.getBanTime(checkTime) + "&7.").message(true);
            return;
        }

        e.setCancelled(false);
        getPlayerChattedList().remove(ply.getName());
    }

    public boolean ban(Player staff, String duration, String reason) {
        if (playerName == null) {
            return false;
        }

        if (reason.length() == 0)
            reason = "none";

        if (duration.equalsIgnoreCase("permanent") || duration.equalsIgnoreCase("perm")) {
            new Chat("&7" + playerName + " has been banned permanently by &a" + staff.getName() + "&7 for '" + reason + "'.").privateMessage(chatPermission, chatWatermark);
            kick(staff, reason);

            return banPlayer(playerName, reason, 22090320000L); // 7 centuries
        }

        Long time = Cooldown.setBanTime(duration);
        String formatTime = Cooldown.getBanTimeFormat(time);

        new Chat("&7" + playerName + " has been banned for " + formatTime + "&a by " + staff.getName() + "&7 for '" + reason + "'.").privateMessage(chatPermission, chatWatermark);
        kick(staff, reason);

        return banPlayer(playerName, ChatColor.RED + reason + ChatColor.RESET, Cooldown.storeBanTime(time));
    }

    public boolean mute(Player staff, String duration, String reason) {
        if (playerName == null) {
            return false;
        }

        if (reason.length() == 0)
            reason = "none";

        Long time = Cooldown.setBanTime(duration);
        String formatTime = Cooldown.getBanTimeFormat(time);

        new Chat("&7" + playerName + " has been muted for " + formatTime + "&a by " + staff.getName() + "&7 for '" + reason + "'.").privateMessage(chatPermission, chatWatermark);

        return mutePlayer(playerName, Cooldown.storeBanTime(time));
    }

    public void kick(Player staff, String reason) {
        if (punishPlayer == null) {
            new Chat(staff, "&cUnable to kick the specified player...").message(true);
            return;
        }

        if (reason.length() == 0)
            reason = "none";

        punishPlayer.kickPlayer("\nYou have been kicked by " + ChatColor.RED + staff.getName() + ChatColor.RESET + "\nReason: " + ChatColor.RED + reason + ChatColor.RESET + "\n");
    }

    public boolean warn(Player staff, String reason) {
        if (playerName == null) {
            return false;
        }

        if (reason.length() == 0)
            reason = "none";

        new Chat("&7" + playerName + " has been warned &aby " + staff.getName() + "&7 for '" + reason + "'.").privateMessage(chatPermission, chatWatermark);

        return warnPlayer(playerName, reason);
    }

    public boolean freeze(Player staff) {
        if (playerName == null) {
            return false;
        }

        if (!frozePlayers.contains(punishPlayer)) {
            new Chat("&7" + playerName + " has been frozen &aby " + staff.getName() + "&7.").privateMessage(chatPermission, chatWatermark);
            new Chat(punishPlayer, "&cYou have been frozen by &l" + staff.getName() + "&c!").message(true);
            return freezePlayer(punishPlayer);
        }

        new Chat("&7" + playerName + " has been unfrozen &aby " + staff.getName() + "&7.").privateMessage(chatPermission, chatWatermark);
        new Chat(punishPlayer, "&aYou have been unfrozen by &l" + staff.getName() + "&a.").message(true);
        return unFreezePlayer(punishPlayer);
    }

    public void delayChat(String duration) {
        Long time = Cooldown.setBanTime(duration);
        String formatTime = Cooldown.getBanTimeFormat(time);

        getChatDelayList().put("Delay", time);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            new Chat(onlinePlayer, "&7A " + formatTime + " delay has been added to the chat.").message(true);
        }
    }

    private boolean banPlayer(String playerName, String reason, Long time) {
        try {
            getBannedPlayerList().put(playerName, time); // 7 centuries
            getBanReasonList().put(playerName, reason);
            return true;
        }
        catch (ClassCastException | NullPointerException | IllegalArgumentException | UnsupportedOperationException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private boolean mutePlayer(String playerName, Long time) {
        try {
            getMutedPlayerList().put(playerName, time);
            return true;
        }
        catch (ClassCastException | NullPointerException | IllegalArgumentException | UnsupportedOperationException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private boolean warnPlayer(String playerName, String reason) {
        try {
            getWarnedPlayerList().put(playerName, reason);
            return true;
        }
        catch (ClassCastException | NullPointerException | IllegalArgumentException | UnsupportedOperationException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private boolean freezePlayer(Player ply) {
        try {
            frozePlayers.add(ply);
            return true;
        }
        catch (ClassCastException | NullPointerException | IllegalArgumentException | UnsupportedOperationException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private boolean unFreezePlayer(Player ply) {
        try {
            frozePlayers.remove(ply);
            return true;
        }
        catch (ClassCastException | NullPointerException | UnsupportedOperationException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
