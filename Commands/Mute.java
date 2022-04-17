package me.illusion.outcastmoderation.Commands;

import me.illusion.outcastmoderation.Moderation.Punishments.Punish;
import me.illusion.outcastmoderation.OutcastModeration;
import me.illusion.outcastmoderation.Util.Communication.Chat;
import me.illusion.outcastmoderation.Util.Communication.Format;
import me.illusion.outcastmoderation.Util.Config.ConfigData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Mute implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player ply = (Player) sender;
        UUID pID = ply.getUniqueId();

        String errorMsg = new ConfigData(OutcastModeration.getInstance().chatMessages)
                .getStringFromConfig("chat.error.mute.incorrect-command-use");

        if (args.length < 3) {
            new Chat(ply, errorMsg).message(true);
            return true;
        }

        String playerName = args[0];
        String muteTime = args[1];

        StringBuilder sb = new StringBuilder();
        String reason = Chat.makeDescription(sb, 2, args.length, args);

        if (!new Punish(playerName).mute(ply, muteTime, reason)) {
            new Chat(ply, Format.formatString(new ConfigData(OutcastModeration.getInstance().chatMessages)
                    .getStringFromConfig("chat.error.mute-message"), "#IGN", playerName)).message(true);
            return true;
        }

        new Chat(ply, Format.formatString(new ConfigData(OutcastModeration.getInstance().chatMessages)
                .getStringFromConfig("chat.success.mute-message"), "#IGN", playerName)).message(true);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        String commandName = command.getName();

        if (commandName.equals("mute") || commandName.equals("m")) {
            List<String> itemList = new ArrayList<>();

            switch (args.length) {
                case 1:
                    for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                        itemList.add(onlinePlayer.getName());
                    }
                    itemList.add("[player name]");
                    break;
                case 2:
                    itemList.add("[time in seconds]");
                    break;
            }

            if (args.length >= 3) {
                itemList.add("[reason]");
            }
            return itemList;
        }
        return null;
    }
}
