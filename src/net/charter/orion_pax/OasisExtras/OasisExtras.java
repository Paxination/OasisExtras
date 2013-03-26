package net.charter.orion_pax.OasisExtras;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class OasisExtras extends JavaPlugin{

    HashMap<String, Location> frozen = new HashMap<String, Location>();
    String effectslist;
    World world;
    int ndt;
    int default_min;
    int default_max;
    Plugin OasisChat;

    OasisExtrasCMD extras = new OasisExtrasCMD(this);

    @Override
    public void onEnable() {
	this.saveDefaultConfig();
	Bukkit.getPluginManager().registerEvents(new OasisExtrasListener(this), this);
	try {
	    world = getServer().getWorld(getConfig().getString("world"));
	} catch (Exception e) {
	    getLogger().info("This is the first time net.charter.orion_pax.OasisExtras has been ran!");
	    getLogger().info("Please edit the config file and add the world name to it and reload!");
	    e.printStackTrace();
	}
	default_min = Integer.parseInt(getConfig().getString("min_default_location"));
	default_max = Integer.parseInt(getConfig().getString("max_default_location"));
	ndt = Integer.parseInt(getConfig().getString("default_invulnerability_ticks"));
	getCommand("enableme").setExecutor(new OasisExtrasCommand(this));
	getCommand("disableme").setExecutor(new OasisExtrasCommand(this));
	getCommand("brocast").setExecutor(new OasisExtrasCommand(this));
	getCommand("spook").setExecutor(new OasisExtrasCommand(this));
	getCommand("freeze").setExecutor(new OasisExtrasCommand(this));
	getCommand("drunk").setExecutor(new OasisExtrasCommand(this));
	getCommand("slap").setExecutor(new OasisExtrasCommand(this));
	getCommand("random").setExecutor(new OasisExtrasCommand(this));
	getCommand("extrasreload").setExecutor(new OasisExtrasCommand(this));
	loadfrozen();
	effectslist = extras.effects();
	getLogger().info("OasisExtras has been enabled!");
    }

    @Override
    public void onDisable(){
	getLogger().info("net.charter.orion_pax.OasisExtras has been disabled!");
    }

    public void loadfrozen(){
	Set<String> flist = getConfig().getConfigurationSection("frozen").getKeys(false);
	for (String playername : flist){
	    Location loc = new Location(world, getConfig().getDouble("frozen." + playername + ".x"), getConfig().getDouble("frozen." + playername + ".y"), getConfig().getDouble("frozen." + playername + ".z"));
	    frozen.put(playername, loc);
	}
    }

    public void savefrozen(Player player){
	getConfig().set("frozen." + player.getName() + ".x", player.getLocation().getX());
	getConfig().set("frozen." + player.getName() + ".y", player.getLocation().getY());
	getConfig().set("frozen." + player.getName() + ".z", player.getLocation().getZ());
    }

    public void removefrozen(Player player){
	getConfig().set("frozen." + player.getName(), null);
    }

    void printStackTrace(Throwable t){
	StringWriter sw = new StringWriter();
	PrintWriter pw = new PrintWriter(sw);
	t.printStackTrace(pw);
	for(String l: sw.toString().replace("\r", "").split("\n"))
	    getServer().broadcast(l, "oasis.staff.dev");
	pw.close();
	try {
	    sw.close();
	} catch(IOException e) {

	}
    }

}
