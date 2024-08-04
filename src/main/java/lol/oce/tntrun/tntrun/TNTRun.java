package lol.oce.tntrun.tntrun;

import lol.oce.tntrun.tntrun.commands.ArenaCommand;
import lol.oce.tntrun.tntrun.configs.ConfigManager;
import lol.oce.tntrun.tntrun.listeners.ArenaSetupListener;
import lol.oce.tntrun.tntrun.listeners.MatchListener;
import lol.oce.tntrun.tntrun.match.Match;
import lol.oce.tntrun.tntrun.match.MatchManager;
import lol.oce.tntrun.tntrun.players.PlayerManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.foreign.Arena;
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

        configManager = new ConfigManager();
        matchManager = new MatchManager();
        playerManager = new PlayerManager();

        configManager.load();

        getServer().getPluginManager().registerEvents(new MatchListener(), this);
        getServer().getPluginManager().registerEvents(new ArenaSetupListener(), this);

        getCommand("tntrun").setExecutor(new ArenaCommand());

        // Create 5 matches on startup
        for (int i = 0; i < 5; i++) {
            matchManager.addMatch(Match.builder()
                    .setUuid(UUID.randomUUID())
                    .setArenaSchematic(matchManager.getArena())
                    .setPlayers(new ArrayList<>())
                    .build());
        }

        getLogger().info("TNTRun has been enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static TNTRun get() {
        return instance;
    }

    public static String getPluginPath() {
        return TNTRun.getPlugin(TNTRun.class).getDataFolder().getAbsolutePath();
    }
}
