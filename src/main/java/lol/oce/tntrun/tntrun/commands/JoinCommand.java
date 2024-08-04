package lol.oce.tntrun.tntrun.commands;

import lol.oce.tntrun.tntrun.TNTRun;
import lol.oce.tntrun.tntrun.match.Match;
import lol.oce.tntrun.tntrun.match.MatchManager;
import lol.oce.tntrun.tntrun.players.TNTPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class JoinCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player player = (Player) commandSender;
        TNTPlayer tntPlayer = TNTRun.get().getPlayerManager().getPlayer(player.getUniqueId());
        if (tntPlayer == null) {
            tntPlayer = new TNTPlayer(player, 0, 0, 0, false, null);
            TNTRun.get().getPlayerManager().addPlayer(tntPlayer);
        }
        if (tntPlayer.getMatch() != null) {
            player.sendMessage("You are already in a match!");
            return true;
        }
        MatchManager matchManager = TNTRun.get().getMatchManager();
        Match match = matchManager.getAvailableMatch();
        if (match == null) {
            player.sendMessage("There are no matches available.");
            return true;
        }
        match.addPlayer(tntPlayer);
        player.sendMessage("You have joined a match!");
        return true;
    }
}
