package me.illusion.outcastmoderation.Moderation.Punishments;

import com.google.common.collect.Maps;
import me.illusion.outcastmoderation.OutcastModeration;
import me.illusion.outcastmoderation.Util.Communication.Chat;
import me.illusion.outcastmoderation.Util.Communication.Format;
import me.illusion.outcastmoderation.Util.Communication.LogMe;
import me.illusion.outcastmoderation.Util.Config.ConfigData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
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

    private static Map<String, Long> blocksBroken = Maps.newLinkedHashMap();
    private static Map<String, Long> mineBannedPlayers = Maps.newHashMap();

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

    public static Map<String, Long> getBlockBrokenList() {
        return blocksBroken;
    }

    public static Map<String, Long> getMineBannedList() {
        return mineBannedPlayers;
    }

    public static void oreCheck(BlockBreakEvent e, Player ply) {
        Block block = e.getBlock();
        Material blockMaterial = block.getBlockData().getMaterial();

        String thresholdItems = new ConfigData(OutcastModeration.getInstance().antixray).getStringFromConfig(blockMaterial + ".threshold-items");
        String thresholdTime = new ConfigData(OutcastModeration.getInstance().antixray).getStringFromConfig(blockMaterial + ".threshold-time");
        String cooldownTime = new ConfigData(OutcastModeration.getInstance().antixray).getStringFromConfig("moderation.antixray.cooldown-time");

        if (ChatColor.stripColor(thresholdItems).equalsIgnoreCase("none")) {
            return;
        }

        String getBrokenString = null;
        Long time = Cooldown.setBanTime(thresholdTime);

        if (!getBlockBrokenList().isEmpty()) {
            for (Map.Entry<String, Long> entry : getBlockBrokenList().entrySet()) {
                if (entry.getKey().contains(ply.getName() + "_" + blockMaterial)) {
                    getBrokenString = entry.getKey();
                }
            }
        }

        if (getBrokenString == null) {
            getBlockBrokenList().put(ply.getName() + "_" + blockMaterial + "#1", Cooldown.storeBanTime(time));
            return;
        }

        int amountMined = Integer.parseInt(getBrokenString.substring(getBrokenString.lastIndexOf('#') + 1));
        long checkTime = getBlockBrokenList().get(getBrokenString) - System.currentTimeMillis();

        if (!(checkTime > 0)) {
            getBlockBrokenList().remove(getBrokenString);
            return;
        }

        if (amountMined > Integer.parseInt(thresholdItems)) {
            if (!new Punish(ply.getName()).mineBan(cooldownTime, blockMaterial)) {
                new LogMe("Unable to ban '" + ply.getName() + "' from mining.").Error();
                return;
            }
            getBlockBrokenList().remove(getBrokenString);

            e.setDropItems(false);
            e.setExpToDrop(0);
            return;
        }

        Long getPreviousTime = getBlockBrokenList().get(getBrokenString);

        if (amountMined > 1)
            removeStringFromMap(getBlockBrokenList(), ply.getName() + "_" + blockMaterial, (amountMined - 1));

        getBlockBrokenList().put(ply.getName() + "_" + blockMaterial + "#" + (amountMined + 1), getPreviousTime);
    }

    public static boolean mineBanned(BlockBreakEvent e, Player ply) {
        Block block = e.getBlock();
        Material mat = block.getBlockData().getMaterial();

        if (!getMineBannedList().containsKey(ply.getName() + "_" + mat)) {
            return false;
        }

        long checkTime = getMineBannedList().get(ply.getName() + "_" + mat) - System.currentTimeMillis();

        if (!(checkTime > 0)) {
            getMineBannedList().remove(ply.getName() + "_" + mat);
            return false;
        }

        e.setDropItems(false);
        e.setExpToDrop(0);

        //new Chat(ply, "&7You are currently on a cooldown (" + Cooldown.getBanTimeFormat(checkTime) + ")").message(false);
        return true;
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
            e.disallow(PlayerLoginEvent.Result.KICK_OTHER, Format.formatString(new ConfigData(OutcastModeration.getInstance().chatMessages)
                    .getStringFromConfig("punish.ban-permanent.remind-user"), "#REASON", getBanReason));
            return;
        }

        e.disallow(PlayerLoginEvent.Result.KICK_OTHER, Format.formatString(new ConfigData(OutcastModeration.getInstance().chatMessages)
                .getStringFromConfig("punish.ban.remind-user"), "#REASON", getBanReason, "#TIME", Cooldown.getBanTimeFormat(checkTime)));
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

        new Chat(ply, Format.formatString(new ConfigData(OutcastModeration.getInstance().chatMessages)
                .getStringFromConfig("punish.mute.remind-user"), "#TIME", Cooldown.getBanTimeFormat(checkTime))).message(true);
        e.setCancelled(true);
    }

    public static void chatDelayCheck(AsyncPlayerChatEvent e, Player ply) {
        if (getChatDelayList().isEmpty()) {
            return;
        }

        if (!getPlayerChattedList().containsKey(ply.getName()) && !ply.hasPermission(new ConfigData(OutcastModeration.getInstance().config)
                .getStringFromConfig("moderation.permissions.chatdelay-bypass"))) {
            getPlayerChattedList().put(ply.getName(), System.currentTimeMillis());
            return;
        }

        long checkTime = getChatDelayList().get("Delay");
        long getPlayerTime = getPlayerChattedList().get(ply.getName());

        if (getPlayerTime + checkTime > System.currentTimeMillis()) {
            e.setCancelled(true);
            new Chat(ply, Format.formatString(new ConfigData(OutcastModeration.getInstance().chatMessages)
                    .getStringFromConfig("chat.error.delaychat"), "#TIME", String.valueOf(Cooldown.getBanTime(checkTime)))).message(true);
            return;
        }

        e.setCancelled(false);
        getPlayerChattedList().remove(ply.getName());
    }

    public boolean mineBan(String duration, Material mat) {
        if (playerName == null) {
            return false;
        }

        Long time = Cooldown.setBanTime(duration);

        new Chat(Format.formatString(new ConfigData(OutcastModeration.getInstance().chatMessages)
                .getStringFromConfig("staff.alert.antixray"), "#IGN", playerName, "#MATERIAL", mat.name(), "#TIME", Cooldown.getBanTimeFormat(time))).privateMessage(chatPermission, chatWatermark);

        return mineBanPlayer(playerName + "_" + mat, Cooldown.storeBanTime(time));
    }

    public boolean ban(Player staff, String duration, String reason) {
        if (playerName == null) {
            return false;
        }

        if (reason.length() == 0)
            reason = "none";

        if (duration.equalsIgnoreCase("permanent") || duration.equalsIgnoreCase("perm")) {
            new Chat(Format.formatString(new ConfigData(OutcastModeration.getInstance().chatMessages)
                    .getStringFromConfig("staff.alert.ban-permanent"), "#IGN", playerName, "#STAFF", staff.getName(), "#REASON", reason)).privateMessage(chatPermission, chatWatermark);
            kick(staff, reason);

            return banPlayer(playerName, reason, 22090320000L); // 7 centuries
        }

        Long time = Cooldown.setBanTime(duration);

        new Chat(Format.formatString(new ConfigData(OutcastModeration.getInstance().chatMessages)
                .getStringFromConfig("staff.alert.ban"), "#IGN", playerName, "#TIME", Cooldown.getBanTimeFormat(time), "#STAFF", staff.getName(), "#REASON", reason)).privateMessage(chatPermission, chatWatermark);
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

        new Chat(Format.formatString(new ConfigData(OutcastModeration.getInstance().chatMessages)
                .getStringFromConfig("staff.alert.mute"), "#IGN", playerName, "#TIME", Cooldown.getBanTimeFormat(time), "#STAFF", staff.getName(), "#REASON", reason)).privateMessage(chatPermission, chatWatermark);

        return mutePlayer(playerName, Cooldown.storeBanTime(time));
    }

    public void kick(Player staff, String reason) {
        if (punishPlayer == null) {
            new Chat(staff, "&cUnable to kick the specified player...").message(true);
            return;
        }

        if (reason.length() == 0)
            reason = "none";

        punishPlayer.kickPlayer(Format.formatString(new ConfigData(OutcastModeration.getInstance().chatMessages)
                .getStringFromConfig("chat.command.kick"), "#STAFF", staff.getName(), "#REASON", reason));
    }

    public boolean warn(Player staff, String reason) {
        if (playerName == null) {
            return false;
        }

        if (reason.length() == 0)
            reason = "none";

        new Chat(Format.formatString(new ConfigData(OutcastModeration.getInstance().chatMessages)
                .getStringFromConfig("staff.alert.warn"), "#IGN", playerName, "#STAFF", staff.getName(), "#REASON", reason)).privateMessage(chatPermission, chatWatermark);

        new Chat(punishPlayer, Format.formatString(new ConfigData(OutcastModeration.getInstance().chatMessages)
                .getStringFromConfig("chat.command.warning"), "#STAFF", staff.getName(), "#REASON", reason)).message(true);

        return warnPlayer(playerName, reason);
    }

    public boolean freeze(Player staff) {
        if (playerName == null) {
            return false;
        }

        if (!frozePlayers.contains(punishPlayer)) {
            new Chat(Format.formatString(new ConfigData(OutcastModeration.getInstance().chatMessages)
                    .getStringFromConfig("staff.alert.freeze.enable"), "#IGN", playerName, "#STAFF", staff.getName())).privateMessage(chatPermission, chatWatermark);

            new Chat(punishPlayer, Format.formatString(new ConfigData(OutcastModeration.getInstance().chatMessages)
                    .getStringFromConfig("chat.command.freeze.enable"), "#STAFF", staff.getName())).message(true);
            return freezePlayer(punishPlayer);
        }

        new Chat(Format.formatString(new ConfigData(OutcastModeration.getInstance().chatMessages)
                .getStringFromConfig("staff.alert.freeze.disable"), "#IGN", playerName, "#STAFF", staff.getName())).privateMessage(chatPermission, chatWatermark);

        new Chat(punishPlayer, Format.formatString(new ConfigData(OutcastModeration.getInstance().chatMessages)
                .getStringFromConfig("chat.command.freeze.disable"), "#STAFF", staff.getName())).message(true);
        return unFreezePlayer(punishPlayer);
    }

    public void delayChat(String duration) {
        Long time = Cooldown.setBanTime(duration);
        String formatTime = Cooldown.getBanTimeFormat(time);

        getChatDelayList().put("Delay", time);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            new Chat(onlinePlayer, Format.formatString(new ConfigData(OutcastModeration.getInstance().chatMessages)
                    .getStringFromConfig("chat.success.delaychat"), "#TIME", formatTime)).message(true);
        }
    }

    private static void removeStringFromMap(Map<String, Long> map, String value, int amount) {
        for (int i = 1; i <= amount; i++) {
            map.remove(value + "#" + amount);
        }
    }

    private boolean banPlayer(String playerName, String reason, Long time) {
        try {
            getBannedPlayerList().put(playerName, time);
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

    private boolean mineBanPlayer(String playerName, Long time) {
        try {
            getMineBannedList().put(playerName, time);
            return true;
        }
        catch (ClassCastException | NullPointerException | IllegalArgumentException | UnsupportedOperationException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
