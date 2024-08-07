package lol.oce.tntrun.match;

import lol.oce.tntrun.TNTRun;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MatchManager {
    private final List<Match> matches = new ArrayList<>();
    @Getter
    private final HashMap<String, Boolean> schematics = new HashMap<>();

    public void load() {
        File folder = new File(TNTRun.get().getDataFolder(), "arenas");
        if (!folder.exists()) {
            folder.mkdir();
        }
        for (File file : folder.listFiles()) {
            schematics.put(file.getName().replace(".schem", ""), false);
        }
    }

    public File getSchematicFile(String schematic) {
        File folder = new File(TNTRun.get().getDataFolder(), "arenas");
        return new File(folder, schematic + ".schem");
    }

    public void addMatch(Match match) {
        try {
            match.setup();
        } catch (Exception e) {
            e.printStackTrace();
        }
        matches.add(match);
    }

    public List<Match> getMatches() {
        return matches;
    }


    public String getArena() {
        for (String schematic : schematics.keySet()) {

            schematic = schematic.replace(".schem", "");
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
                    match.getPlayers().forEach(player -> player.getPlayer().teleport(match.getSpawn()));
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
