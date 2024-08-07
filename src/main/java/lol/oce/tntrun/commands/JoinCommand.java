package lol.oce.tntrun.commands;

import lol.oce.tntrun.TNTRun;
import lol.oce.tntrun.match.Match;
import lol.oce.tntrun.match.MatchManager;
import lol.oce.tntrun.players.TNTPlayer;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class JoinCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) commandSender;
        TNTPlayer tntPlayer = TNTRun.get().getPlayerManager().getPlayer(player.getUniqueId());
        if (tntPlayer == null) {
            tntPlayer = new TNTPlayer(player, 0, 0, 0);
            TNTRun.get().getPlayerManager().addPlayer(tntPlayer);
        }
        if (TNTRun.get().getPlayerManager().getPlayerMatch().get(tntPlayer) != null) {
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
        TNTRun.get().getPlayerManager().getPlayerMatch().put(tntPlayer, match);
        Location spawnLocation = match.getSpawn();
        if (spawnLocation == null) {
            player.sendMessage("Failed to find the spawn location.");
            return true;
        }

        player.teleport(spawnLocation);
        return true;
    }
}