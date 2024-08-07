package lol.oce.tntrun.listeners;

import lol.oce.tntrun.match.Match;
import lol.oce.tntrun.TNTRun;
import lol.oce.tntrun.players.TNTPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();;
        if (TNTRun.get().getPlayerManager().getPlayer(player.getUniqueId()) == null) {
            TNTPlayer tntPlayer = new TNTPlayer(player, 0, 0, 0);
            TNTRun.get().getPlayerManager().addPlayer(tntPlayer);
        } else {
            TNTPlayer tntPlayer = TNTRun.get().getPlayerManager().getPlayer(player.getUniqueId());
            tntPlayer.setPlayer(player);
            TNTRun.get().getPlayerManager().addPlayer(tntPlayer);
        }


        if (TNTRun.get().getPlayerManager().getPlayers().size() % 10 == 0) {

            Match.builder()
                    .setUuid(player.getUniqueId())
                    .setArenaSchematic(TNTRun.get().getMatchManager().getArena())
                    .setPlayers(new ArrayList<>())
                    .build();
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        TNTPlayer tntPlayer = TNTRun.get().getPlayerManager().getPlayer(player.getUniqueId());
        if (tntPlayer != null) {
            TNTRun.get().getPlayerManager().removePlayer(tntPlayer);
            TNTRun.get().getPlayerManager().getPlayerMatch().remove(tntPlayer);
        }
    }
}