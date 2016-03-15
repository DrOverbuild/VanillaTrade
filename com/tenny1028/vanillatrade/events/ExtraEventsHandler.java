package com.tenny1028.vanillatrade.events;

import com.tenny1028.vanillatrade.ShopChest;
import com.tenny1028.vanillatrade.ShopSetupState;
import com.tenny1028.vanillatrade.VanillaTrade;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
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
				if(e.getPlayer().getName().equals(shop.getOwner().getName())){
					plugin.getShopConfigManager().removeShopChest(shop);
					e.getPlayer().sendMessage("");
					e.getPlayer().sendMessage(ChatColor.GOLD + "+++++++++++ SHOP SETUP +++++++++++");
					e.getPlayer().sendMessage(ChatColor.GRAY + "Shop has been removed.");
					e.getPlayer().sendMessage(ChatColor.GOLD + "+++++++++++++++++++++++++++++++++");
					e.getPlayer().sendMessage("");
				}
			}
		}
	}

	@EventHandler
	public void onPlayerLeave(PlayerQuitEvent e){
		plugin.setShopSetupState(e.getPlayer(), ShopSetupState.NONE);
	}
}
