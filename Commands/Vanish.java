public class Vanish implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player ply = (Player) sender;
        UUID pID = ply.getUniqueId();

        if (args.length <= 0) {
            if (!new Utilities(ply).vanish()) {
                new Chat(ply, "&cUnable to vanish / unvanish yourself.").message(true);
            }
            new Chat(ply, "&7You are now vanished from other players...").message(true);
            return true;
        }

        String playerName = args[0];

        if (!new Utilities(playerName).vanish()) {
            new Chat(ply, "&cUnable to vanish / unvanish '&c&l" + playerName + "&7'.").message(true);
        }

        new Chat(ply, "&7You are now vanished from other players...").message(true);
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
