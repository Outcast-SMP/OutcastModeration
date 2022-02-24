public class PlayerLeave implements Listener {
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        Player ply = e.getPlayer();
        UUID pID = ply.getUniqueId();

        Utilities.getVanishedPlayerList().remove(ply);
    }
}
