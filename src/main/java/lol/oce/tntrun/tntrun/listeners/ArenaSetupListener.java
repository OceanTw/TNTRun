package lol.oce.tntrun.tntrun.listeners;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.world.World;
import lol.oce.tntrun.tntrun.TNTRun;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ArenaSetupListener implements Listener {

    private final TNTRun plugin = TNTRun.get();
    private BlockVector3 pos1;
    private BlockVector3 pos2;


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand().getType() != Material.GOLDEN_AXE && player.getInventory().getItemInMainHand().getItemMeta().getDisplayName() == "Golden Axe") {
            return;
        }

        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }

        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            pos1 = BlockVector3.at(block.getX(), block.getY(), block.getZ());
            player.sendMessage("Position 1 set to " + pos1);
            event.setCancelled(true);
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            pos2 = BlockVector3.at(block.getX(), block.getY(), block.getZ());
            player.sendMessage("Position 2 set to " + pos2);
            event.setCancelled(true);
        }

        if (pos1 != null && pos2 != null) {
            try {
                createSchematic(player, player.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
                player.getInventory().remove(player.getInventory().getItemInMainHand());
            } catch (IOException e) {
                player.sendMessage("An error occurred while creating the schematic.");
                e.printStackTrace();
            }
        }
    }

    private void createSchematic(Player player, String name) throws IOException {
        World world = BukkitAdapter.adapt(player.getWorld());
        CuboidRegion region = new CuboidRegion(world, pos1, pos2);
        Clipboard clipboard = new BlockArrayClipboard(region);
        File schematicFile = new File(plugin.getDataFolder(), "arenas/" + player.getName() + name + ".schem");
        ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);

        try (ClipboardWriter writer = format.getWriter(new FileOutputStream(schematicFile))) {
            writer.write(clipboard);
            player.sendMessage("Schematic saved as " + schematicFile.getName());
        }
    }
}