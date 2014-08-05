package net.poweredbyhate.cratekeys;

import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

/*
Target plugin http://www.spigotmc.org/resources/cratekeys-the-best-crate-plugin.830/
Sample config http://hastebin.com/wibeluxoki.vbs
 */

public class CrateKeys extends JavaPlugin implements Listener {

    List<String> crateLocs;
    String cSetter;
    String nCrate;
    boolean sCrate;


    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("ck").setExecutor(new CrateCommand());
        doConfigStuff();
    }

    public void onDisable() {
        cSetter = null;
        sCrate = false;
        nCrate = null;
    }

    @EventHandler
    public void onCrateClick(PlayerInteractEvent ev) {
        if (sCrate && ev.getPlayer().getName().equalsIgnoreCase(cSetter)) {
            setNewCrate(ev.getClickedBlock().getLocation(), ev.getPlayer());
            ev.setCancelled(true);
        }
        if (ev.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Player p = ev.getPlayer();
            if (ev.getClickedBlock().getLocation().toString().equalsIgnoreCase(crateLocz().toString())) {
                   String crateName = getCrateName(crateLocz().toString());
                checkPlayerHand(p, crateName);

            }
        }
    }

    public void setNewCrate(Location loc, Player p) {
        getConfig().getStringList("CrateLocations").add(loc.getBlockX()+"."+loc.getBlockY()+"."+loc.getBlockZ()+"."+nCrate);
        doConfigStuff();
        p.sendMessage(ChatColor.GREEN + "Block is now a crate " + nCrate);
        sCrate = false;
        cSetter = null;
        nCrate = null;
    }

    public void checkPlayerHand(Player p, String crate) {
        ItemStack is = p.getItemInHand();
        ItemMeta im = is.getItemMeta();
        if (im.getDisplayName() == null) return;
        if (im.getEnchants() == null) return;
           if (is.equals(Material.valueOf(getConfig().getString(crate+".keyMaterial")))) {
               if (im.getEnchants().containsKey(Enchantment.DURABILITY)) {
                   if (im.getEnchantLevel(Enchantment.DURABILITY) == getConfig().getInt(crate+"keyLevel")) {
                       rewardPlayer(p, crate);
                   }
               }
           }
    }

    public Location crateLocz() {
        for (String cL : crateLocs) {
            return getCrateLocs(cL);
        } return null;
    }

    public Location getCrateLocs(String m) {
        String[] split = m.split("\\.");
        return new Location(getServer().getWorld(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
    }

    public String getCrateName(String m) {
        String[] split = m.split("\\.");
        return split[4];
    }

    public void rewardPlayer(Player p, String crate) {
        p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString(crate+"playerMessage")));
        p.playSound(p.getLocation(), Sound.valueOf(getConfig().getString(crate+"keySound")), 10F, 1F);
        for (String broadcast : getConfig().getStringList(crate+"broadcastMessage")) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', broadcast.replace("%player%", p.getName())));
        }
        for (String cmds : getConfig().getStringList(crate+"commands")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmds.replace("%player%", p.getName()));
        }

    }


    public void doConfigStuff() {
        saveDefaultConfig();
        saveConfig();
        crateLocs = getConfig().getStringList("CrateLocations");
    }

}
