public class Ban implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player ply = (Player) sender;
        UUID pID = ply.getUniqueId();

        String errorMsg = "&cIncorrect command usage. Please use /ban <name> <time> <reason>";

        if (args.length < 3) {
            new Chat(ply, errorMsg).message(true);
            return true;
        }

        String playerName = args[0];
        String banTime = args[1];

        StringBuilder sb = new StringBuilder();
        String reason = Chat.makeDescription(sb, 2, args.length, args);

        if (!new Punish(playerName).ban(ply, banTime, reason)) {
            new Chat(ply, "&cUnable to ban the specified player '" + playerName + "'.").message(true);
        }

        new Chat(ply, "&a" + playerName + " has been successfully banned.").message(true);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        String commandName = command.getName();

        if (commandName.equals("ban") || commandName.equals("b")) {
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
