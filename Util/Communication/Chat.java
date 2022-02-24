public class Chat {
    public static String watermark = new ConfigData(OutcastModeration.getInstance().config).getStringFromConfig("moderation.prefix") + " ";

    private Player ply;
    private CommandSender sender;
    private String message;

    public Chat(String message) {
        this.message = ChatColor.translateAlternateColorCodes('&', message);
    }

    public Chat(CommandSender sender, String message) {
        this.sender = sender;
        this.message = ChatColor.translateAlternateColorCodes('&', message);
    }

    public Chat(Player ply, String message) {
        this.ply = ply;
        this.message = ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String makeDescription(StringBuilder sb, int minArg, int maxArg, String[] args) {
        for (int i = minArg; i < maxArg; i++){
            sb.append(args[i]).append(" ");
        }
        return sb.toString().trim();
    }

    public void message(boolean bWatermark) {
        ply.sendMessage(bWatermark ? watermark + message : message);
    }

    public void messageSender(boolean bWatermark) {
        sender.sendMessage(bWatermark ? watermark + message : message);
    }

    public void privateMessage(String permission, String watermark) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.hasPermission(permission)) {
                onlinePlayer.sendMessage(watermark + " " + message);
            }
        }
    }

    public void privateMessage(Player ply, String permission, String watermark) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.hasPermission(permission)) {
                onlinePlayer.sendMessage(watermark + " " + ChatColor.AQUA + ply.getName() + ": " + ChatColor.GRAY + message);
            }
        }
    }
}
