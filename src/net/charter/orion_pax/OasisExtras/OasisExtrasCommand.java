package net.charter.orion_pax.OasisExtras;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class OasisExtrasCommand implements CommandExecutor{
    
    private OasisExtras plugin; // pointer to your main class, unrequired if you don't need methods from the main class

    public OasisExtrasCommand(OasisExtras plugin){
	this.plugin = plugin;
    }
    
    enum Commands {
	SLAP, FREEZE, DRUNK, SPOOK, ENABLEME,
	DISABLEME, BROCAST, RANDOM, EXTRASRELOAD
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
	try {
	    Commands mycommand = Commands.valueOf(cmd.getName().toUpperCase());
	    
	    Player player = null;
	    if (sender instanceof Player){
	        player = (Player) sender;
	    }
	    
	    switch (mycommand) {
	    
	    case EXTRASRELOAD:
	        plugin.world = plugin.getServer().getWorld(plugin.getConfig().getString("world"));
	        plugin.loadfrozen();
	        plugin.ndt = Integer.parseInt(plugin.getConfig().getString("default_invulnerability_ticks"));
	        plugin.default_min = Integer.parseInt(plugin.getConfig().getString("min_default_location"));
	        plugin.default_max = Integer.parseInt(plugin.getConfig().getString("max_default_location"));
	        return true;
	    
	    case RANDOM:
	        if (!(sender instanceof Player)) {
	    	sender.sendMessage("This command cannot be used from the console.");
	    	return true;
	        }   
	        World default_world = player.getWorld();
	        if (args.length == 0) {
	    	// use the config.yml values
	    	int default_min = Integer.parseInt(plugin.getConfig().getString("min_default_location"));
	    	int default_max = Integer.parseInt(plugin.getConfig().getString("max_default_location"));
	    	player.setNoDamageTicks(plugin.ndt);
	    	player.teleport(plugin.extras.getRandomLoc(null, default_min, default_max, default_world));
	    	player.sendMessage(ChatColor.GOLD+"You have been randomly teleported!");
	    	return true;
	        } else {
	    	sender.sendMessage("/random teleports you to a random location.");
	    	return false;
	        }
	    case DRUNK:
	        if (args.length > 0) {
	    	Player target = sender.getServer().getPlayer(args[0]);
	    	if (target == null){
	    	    sender.sendMessage(ChatColor.RED + args[0] + ChatColor.GOLD + " is not online!");
	    	    return true;
	    	}
	    	int duration = 600;
	    	if (args.length == 2) {
	    	    duration = Integer.parseInt(args[1]);
	    	    duration = duration*20;
	    	}
	    	target.getPlayer().addPotionEffect(
	    		new PotionEffect(PotionEffectType.CONFUSION, duration,10));
	    	sender.sendMessage(ChatColor.GOLD + target.getName() + " is now DRUNK!");
	    	return true;
	        } else {
	    	sender.sendMessage(ChatColor.RED + "Too few arguments!");
	    	return false;
	        }
	    case FREEZE:
	        if (args.length > 0) {
	    	if (sender.getServer().getPlayer(args[0]) != null) {
	    	    Player target = sender.getServer().getPlayer(args[0]);
	    	    if (target.hasPermission("OasisChat.staff.a")) {
	    		sender.sendMessage("Can not freeze staff");
	    	    } else {
	    		if (plugin.frozen.containsKey(target.getName())) {
	    		    plugin.frozen.remove(target.getName());
	    		    sender.sendMessage(ChatColor.RED + target.getName() + ChatColor.BLUE + " is now THAWED!");
	    		    target.sendMessage(ChatColor.GOLD + "You are now " + ChatColor.BLUE + "THAWED!");
	    		    plugin.removefrozen(target);
	    		    plugin.saveConfig();
	    		    return true;
	    		} else {
	    		    plugin.frozen.put(target.getName(),target.getLocation());
	    		    sender.sendMessage(ChatColor.RED + target.getName() + ChatColor.AQUA + " is now FROZEN!");
	    		    target.sendMessage(ChatColor.GOLD + "You are now " + ChatColor.AQUA + "FROZEN!");
	    		    plugin.savefrozen(target);
	    		    plugin.saveConfig();
	    		    return true;
	    		}
	    	    }
	    	} else {
	    	    sender.sendMessage(ChatColor.GOLD + args[0] + " is not online!");
	    	    return true;
	    	}
	        } else {
	    	sender.sendMessage(ChatColor.RED + "Too few arguments!");
	    	return false;
	        }
	        break;
	        
	    case BROCAST:
	        if (args.length > 0) {
	    	StringBuffer buffer = new StringBuffer();
	    	buffer.append(args[0]);
	    	for (int i = 1; i < args.length; i++) {
	    	    buffer.append(" ");
	    	    buffer.append(args[i]);
	    	}
	    	String message = buffer.toString();
	    	plugin.getServer().broadcastMessage(
	    		ChatColor.RED + "[" + ChatColor.DARK_RED + "Brocast" + ChatColor.RED + "] " + ChatColor.GOLD + message);
	    	return true;
	        } else {
	    	return false;
	        }

	    case SPOOK:
	        if (args.length > 0) {
	    	Player target = sender.getServer().getPlayer(args[0]);
	    	if (target == null){
	    	    sender.sendMessage(ChatColor.RED + args[0] + ChatColor.GOLD + " is not online!");
	    	    return false;
	    	}
	    	int soundtoplay = 0;
	    	try { 
	    	    Integer.parseInt(args[1]); 
	    	} catch(NumberFormatException e) { 
	    	    sender.sendMessage(ChatColor.GOLD + args[1] + " is not an integer!");
	    	    return false; 
	    	}
	    	if (args.length == 2) {
	    	    soundtoplay = Integer.parseInt(args[1]);
	    	}
	    	switch (soundtoplay) {
	    	case 1:
	    	    target.playSound(target.getLocation(), Sound.GHAST_MOAN, 1, 1);
	    	    sender.sendMessage(ChatColor.YELLOW + "Now Playing Ghast Moan on " + ChatColor.RED + target.getName());
	    	    return true;
	    	case 2:
	    	    target.playSound(target.getLocation(), Sound.GHAST_SCREAM, 1, 1);
	    	    sender.sendMessage(ChatColor.YELLOW + "Now Playing Ghast Scream 1 on " + ChatColor.RED + target.getName());
	    	    return true;
	    	case 3:
	    	    target.playSound(target.getLocation(), Sound.GHAST_SCREAM2, 1, 1);
	    	    sender.sendMessage(ChatColor.YELLOW + "Now Playing Ghast Scream 2 on " + ChatColor.RED + target.getName());
	    	    return true;
	    	case 4:
	    	    target.playSound(target.getLocation(), Sound.CREEPER_HISS, 1, 1);
	    	    sender.sendMessage(ChatColor.YELLOW + "Now Playing Creeper Hiss on " + ChatColor.RED + target.getName());
	    	    return true;
	    	case 5:
	    	    target.playSound(target.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
	    	    sender.sendMessage(ChatColor.YELLOW + "Now Playing Enderdragon Growl on " + ChatColor.RED + target.getName());
	    	    return true;
	    	case 6:
	    	    target.playSound(target.getLocation(), Sound.ENDERMAN_SCREAM, 1, 1);
	    	    sender.sendMessage(ChatColor.YELLOW + "Now Playing Enderman Scream on " + ChatColor.RED + target.getName());
	    	    return true;
	    	case 7:
	    	    target.playSound(target.getLocation(), Sound.EXPLODE, 1, 1);
	    	    sender.sendMessage(ChatColor.YELLOW + "Now Playing TNT Explosion on " + ChatColor.RED + target.getName());
	    	    return true;
	    	case 8:
	    	    target.playSound(target.getLocation(), Sound.WITHER_SPAWN, 1, 1);
	    	    sender.sendMessage(ChatColor.YELLOW + "Now Playing Wither Spawn on " + ChatColor.RED + target.getName());
	    	    return true;
	    	case 9:
	    	    target.playSound(target.getLocation(), Sound.ANVIL_LAND, 1, 1);
	    	    sender.sendMessage(ChatColor.YELLOW + "Now Playing Anvil Land on " + ChatColor.RED + target.getName());
	    	    return true;
	    	case 10:
	    	    target.playSound(target.getLocation(), Sound.ZOMBIE_PIG_ANGRY, 1, 1);
	    	    sender.sendMessage(ChatColor.YELLOW + "Now Playing Angry Zombie Pigman on " + ChatColor.RED + target.getName());
	    	    return true;
	    	default:
	    	    target.playSound(target.getLocation(), Sound.GHAST_MOAN, 1, 1);
	    	    sender.sendMessage(ChatColor.YELLOW + "Now Playing Ghast Moan on " + ChatColor.RED + target.getName());
	    	    return true;
	    	}
	        } else {
	    	sender.sendMessage(ChatColor.RED + "Too few arguments!");
	    	return false;
	        }

	    case SLAP:
	        if (!(sender instanceof Player)){
	    	plugin.getServer().broadcastMessage(ChatColor.RED + "CONSOLE has slapped " + args[0]);
	        }
	        if (args.length == 0){
	    	return false;
	        }

	        if (args[0].equalsIgnoreCase("all")){
	    	String msg;
	    	if (args.length > 1){
	    	    StringBuffer buffer = new StringBuffer();
	    	    for (int i = 1; i < args.length; i++) {
	    		buffer.append(" ");
	    		buffer.append(args[i]);
	    	    }
	    	    msg = buffer.toString();
	    	} else {
	    	    msg = "none";
	    	}
	    	Player[] onlinePlayers = Bukkit.getServer().getOnlinePlayers();
	    	for (Player oplayer : onlinePlayers){
	    	    plugin.extras.slap(oplayer.getName(),sender,msg);
	    	}
	    	return true;

	        } else {
	    	String msg;
	    	if (args.length > 1){
	    	    StringBuffer buffer = new StringBuffer();
	    	    for (int i = 1; i < args.length; i++) {
	    		buffer.append(" ");
	    		buffer.append(args[i]);
	    	    }
	    	    msg = buffer.toString();
	    	} else {
	    	    msg = "none";
	    	}
	    	plugin.extras.slap(args[0],sender,msg);
	    	return true;
	        }
	    case ENABLEME:
	        if (args.length == 0) {
	    	plugin.getServer().broadcastMessage(ChatColor.GOLD + sender.getName() + " is " + ChatColor.GREEN + "ENABLED!");
	    	return true;
	        } else if (args.length == 1) {

	    	plugin.getServer().broadcastMessage(ChatColor.GOLD + args[0] + " is " + ChatColor.GREEN + "ENABLED!");
	    	return true;
	        }
	        return true;

	    case DISABLEME:
	        if (args.length == 0) {
	    	plugin.getServer().broadcastMessage(ChatColor.GOLD + sender.getName() + " is " + ChatColor.RED + "DISABLED!");
	    	return true;
	        } else if (args.length == 1) {
	    	plugin.getServer().broadcastMessage(ChatColor.GOLD + args[0] + " is " + ChatColor.RED + "DISABLED!");
	    	return true;
	        }
	        return true;
	    }
	} catch (Throwable e) {
	    e.printStackTrace();
	    plugin.printStackTrace(e);
	}
	return false;
    }

}
