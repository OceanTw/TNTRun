package lol.oce.tntrun.tntrun.commands;

import lol.oce.tntrun.tntrun.TNTRun;
import lol.oce.tntrun.tntrun.utils.CC;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class ArenaCommand implements CommandExecutor {

    private final TNTRun plugin = TNTRun.get();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (!sender.hasPermission("tntrun.admin")) {
            sender.sendMessage(CC.color("&cYou do not have permission to use this command."));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("Usage: /arena <create/delete>");
            return true;
        }

        Player player = (Player) sender;
        if (args[0].equalsIgnoreCase("create")) {
            ItemStack item = new ItemStack(Material.GOLDEN_AXE);
            item.getItemMeta().setDisplayName(args[1]);
            player.getInventory().addItem(item);
            player.sendMessage("You have been given the setup wand.");
        }
        if (args[0].equalsIgnoreCase("delete")) {
            player.sendMessage("The arena has been deleted.");
            new File(plugin.getDataFolder().getAbsolutePath() + File.separator
                    + "arenas", "arena.schematic").delete();
        }
        return true;
    }
}