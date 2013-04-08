package net.charter.orion_pax.OasisExtras;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class OasisExtrasListener implements Listener{

	private OasisExtras plugin;

	public OasisExtrasListener(OasisExtras plugin){
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnPlayerMove(PlayerMoveEvent event) {
		try {
			if (plugin.frozen.containsKey(event.getPlayer().getName())){

				int fromX=(int)event.getFrom().getX();
				int fromY=(int)event.getFrom().getY();
				int fromZ=(int)event.getFrom().getZ();
				int toX=(int)event.getTo().getX();
				int toY=(int)event.getTo().getY();
				int toZ=(int)event.getTo().getZ();

				if(fromX!=toX||fromZ!=toZ||fromY!=toY){
					event.getPlayer().teleport(event.getFrom());
					event.getPlayer().sendMessage(ChatColor.RED + "YOU CAN NOT MOVE, YOU'RE " + ChatColor.AQUA + "FROZEN!");
					event.setCancelled(true);
				}
			}
		} catch (Throwable e) {

			plugin.printStackTrace(e, "OnPlayerMove");
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnPlayerBreakBlock(BlockBreakEvent event) {
		try {
			if (plugin.frozen.containsKey(event.getPlayer().getName())){
				event.getPlayer().sendMessage(ChatColor.RED + "YOU CAN NOT DESTROY BLOCKS WHILE " + ChatColor.AQUA + "FROZEN!");
				event.setCancelled(true);
			}
			Set set = plugin.frozen.entrySet();
			Iterator i = set.iterator();
			while(i.hasNext()) {
				Map.Entry me = (Map.Entry)i.next();
				Location loc = (Location) me.getValue();
				if (event.getBlock().getLocation().getBlockY() == loc.getY()-1){
					if (event.getBlock().getLocation().getBlockX() == loc.getX()) {
						if (event.getBlock().getLocation().getBlockZ() == loc.getZ()) {
							if (!(event.getPlayer()
									.hasPermission("oasischat.staff.a"))) {
								event.getPlayer()
								.sendMessage(
										ChatColor.RED
										+ "YOU CAN NOT DESTROY BLOCKS UNDER THE "
										+ ChatColor.AQUA
										+ "FROZEN"
										+ ChatColor.RED
										+ " PLAYER!");
								event.setCancelled(true);
							}
						}
					}
				}
			}
		} catch (Throwable e) {

			plugin.printStackTrace(e, "OnPlayerBreakBlock");
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnPlayerRespawn(PlayerRespawnEvent event){
		try {
			Player player = event.getPlayer();
			if (plugin.frozen.containsKey(player.getName())) {
				event.setRespawnLocation((Location) plugin.frozen.get(event.getPlayer().getName()));
				event.getPlayer().sendMessage(ChatColor.RED + "RESPAWNED AT YOUR " + ChatColor.AQUA + "CHILLED " + ChatColor.RED + "LOCATION!");
			}
		} catch (Throwable e) {

			plugin.printStackTrace(e, "OnPlayerRespawn");
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnPlayerCommand(PlayerCommandPreprocessEvent event) {
		try {
			if (plugin.frozen.containsKey(event.getPlayer().getName())){
				if (event.getMessage().contains("/")){
					event.setCancelled(true);
					event.getPlayer().sendMessage(ChatColor.RED + "COMMANDS ARE DISABLED WHILE " + ChatColor.AQUA + "FROZEN!");
				}
			}
		} catch (Throwable e) {

			plugin.printStackTrace(e, "OnPlayerCommand");
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void OnPlayerPlaceBlock(BlockPlaceEvent event){
		try {
			if (plugin.frozen.containsKey(event.getPlayer().getName())){
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + "YOU CAN NOT PLACE BLOCKS WHILE " + ChatColor.AQUA + "FROZEN!");
			}
		} catch (Throwable e) {
			plugin.printStackTrace(e, "OnPlayerPlaceBlock");
		}
	}

}
