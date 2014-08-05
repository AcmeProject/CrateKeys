package net.poweredbyhate.cratekeys;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CrateCommand implements CommandExecutor {

    public CrateKeys plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (sender.hasPermission("cratekeys.admin")) {
            if (args.length == 4) {
                if (args[0].equalsIgnoreCase("give")) {
                          if (sender.getServer().getPlayer(args[1]) != null) {
                              if (plugin.getConfig().getString(args[2]+".keyMaterial") != null) {
                                  ItemStack is = new ItemStack(Material.valueOf(plugin.getConfig().getString(args[2]+".keyMaterial")), Integer.valueOf(args[3]));
                                  ItemMeta im = is.getItemMeta();
                                  List<String> lores = new ArrayList<String>();
                                  for (String sexy : plugin.getConfig().getStringList(args[2]+".keyLore"))
                                  lores.add(ChatColor.translateAlternateColorCodes('&', sexy));
                                  im.setLore(lores);
                                  im.setDisplayName(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString(args[2]+".keyName")));
                                  is.addUnsafeEnchantment(Enchantment.DURABILITY, plugin.getConfig().getInt(args[2] + ".keyLevel"));
                                  Player p = Bukkit.getPlayer(args[1]);
                                  is.setItemMeta(im);
                                  p.getInventory().addItem(is);
                              } else {
                                  sender.sendMessage(ChatColor.RED + "That crate does not exist!");
                              }
                          } else {
                              sender.sendMessage(ChatColor.RED + "Player does not exist!");
                          }
                }
            }   else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("addcrate")) {
                     plugin.cSetter = sender.getName();
                    plugin.sCrate = true;
                    plugin.nCrate = args[1];
                    sender.sendMessage(ChatColor.GREEN + "Please click a block that you want to set as crate");
                }
            }
        }
        return false;
    }
}
