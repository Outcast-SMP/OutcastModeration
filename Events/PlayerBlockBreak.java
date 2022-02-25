public class PlayerBlockBreak implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player ply = e.getPlayer();
        UUID pID = ply.getUniqueId();

        String bypassPermission = new ConfigData(OutcastModeration.getInstance().antixray).getStringFromConfig("moderation.permissions.antixray-bypass");

        if (ply.isOp() || ply.hasPermission(bypassPermission) || ply.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        if (!Punish.mineBanned(e, ply)) {
            Punish.oreCheck(e, ply);
        }
    }
}
