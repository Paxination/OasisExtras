package net.charter.orion_pax.OasisExtras;

import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class OasisExtrasCMD {

	private OasisExtras plugin;

	public OasisExtrasCMD(OasisExtras plugin){
		this.plugin = plugin;
	}

	public void slap(String name, CommandSender sender, String msg){
		String message,message2;
		Vector vector = new Vector(randomNum(-3,3), 0, randomNum(-3,3));
		Player player = plugin.getServer().getPlayer(name);
		if (msg.equalsIgnoreCase("none")){
			message = ChatColor.RED + sender.getName() + " Slapped you!";
			message2 = ChatColor.GRAY + "You slapped " + player.getName() + "!";
		} else {
			message = ChatColor.RED + sender.getName() + " Slapped you for" + msg + "!";
			message2 = ChatColor.GRAY + "You slapped " + player.getName() + " for " + msg + "!";
		}
		((LivingEntity) player).damage(0);
		player.setNoDamageTicks(200);
		player.setVelocity(vector);
		player.sendMessage(message);
		sender.sendMessage(message2);
	}

	public PotionEffect effect(String what){
		if (what == "DRUNK"){
			return new PotionEffect(PotionEffectType.CONFUSION, 30,
					10);
		}
		return null;
	}

	public String effects(){
		StringBuilder sb = new StringBuilder();
		for (PotionEffectType pe : PotionEffectType.values()) {
			if (sb.length() > 0) {
				sb.append(", "); //separate values with a comma and space
			}
			if (pe != null){
				sb.append(pe.getName());
			}
		}
		return sb.toString();
	}

	public int randomNum(Integer lownum, Integer highnum) {
		Random rand = new Random();
		int randomNum = rand.nextInt(highnum - lownum + 1) + lownum;
		return randomNum;
	}

	public Location getRandomLoc(Location loc, int min, int max, World world) {
		// Location coordinates  
		boolean test = false;// variable to tell while loop that we found good area
		//int loop=0;
		while (test == false){
			//loop++;
			double x = this.randomNum(min, max) + 0.5;
			double y = this.randomNum(64, 75);
			double z = this.randomNum(min, max) + 0.5;

			loc = new Location(world, x, y, z);//Location to tp to, and players bottom half
			Location block1 = new Location(world, x, (y-1), z);//Block under player
			Location block2 = new Location(world, x, (y + 1), z);//player location top
			if ((block2.getBlock().isEmpty() == true) && //is this air at top of player?
					(loc.getBlock().isEmpty() == true) && //is this air at bottom of player?
					(block1.getBlock().isLiquid() == false) && // lava n water
					(block1.getBlock().getTypeId() !=0)){ // air under player
				test=true;
			}
		}
		Chunk thischunk = loc.getChunk();
		thischunk.load(true);
		return loc;
	}
}
