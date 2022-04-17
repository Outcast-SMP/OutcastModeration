package me.illusion.outcastmoderation.Commands;

import me.illusion.outcastmoderation.OutcastModeration;
import me.illusion.outcastmoderation.Util.Communication.Chat;
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

public class Staff implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player ply = (Player) sender;
        UUID pID = ply.getUniqueId();

        String errorMsg = new ConfigData(OutcastModeration.getInstance().chatMessages)
                .getStringFromConfig("chat.error.staff.incorrect-command-use");

        if (args.length <= 0) {
            new Chat(ply, errorMsg).message(true);
            return true;
        }

        StringBuilder sb = new StringBuilder();
        String message = Chat.makeDescription(sb, 0, args.length, args);

        String staffPermission = new ConfigData(OutcastModeration.getInstance().staff).getStringFromConfig("moderation.permissions.staff-chat");
        String staffWatermark = new ConfigData(OutcastModeration.getInstance().staff).getStringFromConfig("moderation.staff-chat.prefix");

        new Chat(message).privateMessage(ply, staffPermission, staffWatermark);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        String commandName = command.getName();

        if (commandName.equals("staff")) {
            List<String> itemList = new ArrayList<>();

            if (args.length > 0) {
                itemList.add("[message]");
            }
            return itemList;
        }
        return null;
    }
}
