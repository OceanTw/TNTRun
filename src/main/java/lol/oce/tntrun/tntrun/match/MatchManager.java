package lol.oce.tntrun.tntrun.match;

import java.util.*;

public class MatchManager {
    private final List<Match> matches = new ArrayList<>();
    private final HashMap<String, Boolean> schematics = new HashMap<>();

    public void addMatch(Match match) {
        matches.add(match);
    }

    public void removeMatch(Match match) {
        matches.remove(match);
    }

    public Match getMatch(UUID id) {
        return matches.stream().filter(match -> match.getUuid().equals(id)).findFirst().orElse(null);
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
    public void startMatch(Match match) {
        // Start the match
    }


}
