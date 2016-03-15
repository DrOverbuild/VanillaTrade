package com.tenny1028.vanillatrade.events;

import com.tenny1028.vanillatrade.ShopChest;
import com.tenny1028.vanillatrade.ShopSetupState;
import com.tenny1028.vanillatrade.VanillaTrade;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by jasper on 3/14/16.
 */
public class PlayerInteractEventHandler implements Listener{
	VanillaTrade plugin;

	public PlayerInteractEventHandler(VanillaTrade plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e){
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (e.getClickedBlock() != null) {
				if (e.getClickedBlock().getType().equals(Material.CHEST)) {
					if(plugin.getSetupState(e.getPlayer()).equals(ShopSetupState.CHOOSE_CHEST)) {
						e.setCancelled(true);
						ShopChest shop = plugin.getShopConfigManager().getShopChest(e.getClickedBlock().getLocation());
						if (shop == null) {
							e.getPlayer().sendMessage(ChatColor.GOLD + "+++++++++++ SHOP SETUP +++++++++++");
							e.getPlayer().sendMessage(ChatColor.GRAY + "Setting up a new Shop chest.");
							e.getPlayer().sendMessage(ChatColor.GRAY + "Enter desired payment type.");
							e.getPlayer().sendMessage(ChatColor.GRAY + "Examples: " + ChatColor.GOLD + "diamond" + ChatColor.GRAY + " or " + ChatColor.GOLD + "gold_nugget");
							e.getPlayer().sendMessage(ChatColor.GOLD + "+++++++++++++++++++++++++++++++++");
							ShopSetupState state = ShopSetupState.CHOOSE_PAYMENT_TYPE;
							state.setCurrentShop(e.getClickedBlock().getLocation());
							plugin.setShopSetupState(e.getPlayer(),state);
						}else{
							if(e.getPlayer().getName().equals(shop.getOwner().getName())) {
								e.getPlayer().sendMessage(ChatColor.GOLD + "++++++++++++ SHOP SETUP" +
										" ++++++++++++");
								e.getPlayer().sendMessage(ChatColor.GRAY + "Editing an existing Shop chest.");
								e.getPlayer().sendMessage(ChatColor.GRAY + "Cost of items in chest: " + ChatColor.GOLD + shop.getCost().getAmount() + " " +
										shop.getCost().getType().name().toLowerCase() + ((shop.getCost().getAmount() > 1 || shop.getCost().getAmount() == 0) ? "s" : ""));
								e.getPlayer().sendMessage(ChatColor.GRAY + "Enter desired payment type.");
								e.getPlayer().sendMessage(ChatColor.GRAY + "Examples: " + ChatColor.GOLD + "diamond" + ChatColor.GRAY + " or " + ChatColor.GOLD + "gold_nugget");
								e.getPlayer().sendMessage(ChatColor.GOLD + "+++++++++++++++++++++++++++++++++++");
								ShopSetupState state = ShopSetupState.CHOOSE_PAYMENT_TYPE;
								state.setCurrentShop(e.getClickedBlock().getLocation());
								plugin.setShopSetupState(e.getPlayer(), state);
							}else{
								e.getPlayer().sendMessage(ChatColor.RED + "This shop chest is owned by " + shop.getOwner().getName());
							}
						}
					}
				} else if (e.getClickedBlock().getType().equals(Material.SIGN)) {

				}
			}
		}
	}
}
