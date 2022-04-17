package me.illusion.outcastmoderation.Util.Communication;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import me.illusion.outcastmoderation.OutcastModeration;
import me.illusion.outcastmoderation.Util.Config.ConfigData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Chat {
    private Player ply;
    private CommandSender sender;
    private String message;

    public Chat(String message) {
        this.message = IridiumColorAPI.process(message);
    }

    public Chat(CommandSender sender, String message) {
        this.sender = sender;
        this.message = IridiumColorAPI.process(message);
    }

    public Chat(Player ply, String message) {
        this.ply = ply;
        this.message = IridiumColorAPI.process(message);
    }

    public static String getPrefix() {
        return new ConfigData(OutcastModeration.getInstance().config).getStringFromConfig("moderation.prefix") + " ";
    }

    public static String makeDescription(StringBuilder sb, int minArg, int maxArg, String[] args) {
        for (int i = minArg; i < maxArg; i++){
            sb.append(args[i]).append(" ");
        }
        return sb.toString().trim();
    }

    public void message(boolean bWatermark) {
        ply.sendMessage(bWatermark ? getPrefix() + message : message);
    }

    public void messageSender(boolean bWatermark) {
        sender.sendMessage(bWatermark ? getPrefix() + message : message);
    }

    public void privateMessage(String permission, String watermark) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.hasPermission(permission)) {
                onlinePlayer.sendMessage(watermark + " " + message);
            }
        }
    }

    public void privateMessage(Player ply, String permission, String watermark) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.hasPermission(permission)) {
                onlinePlayer.sendMessage(watermark + " " + ChatColor.AQUA + ply.getName() + ": " + ChatColor.GRAY + message);
            }
        }
    }
}
