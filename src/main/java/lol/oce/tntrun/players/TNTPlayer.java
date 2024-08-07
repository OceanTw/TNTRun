package lol.oce.tntrun.players;

import lol.oce.tntrun.match.Match;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Data
@AllArgsConstructor
public class TNTPlayer {

    Player player;
    int gamesPlayed;
    int gamesWon;
    int gamesLost;
}
