package me.illusion.outcastmoderation.Commands;

import me.illusion.outcastmoderation.Moderation.Utilities;
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

public class Vanish implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player ply = (Player) sender;
        UUID pID = ply.getUniqueId();

        if (args.length <= 0) {
            boolean alreadyVanished = new Utilities(ply).hasVanish();
            String vanishString = alreadyVanished ? "&c&lOFF" : "&a&lON";

            if (!new Utilities(ply).vanish()) {
                new Chat(ply, new ConfigData(OutcastModeration.getInstance().chatMessages)
                        .getStringFromConfig("chat.error.vanish-toggle.self")).message(true);
                return true;
            }

            new Chat(ply, Format.formatString(new ConfigData(OutcastModeration.getInstance().chatMessages)
                    .getStringFromConfig("chat.success.vanish-toggle.self"), "#VANISH", vanishString)).message(true);
            return true;
        }

        String playerName = args[0];

        if (!new Utilities(playerName).vanish()) {
            new Chat(ply, Format.formatString(new ConfigData(OutcastModeration.getInstance().chatMessages)
                    .getStringFromConfig("chat.error.vanish-toggle.player"), "#IGN", playerName)).message(true);
            return true;
        }

        boolean alreadyVanished = new Utilities(playerName).hasVanish();
        String vanishString = alreadyVanished ? "&c&lOFF" : "&a&lON";

        new Chat(ply, Format.formatString(new ConfigData(OutcastModeration.getInstance().chatMessages)
                .getStringFromConfig("chat.success.vanish-toggle.player"),
                "#IGN", playerName, "#VANISH", vanishString)).message(true);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        String commandName = command.getName();

        if (commandName.equals("vanish") || commandName.equals("v")) {
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
