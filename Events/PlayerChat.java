public class PlayerChat implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        Player ply = e.getPlayer();
        UUID pID = ply.getUniqueId();

        if (Utilities.getNickedPlayerList().containsKey(ply.getName())) {
            String nickName = Utilities.getNickedPlayerList().get(ply.getName());
            ply.setDisplayName(nickName + ChatColor.RESET);
        }

        Punish.muteCheck(e, ply);
        Punish.chatDelayCheck(e, ply);
    }
}
