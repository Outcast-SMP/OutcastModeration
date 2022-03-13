package me.illusion.outcastmoderation.Commands;

import me.illusion.outcastmoderation.Moderation.Utilities;
import me.illusion.outcastmoderation.Util.Communication.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Nick implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player ply = (Player) sender;
        UUID pID = ply.getUniqueId();

        String errorMsg = "&cIncorrect command usage. Please use /nick <nickname>";

        if (args.length <= 0) {
            new Chat(ply, errorMsg).message(true);
            return true;
        }

        String nickName = ChatColor.translateAlternateColorCodes('&', args[0]);

        if (nickName.equalsIgnoreCase("none")) {
            if (!new Utilities(ply).removeNick()) {
                new Chat(ply, "&cThere was a problem removing your nickname.").message(true);
                return true;
            }
            
            ply.setDisplayName(ply.getName());
            new Chat(ply, "&7You no longer have a nickname.").message(true);
            return true;
        }

        if (!new Utilities(ply).nick(nickName)) {
            new Chat(ply, "&cUnable to set your nickname.").message(true);
            return true;
        }

        new Chat(ply, "&7You have now been nicknamed " + nickName + "&7.").message(true);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        String commandName = command.getName();

        if (commandName.equals("nick")) {
            List<String> itemList = new ArrayList<>();

            switch (args.length) {
                case 1:
                    itemList.add("[nickname]");
                    itemList.add("none");
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
