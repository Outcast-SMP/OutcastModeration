public class PlayerJoin implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player ply = e.getPlayer();
        UUID pID = ply.getUniqueId();

        for (Player vanishedPlayers : Utilities.getVanishedPlayerList()) {
            ply.hidePlayer(OutcastModeration.getInstance(), vanishedPlayers);
        }
    }
}
