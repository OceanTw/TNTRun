package lol.oce.tntrun;

import lol.oce.tntrun.listeners.ArenaSetupListener;
import lol.oce.tntrun.listeners.MatchListener;
import lol.oce.tntrun.listeners.PlayerListener;
import lol.oce.tntrun.match.Match;
import lol.oce.tntrun.match.MatchManager;
import lol.oce.tntrun.commands.JoinCommand;
import lol.oce.tntrun.configs.ConfigManager;
import lol.oce.tntrun.match.MatchStatus;
import lol.oce.tntrun.players.PlayerManager;
import lol.oce.tntrun.utils.EmptyChunkGenerator;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

@Getter
public final class TNTRun extends JavaPlugin {

    private static TNTRun instance;

    private ConfigManager configManager;
    private MatchManager matchManager;
    private PlayerManager playerManager;

    @Override
    public void onEnable() {
        instance = this;

        if (getServer().getWorld("matches") == null) {
            WorldCreator wc = new WorldCreator("matches");
            wc.generator(new EmptyChunkGenerator());
            wc.createWorld();
        }

        Bukkit.getWorld("matches").setAutoSave(false);
        Bukkit.getWorld("matches").setDifficulty(org.bukkit.Difficulty.PEACEFUL);

        configManager = new ConfigManager();
        configManager.load();
        matchManager = new MatchManager();
        matchManager.load();
        playerManager = new PlayerManager();

        if (!new File(getDataFolder(), "arenas").exists()) {
            new File(getDataFolder(), "arenas").mkdir();
        }

        getServer().getPluginManager().registerEvents(new MatchListener(), this);
        getServer().getPluginManager().registerEvents(new ArenaSetupListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        getCommand("join").setExecutor(new JoinCommand());


        if (matchManager.getSchematics().isEmpty()) {
            getLogger().warning("No arenas found, TNTRun will not work without arenas!");
        } else {
            for (int i = 0; i < 5; i++) {
                matchManager.addMatch(Match.builder()
                        .setUuid(UUID.randomUUID())
                        .setArenaSchematic(matchManager.getArena())
                        .setPlayers(new ArrayList<>())
                        .setStatus(MatchStatus.WAITING)
                        .build());
            }
        }

        getLogger().info("TNTRun has been enabled!");
    }

    @Override
    public void onDisable() {

    }

    public static TNTRun get() {
        return instance;
    }

    public static String getPluginPath() {
        return TNTRun.getPlugin(TNTRun.class).getDataFolder().getAbsolutePath();
    }
}
