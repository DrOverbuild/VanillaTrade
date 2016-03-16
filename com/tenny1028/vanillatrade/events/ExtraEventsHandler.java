package com.tenny1028.vanillatrade.events;

import com.tenny1028.vanillatrade.ShopChest;
import com.tenny1028.vanillatrade.ShopState;
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
			ShopChest shop = plugin.getShopConfigManager().getShopChest(e.getBlock().getLocation());
			if(shop!=null){
				if(e.getPlayer().getName().equals(shop.getOwner().getName())||e.getPlayer().hasPermission("vanillatrade.op")){
					if(!plugin.isDoubleChest(shop.getChest())) {
						plugin.getShopConfigManager().removeShopChest(shop);
						e.getPlayer().sendMessage("");
						e.getPlayer().sendMessage(ChatColor.GOLD + "+++++++++++ SHOP SETUP +++++++++++");
						e.getPlayer().sendMessage(ChatColor.GRAY + "Shop has been removed.");
						e.getPlayer().sendMessage(ChatColor.GOLD + "+++++++++++++++++++++++++++++++++");
						e.getPlayer().sendMessage("");
					}else{
						plugin.getShopConfigManager().removeShopChest(shop);
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
			if (chest!= null){
				ShopChest shop = plugin.getShopConfigManager().getShopChest(chest.getLocation());
				if(shop!=null){
					if(shop.getOwner().getName().equals(e.getPlayer().getName())||e.getPlayer().hasPermission("vanillatrade.op")){
						plugin.getShopConfigManager().saveShopChest(new ShopChest(shop.getOwner(),e.getBlock().getLocation(),shop.getCost()));
					}else{
						e.setCancelled(true);
						e.getPlayer().sendMessage(ChatColor.RED + "You do not own this chest.");
					}
				}
			}
		}
	}


	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e){
		plugin.setState(e.getPlayer(), ShopState.NONE);
	}
}
