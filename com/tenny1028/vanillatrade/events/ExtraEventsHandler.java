package com.tenny1028.vanillatrade.events;

import com.tenny1028.vanillatrade.VanillaTrade;
import com.tenny1028.vanillatrade.protection.AccessLevel;
import com.tenny1028.vanillatrade.protection.LockedContainer;
import com.tenny1028.vanillatrade.protection.ShopChest;
import com.tenny1028.vanillatrade.VanillaTradeState;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by jasper on 3/14/16.
 */
public class ExtraEventsHandler implements Listener {
	VanillaTrade plugin;

	public ExtraEventsHandler(VanillaTrade plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerBreakBlock(BlockBreakEvent e){
		if(e.getBlock().getState() instanceof Container){
			LockedContainer container = plugin.getLockedContainerConfigManager().getLockedContainer(e.getBlock().getLocation());
			if(container!=null){
				if(AccessLevel.hasPermission(container.getAccessLevelOf(e.getPlayer()),AccessLevel.FULL_ACCESS)){
					plugin.getLockedContainerConfigManager().unlockContainer(container);
					if(!plugin.isDoubleChest(container.getContainerBlock())) {
						e.getPlayer().sendMessage(ChatColor.GRAY + "This chest has been unlocked.");
					}
				}else{
					e.setCancelled(true);
					e.getPlayer().sendMessage(ChatColor.RED + "You do not own this chest.");
				}
			}
		}
	}

	@EventHandler
	public void onPlaceBlock(BlockPlaceEvent e){
		// handle whenever a player tries to expand a single chest's inventory by placing an unlocked chest beside it

		if(e.getBlock().getType().equals(Material.CHEST)) {
			final Block placedBlock = e.getBlockPlaced();

			// need to check if the chest will be a double chest but it doesn't know until next tick
			plugin.getServer().getScheduler().runTask(plugin, () -> {
				Chest placed = (Chest) placedBlock.getState();
				if (plugin.isDoubleChest(placed)) {
					// it is a double chest

					// find the sister chest to check for ownership
					Chest otherChest = plugin.getSisterChest(placed);
					LockedContainer container = plugin.getLockedContainerConfigManager().getLockedContainer(otherChest.getLocation());

					if (container != null) {
						// if container is not null, this chest is locked
						if (AccessLevel.hasPermission(container.getAccessLevelOf(e.getPlayer()),AccessLevel.FULL_ACCESS)) {
							// add placed chest to our databases and copy data from sister chest

							if(container instanceof ShopChest) {
								plugin.getLockedContainerConfigManager().saveShopChest(
										new ShopChest(container.getOwner(),
												e.getBlock().getLocation(),
												((ShopChest)container).getCost())
								);
							}else{
								plugin.getLockedContainerConfigManager().saveContainer(
										new LockedContainer(container.getOwner(),
												e.getBlock().getLocation(),
												container.getPublicAccessLevel(),
												container.getFriendsAccessLevel(),
												container.getFriends())
								);
							}
						} else {
							// can't cancel the event but we can break it naturally
							placedBlock.breakNaturally();
							e.getPlayer().sendMessage(ChatColor.RED + "You do not own this chest.");
						}
					}
				}
			});



		}
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e){
		plugin.setState(e.getPlayer(), VanillaTradeState.NONE);
	}
}
