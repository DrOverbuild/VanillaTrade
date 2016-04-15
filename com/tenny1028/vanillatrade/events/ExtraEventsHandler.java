package com.tenny1028.vanillatrade.events;

import com.tenny1028.vanillatrade.protection.AccessLevel;
import com.tenny1028.vanillatrade.protection.LockedContainer;
import com.tenny1028.vanillatrade.protection.ShopChest;
import com.tenny1028.vanillatrade.VanillaTradeState;
import com.tenny1028.vanillatrade.VanillaTrade;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
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
		if(e.getBlock().getType().equals(Material.CHEST)){
			LockedContainer container = plugin.getLockedContainerConfigManager().getLockedContainer(e.getBlock().getLocation());
			if(container!=null){
				if(AccessLevel.hasPermission(container.getAccessLevelOf(e.getPlayer()),AccessLevel.FULL_ACCESS)){
					plugin.getLockedContainerConfigManager().unlockContainer(container);
					if(!plugin.isDoubleChest(container.getChest())) {
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
		if(e.getBlock().getType().equals(Material.CHEST)) {
			Chest chest = plugin.getSisterChest((Chest)e.getBlock().getState());
			if (chest != null) {
				LockedContainer container = plugin.getLockedContainerConfigManager().getLockedContainer(chest.getLocation());
				if (container != null) {
					if (AccessLevel.hasPermission(container.getAccessLevelOf(e.getPlayer()),AccessLevel.FULL_ACCESS)) {
						if(container instanceof ShopChest) {
							plugin.getLockedContainerConfigManager().saveShopChest(new ShopChest(container.getOwner(), e.getBlock().getLocation(), ((ShopChest)container).getCost()));
						}else{
							plugin.getLockedContainerConfigManager().saveContainer(new LockedContainer(container.getOwner(),
									e.getBlock().getLocation(),container.getPublicAccessLevel(),container.getFriendsAccessLevel(),container.getFriends()));
						}
					} else {
						e.setCancelled(true);
						e.getPlayer().sendMessage(ChatColor.RED + "You do not own this chest.");
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e){
		plugin.setState(e.getPlayer(), VanillaTradeState.NONE);
	}
}
