package me.illusion.outcastmoderation.Commands;

import me.illusion.outcastmoderation.OutcastModeration;
import me.illusion.outcastmoderation.Util.Communication.Chat;
import me.illusion.outcastmoderation.Util.Config.ConfigData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Reload implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player ply = (Player) sender;
        UUID pID = ply.getUniqueId();

        String errorMsg = new ConfigData(OutcastModeration.getInstance().chatMessages)
                .getStringFromConfig("chat.error.reload.incorrect-command-use");

        if (args.length <= 0) {
            new Chat(ply, errorMsg).message(true);
            return true;
        }

        String commandName = args[0];

        if (commandName.equals("reload")) {
            if (!ply.hasPermission(new ConfigData(OutcastModeration.getInstance().config).getStringFromConfig("moderation.permissions.reload"))) {
                new Chat(ply, new ConfigData(OutcastModeration.getInstance().chatMessages)
                        .getStringFromConfig("chat.error.reload.insufficient-permission")).message(true);
                return true;
            }

            OutcastModeration.getInstance().config.reload();
            OutcastModeration.getInstance().staff.reload();
            OutcastModeration.getInstance().antixray.reload();
            OutcastModeration.getInstance().chatMessages.reload();
            new Chat(ply, new ConfigData(OutcastModeration.getInstance().chatMessages)
                    .getStringFromConfig("chat.success.reload-message")).message(true);
            return true;
        }

        new Chat(ply, errorMsg).message(true);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        String commandName = command.getName();

        if (commandName.equals("om")) {
            List<String> itemList = new ArrayList<>();

            switch (args.length) {
                case 1:
                    itemList.add("reload");
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
