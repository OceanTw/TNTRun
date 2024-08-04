package lol.oce.tntrun.tntrun.match;

import lol.oce.tntrun.tntrun.TNTRun;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MatchManager {
    private final List<Match> matches = new ArrayList<>();
    private final HashMap<String, Boolean> schematics = new HashMap<>();

    public void addMatch(Match match) {
        matches.add(match);
    }

    public List<Match> getMatches() {
        return matches;
    }

    // get a arena that hasn't been used yet, if all arenas are used, choose a random one
    public String getArena() {
        for (String schematic : schematics.keySet()) {
            // if all arenas are used, choose a random one
            if (!schematics.get(schematic)) {
                schematics.put(schematic, true);
                return schematic;
            }
            Random random = new Random();
            int index = random.nextInt(schematics.size());
            String arena = (String) schematics.keySet().toArray()[index];
            return arena;
        }
        return null;
    }

    public Match getAvailableMatch() {
        for (Match match : matches) {
            if (match.getPlayers().size() < 10) {
                return match;
            }
        }
        return null;
    }

    // start a waiting match
    public void startCountdown(Match match) {
        match.setStatus(MatchStatus.WAITING);
        match.getPlayers().forEach(player -> player.getPlayer().sendMessage("Match starting in 30 seconds!"));
        new BukkitRunnable() {
            int seconds = 30;

            @Override
            public void run() {
                if (seconds == 0) {
                    match.setStatus(MatchStatus.INGAME);
                    match.getPlayers().forEach(player -> player.getPlayer().sendMessage("Match has started!"));
                    cancel();
                    return;
                }
                if (seconds == 10 || seconds <= 5) {
                    match.getPlayers().forEach(player -> player.getPlayer().sendMessage("Match starting in " + seconds + " seconds!"));
                }
                seconds--;
            }
        }.runTaskTimer(TNTRun.get(), 0, 20);
    }



}
