package net.charter.orion_pax.OasisExtras;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class OasisExtras extends JavaPlugin{

	ConsoleCommandSender console;
	HashMap<String, Location> frozen = new HashMap<String, Location>();
	HashMap<String, Set<String>> mounted = new HashMap<String, Set<String>>();
	String effectslist,savemsg1,savemsg2;
	int default_min, default_max, ndt,bcastcount,warningtime;
	long  savealltimer,bcasttimer;
	List<String> bcastmsgs;
	Plugin OasisChat;
	File file = new File("plugins/OasisChat/paxserrorlog.txt");

	OasisExtrasCMD extras = new OasisExtrasCMD(this);
	OasisExtrasTask task = new OasisExtrasTask(this);

	@Override
	public void onEnable() {
		try {
			saveDefaultConfig();
			Bukkit.getPluginManager().registerEvents(new OasisExtrasListener(this), this);
			getCommand("enableme").setExecutor(new OasisExtrasCommand(this));
			getCommand("disableme").setExecutor(new OasisExtrasCommand(this));
			getCommand("brocast").setExecutor(new OasisExtrasCommand(this));
			getCommand("spook").setExecutor(new OasisExtrasCommand(this));
			getCommand("freeze").setExecutor(new OasisExtrasCommand(this));
			getCommand("drunk").setExecutor(new OasisExtrasCommand(this));
			getCommand("slap").setExecutor(new OasisExtrasCommand(this));
			getCommand("random").setExecutor(new OasisExtrasCommand(this));
			getCommand("oasisextras").setExecutor(new OasisExtrasCommand(this));
			getCommand("mount").setExecutor(new OasisExtrasCommand(this));
			getCommand("unmount").setExecutor(new OasisExtrasCommand(this));
			getCommand("godly").setExecutor(new OasisExtrasCommand(this));
			setup();
			effectslist = extras.effects();
			console = Bukkit.getServer().getConsoleSender();
			getLogger().info("OasisExtras has been enabled!");
		} catch (Throwable e) {

			printStackTrace(e, "OnEnable");
		}
	}

	@Override
	public void onDisable(){
		task.savethistask.cancel();
		task.savethisworld.cancel();
		task.bcasttask.cancel();
		task.remindmetask.cancel();
		getLogger().info("OasisExtras has been disabled!");
	}

	public void setup(){
		Set<String> flist = getConfig().getConfigurationSection("frozen").getKeys(false);
		for (String playername : flist){
			String fworld = getConfig().getString("frozen." + playername + ".world");
			Location loc = new Location(Bukkit.getWorld(fworld), getConfig().getDouble("frozen." + playername + ".x"), getConfig().getDouble("frozen." + playername + ".y"), getConfig().getDouble("frozen." + playername + ".z"));
			frozen.put(playername, loc);
			loc=null;
		}
		warningtime = getConfig().getInt("warningdelay")*1200;
		bcastmsgs = getConfig().getStringList("broadcastmessages");
		bcasttimer = getConfig().getInt("broadcasttimer")*1200;
		savemsg1 = getConfig().getString("saveingmsg");
		savemsg2 = getConfig().getString("saveingmsgcomplete");
		savealltimer = getConfig().getInt("savealltimer")*1200;
		default_min = Integer.parseInt(getConfig().getString("min_default_location"));
		default_max = Integer.parseInt(getConfig().getString("max_default_location"));
		ndt = Integer.parseInt(getConfig().getString("default_invulnerability_ticks"));
		bcastcount = 0;
		task.savethistask.runTaskTimer(this, 10, 12000);
		task.savethisworld.runTaskTimer(this, savealltimer, savealltimer);
		task.bcasttask.runTaskTimer(this, extras.randomNum(0, 18000), bcasttimer);
		task.remindmetask.runTaskTimer(this, savealltimer-warningtime, savealltimer);
	}

	public void savefrozen(Player player){
		getConfig().set("frozen." + player.getName() + ".world", player.getLocation().getWorld().getName());
		getConfig().set("frozen." + player.getName() + ".x", player.getLocation().getBlockX());
		getConfig().set("frozen." + player.getName() + ".y", player.getLocation().getBlockY());
		getConfig().set("frozen." + player.getName() + ".z", player.getLocation().getBlockZ());
	}

	public void removefrozen(Player player){
		getConfig().set("frozen." + player.getName(), null);
	}

	void printStackTrace(Throwable t, String cmd){
		try {
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss,a");
			Date date = new Date();
			if (!file.exists()){file.createNewFile();}
			StringWriter sw = new StringWriter();
			PrintWriter out = new PrintWriter(new FileWriter(file, true));
			PrintWriter pw = new PrintWriter(sw);
			out.println("**BEGIN**");
			out.println("OasisExtras");
			out.println(dateFormat.format(date));
			out.println(cmd);
			t.printStackTrace(pw);
			for(String l: sw.toString().replace("\r", "").split("\n")){
				out.println(l);
				getServer().broadcast(l, "oasis.debug");
			}
			pw.close();
			out.println("**END**");
			out.close();
			sw.close();
		} catch (IOException e) {

		}
	}
}
