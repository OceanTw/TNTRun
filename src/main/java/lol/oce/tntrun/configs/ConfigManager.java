package lol.oce.tntrun.configs;

import lol.oce.tntrun.TNTRun;
import lol.oce.tntrun.utils.ConfigFile;
import lombok.Getter;

@Getter
public class ConfigManager {

    private final TNTRun plugin;
    private ConfigFile settingsFile;
    private ConfigFile localeFile;
    private ConfigFile scoreboardFile;
    private ConfigFile menuFile;


    public ConfigManager() {
        this.plugin = TNTRun.get();
    }

    public void load() {
        this.settingsFile = new ConfigFile("settings");
        this.localeFile = new ConfigFile("locale");
        this.scoreboardFile = new ConfigFile("scoreboard");
        this.menuFile = new ConfigFile("menu");
    }
}
