package me.illusion.outcastmoderation.Moderation;

import com.google.common.collect.Maps;
import me.illusion.outcastmoderation.OutcastModeration;
import me.illusion.outcastmoderation.Util.Communication.Chat;
import me.illusion.outcastmoderation.Util.Communication.Format;
import me.illusion.outcastmoderation.Util.Config.ConfigData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Map;

public class Utilities {
    private static ArrayList<Player> vanishedPlayers = new ArrayList<>();
    private static Map<String, String> nickPlayers = Maps.newHashMap();

    private Player ply;
    private String playerName;

    private String chatPermission = new ConfigData(OutcastModeration.getInstance().staff).getStringFromConfig("moderation.permissions.staff-chat");
    private String chatWatermark = new ConfigData(OutcastModeration.getInstance().staff).getStringFromConfig("moderation.staff-chat.prefix");

    public Utilities(Player ply) {
        this.ply = ply;
    }

    public Utilities(String playerName) {
        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
            if (onlinePlayers.getName().equals(playerName)) {
                this.ply = onlinePlayers;
                this.playerName = ply.getName();
            }
        }
    }

    public static ArrayList<Player> getVanishedPlayerList() {
        return vanishedPlayers;
    }

    public static Map<String, String> getNickedPlayerList() {
        return nickPlayers;
    }

    public boolean vanish() {
        if (ply == null) {
            return false;
        }

        if (getVanishedPlayerList().contains(ply)) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.showPlayer(OutcastModeration.getInstance(), ply);
            }

            new Chat(Format.formatString(new ConfigData(OutcastModeration.getInstance().chatMessages)
                    .getStringFromConfig("staff.alert.vanish.disable"),
                    "#IGN", ply.getName())).privateMessage(chatPermission, chatWatermark);
            return unVanishPlayer(ply);
        }

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.hidePlayer(OutcastModeration.getInstance(), ply);
        }

        new Chat(Format.formatString(new ConfigData(OutcastModeration.getInstance().chatMessages)
                        .getStringFromConfig("staff.alert.vanish.enable"),
                "#IGN", ply.getName())).privateMessage(chatPermission, chatWatermark);
        return vanishPlayer(ply);
    }

    public boolean hasVanish() {
        return getVanishedPlayerList().contains(ply);
    }

    public boolean removeVanish() {
        return unVanishPlayer(ply);
    }

    public boolean nick(String nickName) {
        return nickPlayer(ply.getName(), nickName);
    }

    public boolean removeNick() {
        return removePlayerNick(ply.getName());
    }

    private boolean vanishPlayer(Player ply) {
        try {
            getVanishedPlayerList().add(ply);
            return true;
        }
        catch (ClassCastException | NullPointerException | IllegalArgumentException | UnsupportedOperationException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private boolean unVanishPlayer(Player ply) {
        try {
            getVanishedPlayerList().remove(ply);
            return true;
        }
        catch (ClassCastException | NullPointerException | IllegalArgumentException | UnsupportedOperationException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private boolean nickPlayer(String playerName, String nickName) {
        try {
            getNickedPlayerList().put(playerName, nickName);
            return true;
        }
        catch (ClassCastException | NullPointerException | IllegalArgumentException | UnsupportedOperationException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private boolean removePlayerNick(String playerName) {
        try {
            getNickedPlayerList().remove(playerName);
            return true;
        }
        catch (ClassCastException | NullPointerException | UnsupportedOperationException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
