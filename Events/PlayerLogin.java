public class PlayerLogin implements Listener {
    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent e) {
        Player ply = e.getPlayer();
        UUID pID = ply.getUniqueId();

        Punish.banCheck(e, ply);
    }
}
