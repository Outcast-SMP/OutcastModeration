package me.illusion.outcastmoderation.Commands;

import me.illusion.outcastmoderation.Moderation.Punishments.Punish;
import me.illusion.outcastmoderation.OutcastModeration;
import me.illusion.outcastmoderation.Util.Communication.Chat;
import me.illusion.outcastmoderation.Util.Communication.Format;
import me.illusion.outcastmoderation.Util.Config.ConfigData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Tp implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player ply = (Player) sender;
        UUID pID = ply.getUniqueId();

        String errorMsg = new ConfigData(OutcastModeration.getInstance().chatMessages)
                .getStringFromConfig("chat.error.tp.incorrect-command-use");

        if (args.length <= 0) {
            new Chat(ply, errorMsg).message(true);
            return true;
        }

        String playerName = args[0];

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.getName().equals(playerName)) {
                if (onlinePlayer.getName().equals(ply.getName())) {
                    new Chat(ply, new ConfigData(OutcastModeration.getInstance().chatMessages)
                            .getStringFromConfig("chat.error.tp.same-player")).message(true);
                    return true;
                }

                if (!ply.teleport(onlinePlayer.getLocation())) {
                    new Chat(ply, Format.formatString(new ConfigData(OutcastModeration.getInstance().chatMessages)
                            .getStringFromConfig("chat.error.tp-message"), "#IGN", playerName)).message(true);
                    return true;
                }
                new Chat(ply, Format.formatString(new ConfigData(OutcastModeration.getInstance().chatMessages)
                        .getStringFromConfig("chat.success.tp-message"), "#IGN", playerName)).message(true);
                return true;
            }
        }

        new Chat(ply, Format.formatString(new ConfigData(OutcastModeration.getInstance().chatMessages)
                .getStringFromConfig("chat.error.tp.player-not-found"), "#IGN", playerName)).message(true);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        String commandName = command.getName();

        if (commandName.equals("tp")) {
            List<String> itemList = new ArrayList<>();

            switch (args.length) {
                case 1:
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        itemList.add(onlinePlayer.getName());
                    }
                    itemList.add("[player name]");
                    break;
                default:
                    itemList.add("Invalid amount of arguments.");
                    break;
            }
            return itemList;
        }
        return null;
    }
}
