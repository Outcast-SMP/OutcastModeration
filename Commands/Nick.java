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
