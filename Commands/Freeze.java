package me.illusion.outcastmoderation.Commands;

import me.illusion.outcastmoderation.Moderation.Punishments.Punish;
import me.illusion.outcastmoderation.Util.Communication.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Freeze implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player ply = (Player) sender;
        UUID pID = ply.getUniqueId();

        String errorMsg = "&cIncorrect command usage. Please use /freeze <name>";

        if (args.length <= 0) {
            new Chat(ply, errorMsg).message(true);
            return true;
        }

        String playerName = args[0];

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.getName().equals(playerName)) {
                if (!new Punish(onlinePlayer.getName()).freeze(ply)) {
                    new Chat(ply, "&cUnable to freeze the specified player '" + playerName + "'.").message(true);
                    return true;
                }
                
                new Chat(ply, "&aYou have toggled " + playerName + " frozen state.").message(true);
                return true;
            }
        }
        
        new Chat(ply, "&cThere is no player named " + playerName + ".").message(true);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        String commandName = command.getName();

        if (commandName.equals("freeze") || commandName.equals("f")) {
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
