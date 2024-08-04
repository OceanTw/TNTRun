package lol.oce.tntrun.tntrun.players;

import lol.oce.tntrun.tntrun.match.Match;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;

@Data
@AllArgsConstructor
public class TNTPlayer {

    Player player;
    int gamesPlayed;
    int gamesWon;
    int gamesLost;
    boolean inGame;
    Match match;
}
