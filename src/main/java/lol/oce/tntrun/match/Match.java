package lol.oce.tntrun.match;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockTypes;
import lol.oce.tntrun.TNTRun;
import lol.oce.tntrun.players.TNTPlayer;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

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
    Location spawn;

    public void setup() throws IOException {
        World world = BukkitAdapter.adapt(Bukkit.getWorld("matches"));
        if (world == null) {
            throw new IOException("World not found");
        }
        File schematicFile = TNTRun.get().getMatchManager().getSchematicFile(arenaSchematic);
        if (!schematicFile.exists()) {
            throw new IOException("Schematic not found " + schematicFile.getName());
        }
        ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
        try (ClipboardReader reader = format.getReader(new FileInputStream(schematicFile))) {
            Clipboard clipboard = reader.read();


            try (EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world(world).build()) {
                BlockVector3 to = findLocation(world);
                Bukkit.getLogger().info("Pasting schematic at " + to);
                ForwardExtentCopy copy = new ForwardExtentCopy(clipboard, clipboard.getRegion(), clipboard.getOrigin(), editSession, to);
                copy.setCopyingEntities(true);
                copy.setCopyingBiomes(true);
                Operations.complete(copy);
                Location loc = new Location(Bukkit.getWorld("matches"), to.x(), to.y(), to.z());
                spawn = calculateSpawnLocation(loc);
                Bukkit.getLogger().info("Spawn location set to " + spawn);
            }
        }
    }


    private Location calculateSpawnLocation(Location location) {
        int radius = 150;
        for (double x = location.getX() - (double)radius; x <= location.getX() + (double)radius; x += 1.0) {
            for (double y = location.getY() - (double)radius; y <= location.getY() + (double)radius; y += 1.0) {
                for (double z = location.getZ() - (double)radius; z <= location.getZ() + (double)radius; z += 1.0) {
                    Location loc = new Location(location.getWorld(), x, y, z);
                    if (loc.getBlock().getType().equals(Material.PLAYER_HEAD)) {
                        // change the location direction with the skull
                        loc.setPitch(0);
                        Block self = loc.getBlock().getRelative(BlockFace.SELF);
                        switch (loc.getBlock().getFace(self)) {
                            case NORTH:
                                loc.setYaw(-180);
                                break;
                            case SOUTH:
                                loc.setYaw(0);
                                break;
                            case EAST:
                                loc.setYaw(-90);
                                break;
                            case WEST:
                                loc.setYaw(90);
                                break;
                        }
                        loc.setX(loc.getX() + 0.5);
                        loc.setZ(loc.getZ() + 0.5);
                        return loc;
                    }
                }
            }
        }
        return null;
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
                TNTPlayer winner = players.get(0);
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
