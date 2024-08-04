package lol.oce.tntrun.tntrun.utils;

import lol.oce.tntrun.tntrun.TNTRun;
import lombok.Getter;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@Getter
public class ConfigFile {
    private final File file;
    private final YamlConfiguration configuration;
    private final TNTRun plugin = TNTRun.get();

    public ConfigFile(String name) {
        this.file = new File(plugin.getDataFolder(), name + ".yml");

        if (!file.exists()) {
            plugin.saveResource(name + ".yml", false);
        }

        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }

    public void save() {
        try {
            configuration.save(file);
        } catch (IOException ignored) {
        }
    }
}