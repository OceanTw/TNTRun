package lol.oce.tntrun.tntrun.listeners;

import lol.oce.tntrun.tntrun.TNTRun;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class MatchListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (TNTRun.get().getPlayerManager().getPlayer(event.getPlayer().getUniqueId()).getMatch() == null) {
            return;
        }
        Block block = event.getPlayer().getLocation().subtract(0, 1, 0).getBlock();
        if (block.getType() == Material.SAND || block.getType() == Material.GRAVEL) {
            BukkitScheduler scheduler = event.getPlayer().getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(TNTRun.get(), () -> block.setType(Material.AIR), 5L);
        }

        if (event.getPlayer().getLocation().getY() < 90) {
            TNTRun.get().getPlayerManager().getPlayer(event.getPlayer().getUniqueId()).getMatch().getPlayers().remove(event.getPlayer());
        }
    }
}
