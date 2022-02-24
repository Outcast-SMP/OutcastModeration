public class PlayerMove implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player ply = e.getPlayer();
        UUID pID = ply.getUniqueId();

        if (Punish.getFrozenPlayerList().contains(ply)) {
            e.setCancelled(true);
        }
    }
}
