package com.tenny1028.vanillatrade.events;

import com.tenny1028.vanillatrade.ShopChest;
import com.tenny1028.vanillatrade.ShopSetupState;
import com.tenny1028.vanillatrade.VanillaTrade;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

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

		if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			return;
		}

		if(e.getClickedBlock() == null){
			return;
		}

		if(!e.getClickedBlock().getType().equals(Material.CHEST)){
			return;
		}

		ShopChest shop = plugin.getShopConfigManager().getShopChest(e.getClickedBlock().getLocation());
		if (shop == null) {
			if(plugin.getSetupState(e.getPlayer()).equals(ShopSetupState.CHOOSE_CHEST)) {
				e.setCancelled(true);
				e.getPlayer().sendMessage(ChatColor.GOLD + "+++++++++++ SHOP SETUP +++++++++++");
				e.getPlayer().sendMessage(ChatColor.GRAY + "Setting up a new Shop chest.");
				e.getPlayer().sendMessage(ChatColor.GRAY + "Enter desired payment type.");
				e.getPlayer().sendMessage(ChatColor.GRAY + "Examples: " + ChatColor.GOLD + "diamond" + ChatColor.GRAY + " or " + ChatColor.GOLD + "gold_nugget");
				e.getPlayer().sendMessage(ChatColor.GOLD + "+++++++++++++++++++++++++++++++++");
				ShopSetupState state = ShopSetupState.CHOOSE_PAYMENT_TYPE;
				state.setCurrentShop(e.getClickedBlock().getLocation());
				plugin.setShopSetupState(e.getPlayer(), state);
			}
		}else{
			if(plugin.getSetupState(e.getPlayer()).equals(ShopSetupState.CHOOSE_CHEST)) {
				if (e.getPlayer().getName().equals(shop.getOwner().getName()) || e.getPlayer().hasPermission("vanillatrade.op")) {
					e.setCancelled(true);
					e.getPlayer().sendMessage(ChatColor.GOLD + "++++++++++++ SHOP SETUP ++++++++++++");
					e.getPlayer().sendMessage(ChatColor.GRAY + "Editing an existing Shop chest.");
					e.getPlayer().sendMessage(ChatColor.GRAY + "Cost of items in chest: " + ChatColor.GOLD + shop.getCost().getAmount() + " " +
							shop.getCost().getType().name().toLowerCase() + ((shop.getCost().getAmount() > 1 || shop.getCost().getAmount() == 0) ? "s" : ""));
					e.getPlayer().sendMessage(ChatColor.GRAY + "Enter desired payment type.");
					e.getPlayer().sendMessage(ChatColor.GRAY + "Examples: " + ChatColor.GOLD + "diamond" + ChatColor.GRAY + " or " + ChatColor.GOLD + "gold_nugget");
					e.getPlayer().sendMessage(ChatColor.GOLD + "+++++++++++++++++++++++++++++++++++");
					ShopSetupState state = ShopSetupState.CHOOSE_PAYMENT_TYPE;
					state.setCurrentShop(e.getClickedBlock().getLocation());
					plugin.setShopSetupState(e.getPlayer(), state);
				}
			}else if(!e.getPlayer().getName().equals(shop.getOwner().getName())){
				e.setCancelled(true);
				openInventory(shop,e.getPlayer());
			}
		}
	}

	public void openInventory(ShopChest shop, Player p){
		Inventory chestInv = ((Chest)shop.getLocation().getBlock().getState()).getInventory();
		Inventory inv = plugin.getServer().createInventory(p,chestInv.getSize(),ChatColor.BOLD + "Trade with " + shop.getOwner().getName());

		int i = 0;
		for(ItemStack item:chestInv.getContents()){
			if(item!=null){
				if(!item.getType().equals(shop.getCost().getType())){
					ItemStack copy = new ItemStack(item);
					String lore = ChatColor.GOLD + "Cost: " + shop.getCost().getAmount() + " " +
							shop.getCost().getType().name().replace("_", " ").toLowerCase() + ((shop.getCost().getAmount()==1)?"":"s");
					ItemMeta copyMeta = copy.getItemMeta();
					copyMeta.setLore(Collections.singletonList(lore));
					copy.setItemMeta(copyMeta);
					inv.setItem(i,copy);
				}
			}
			i++;
		}

		p.openInventory(inv);
	}
}
