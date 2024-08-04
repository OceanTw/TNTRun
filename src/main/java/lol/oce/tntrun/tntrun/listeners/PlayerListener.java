package lol.oce.tntrun.tntrun.listeners;

import lol.oce.tntrun.tntrun.TNTRun;
import lol.oce.tntrun.tntrun.match.Match;
import lol.oce.tntrun.tntrun.players.TNTPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        TNTPlayer tntPlayer = TNTRun.get().getPlayerManager().getPlayer(player.getUniqueId());
        if (tntPlayer == null) {
            tntPlayer = new TNTPlayer(player.getUniqueId(), 0, 0, 0, false, null);
            TNTRun.get().getPlayerManager().addPlayer(tntPlayer);
        }

        // create a new match when 10 players are online, another one when 20, etc
        if (TNTRun.get().getPlayerManager().getPlayers().size() % 10 == 0) {
            // create a new match
            Match.builder()
                    .setUuid(player.getUniqueId())
                    .setArenaSchematic("arena")
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
        }
    }
}