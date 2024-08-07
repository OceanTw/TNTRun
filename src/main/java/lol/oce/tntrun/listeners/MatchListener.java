package lol.oce.tntrun.listeners;

import lol.oce.tntrun.TNTRun;
import lol.oce.tntrun.match.MatchStatus;
import lol.oce.tntrun.players.TNTPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitScheduler;

public class MatchListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (TNTRun.get().getPlayerManager().getPlayer(event.getPlayer().getUniqueId()) == null) {
            return;
        }
        if (TNTRun.get().getPlayerManager().getPlayerMatch().get(TNTRun.get().getPlayerManager().getPlayer(event.getPlayer().getUniqueId())) == null) {
            return;
        }
        if (TNTRun.get().getPlayerManager().getPlayerMatch().get(TNTRun.get().getPlayerManager().getPlayer(event.getPlayer().getUniqueId())).getStatus() != MatchStatus.INGAME) {
            return;
        }
        Block block = event.getPlayer().getLocation().subtract(0, 1, 0).getBlock();
        Block block2 = event.getPlayer().getLocation().subtract(0, 2, 0).getBlock();
        if (block.getType() == Material.SAND || block.getType() == Material.GRAVEL) {
            BukkitScheduler scheduler = event.getPlayer().getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(TNTRun.get(), () -> block.setType(Material.AIR), 5L);
        }
        if (block2.getType() == Material.SAND || block2.getType() == Material.GRAVEL) {
            BukkitScheduler scheduler = event.getPlayer().getServer().getScheduler();
            scheduler.scheduleSyncDelayedTask(TNTRun.get(), () -> block2.setType(Material.AIR), 5L);
        }

        if (event.getPlayer().getLocation().getY() < 30) {
            TNTPlayer tntPlayer = TNTRun.get().getPlayerManager().getPlayer(event.getPlayer().getUniqueId());
            TNTRun.get().getPlayerManager().getPlayerMatch().get(tntPlayer).getPlayers().remove(tntPlayer);
        }
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof FallingBlock) {
            event.setCancelled(true);
        }
    }
}
