package lol.oce.tntrun.tntrun.match;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.world.World;
import lol.oce.tntrun.tntrun.TNTRun;
import lol.oce.tntrun.tntrun.players.TNTPlayer;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Builder(setterPrefix = "set")
@Data
public class Match {

    UUID uuid;
    String arenaSchematic;
    List<TNTPlayer> players;
    MatchStatus status;

    public void setup() throws IOException {
        World world = BukkitAdapter.adapt(Bukkit.getWorld("matches"));
        File schematicFile = new File(TNTRun.getPluginPath() + "/arenas/" + arenaSchematic + ".schem");
        if (!schematicFile.exists()) {
            schematicFile = new File(TNTRun.getPluginPath() + "/arenas/" + arenaSchematic + ".schematic");
            if (!schematicFile.exists()) {
                throw new IOException("Schematic file not found");
            }
        }
        ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
        try (ClipboardReader reader = format.getReader(new FileInputStream(schematicFile))) {
            Clipboard clipboard = reader.read();
            try (EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world(world).build()) {
                BlockVector3 to = findLocation(world);
                ForwardExtentCopy copy = new ForwardExtentCopy(clipboard, clipboard.getRegion(), clipboard.getOrigin(), editSession, to);
                copy.setCopyingEntities(true);
                copy.setCopyingBiomes(true);
                Operations.complete(copy);
            }
        }
    }

    public void addPlayer(TNTPlayer player) {
        players.add(player);
        if (players.size() >= TNTRun.get().getConfigManager().getSettingsFile().getConfiguration().getInt("match.min-players")) {
            MatchManager matchManager = TNTRun.get().getMatchManager();
            matchManager.startCountdown(this);
        }
    }

    public void removePlayer(TNTPlayer player) {
        players.remove(player);
        if (players.size() < TNTRun.get().getConfigManager().getSettingsFile().getConfiguration().getInt("match.min-players")) {
            status = MatchStatus.WAITING;
        }
        if (status == MatchStatus.INGAME) {
            if (players.size() == 1) {
                TNTPlayer winner = players.getFirst();
                players.clear();
                winner.getPlayer().sendMessage("You won the match!");
                status = MatchStatus.ENDING;
            }
        }
    }

    private BlockVector3 findLocation(World world) {
        int baseX = 10000;
        int baseY = 100;
        int baseZ = 10000;
        int offset = 500;

        MatchManager matchManager = TNTRun.get().getMatchManager();
        int matchCount = matchManager.getMatches().size();

        int x = baseX + (matchCount * offset);
        int y = baseY;
        int z = baseZ + (matchCount * offset);

        return BlockVector3.at(x, y, z);
    }
}