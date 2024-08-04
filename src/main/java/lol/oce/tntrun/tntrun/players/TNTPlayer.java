package lol.oce.tntrun.tntrun.players;

import lol.oce.tntrun.tntrun.match.Match;
import lol.oce.tntrun.tntrun.match.MatchManager;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TNTPlayer {

    UUID uuid;
    int gamesPlayed;
    int gamesWon;
    int gamesLost;
    boolean inGame;
    Match match;
}
