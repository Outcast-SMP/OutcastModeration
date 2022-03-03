package me.illusion.outcastmoderation.Commands;

import me.illusion.outcastmoderation.Moderation.Utilities;
import me.illusion.outcastmoderation.OutcastModeration;
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
                new Chat(ply, "&cUnable to vanish / unvanish yourself.").message(true);
                return true;
            }

            new Chat(ply, "&7You have toggled your vanish status: " + vanishString).message(true);
            return true;
        }

        String playerName = args[0];

        if (!new Utilities(playerName).vanish()) {
            new Chat(ply, "&cUnable to vanish / unvanish '" + playerName + "'.").message(true);
            return true;
        }

        boolean alreadyVanished = new Utilities(playerName).hasVanish();
        String vanishString = alreadyVanished ? "&c&lOFF" : "&a&lON";

        new Chat(ply, "&7You have toggled " + playerName + "'s vanish status: " + vanishString).message(true);
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
