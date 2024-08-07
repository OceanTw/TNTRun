package lol.oce.tntrun.listeners;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import lol.oce.tntrun.TNTRun;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ArenaSetupListener implements Listener {

    private final TNTRun plugin = TNTRun.get();
    private Location pos1;
    private Location pos2;

    String name = null;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item != null && item.getType() == Material.GOLDEN_AXE) {
            name = item.getItemMeta().getDisplayName();
            if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                pos1 = event.getClickedBlock().getLocation();
                player.sendMessage("Position 1 set.");
                event.setCancelled(true);
            } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                pos2 = event.getClickedBlock().getLocation();
                player.sendMessage("Position 2 set.");
                if (pos1 != null && pos2 != null) {
                    try {
                        createSchematic(player, name);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    player.sendMessage("Arena setup complete.");
                }
            }


        }
    }

    public void createSchematic(Player player, String name) throws IOException {
        World world = BukkitAdapter.adapt(player.getWorld());
        BlockVector3 pos1 = BlockVector3.at(this.pos1.getX(), this.pos1.getY(), this.pos1.getZ());
        BlockVector3 pos2 = BlockVector3.at(this.pos2.getX(), this.pos2.getY(), this.pos2.getZ());
        CuboidRegion region = new CuboidRegion(world, pos1, pos2);


        Clipboard clipboard = new BlockArrayClipboard(region);


        try (EditSession editSession = WorldEdit.getInstance().newEditSession(world)) {
            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(region.getMinimumPoint())
                    .ignoreAirBlocks(false)
                    .build();
            Operations.complete(operation);
        }


        File schematicFile = new File(plugin.getDataFolder() + File.separator + "arenas", name + ".schem");
        ClipboardFormat format = ClipboardFormats.findByExtension("schem");

        try (ClipboardWriter writer = format.getWriter(Files.newOutputStream(schematicFile.toPath()))) {
            writer.write(clipboard);
            player.sendMessage("Schematic saved as " + schematicFile.getName());
        }
    }
}