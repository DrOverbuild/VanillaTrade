package com.tenny1028.vanillatrade.events;

import com.tenny1028.vanillatrade.util.itemstack.ItemStackManager;
import com.tenny1028.vanillatrade.VanillaTrade;
import com.tenny1028.vanillatrade.VanillaTradeState;
import com.tenny1028.vanillatrade.protection.ShopChest;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by jasper on 3/14/16.
 */
public class PlayerAsyncChatEventHandler implements Listener {
	VanillaTrade plugin;

	public PlayerAsyncChatEventHandler(VanillaTrade plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e){
		VanillaTradeState state = plugin.getState(e.getPlayer());

		if(state.equals(VanillaTradeState.NONE)){
			return;
		}

		if(e.getMessage().equalsIgnoreCase("cancel")){
			e.setCancelled(true);

			e.getPlayer().sendMessage(ChatColor.GRAY + "Cancelled setup.");

			plugin.setState(e.getPlayer(), VanillaTradeState.NONE);
			return;
		}

		if(state.equals(VanillaTradeState.SHOP_SETUP_CHOOSE_PAYMENT_TYPE)){
			e.setCancelled(true);

			Material paymentType = null;

			ItemStack payment = ItemStackManager.parsePayment(e.getMessage());

			if(payment == null){
				e.getPlayer().sendMessage("");
				e.getPlayer().sendMessage(ChatColor.GOLD + e.getMessage() + ChatColor.GRAY +" is not a valid item. Please try again.");

				return;
			}

			if(state.getCurrentBlock()!=null) {
				ShopChest shop = plugin.getLockedContainerConfigManager().getShopChest(state.getCurrentBlock());
				if(shop == null){
					shop = new ShopChest(e.getPlayer(),state.getCurrentBlock(),payment);
				}
				shop.setCost(payment);
				plugin.getLockedContainerConfigManager().saveShopChests(shop);

				e.getPlayer().sendMessage("");
				e.getPlayer().sendMessage(ChatColor.GRAY + "Payment type set to " + ChatColor.GOLD +
						ItemStackManager.itemStackToHumanReadableFormat(payment));
				e.getPlayer().sendMessage("");
				e.getPlayer().sendMessage(ChatColor.GOLD + "+++++++++++ SHOP SETUP +++++++++++");
				e.getPlayer().sendMessage(ChatColor.GRAY + "Enter desired payment amount.");
				e.getPlayer().sendMessage(ChatColor.GOLD + "+++++++++++++++++++++++++++++++++");


				state = VanillaTradeState.SHOP_SETUP_CHOOSE_PAYMENT_AMOUNT;
				state.setCurrentBlock(shop.getLocation());
				plugin.setState(e.getPlayer(),state);
			}

		}else if(state.equals(VanillaTradeState.SHOP_SETUP_CHOOSE_PAYMENT_AMOUNT)){
			e.setCancelled(true);
			int paymentAmount = 1;

			try{
				paymentAmount = Integer.parseInt(e.getMessage());
			}catch (NumberFormatException ex){
				e.getPlayer().sendMessage("");
				e.getPlayer().sendMessage(ChatColor.GRAY + "Please enter a number.");
				return;
			}

			ShopChest shop = plugin.getLockedContainerConfigManager().getShopChest(state.getCurrentBlock());

			if(paymentAmount < 0 || paymentAmount > shop.getCost().getMaxStackSize()){
				e.getPlayer().sendMessage("");
				e.getPlayer().sendMessage(ChatColor.GRAY + "Please enter a number between " + ChatColor.GOLD + "0" + ChatColor.GRAY +
						" and " + ChatColor.GOLD + shop.getCost().getMaxStackSize() + ChatColor.GRAY + ".");
				return;
			}

			shop.getCost().setAmount(paymentAmount);
			plugin.getLockedContainerConfigManager().saveShopChests(shop);

			e.getPlayer().sendMessage("");
			e.getPlayer().sendMessage(ChatColor.GRAY + "Payment amount set to " + ChatColor.GOLD + paymentAmount);
			e.getPlayer().sendMessage("");
			e.getPlayer().sendMessage(ChatColor.GOLD + "+++++++++++ SHOP SETUP +++++++++++");
			e.getPlayer().sendMessage(ChatColor.GRAY + "Shop setup complete.");
			e.getPlayer().sendMessage(ChatColor.GOLD + "+++++++++++++++++++++++++++++++++");
			e.getPlayer().sendMessage("");

			plugin.setState(e.getPlayer(), VanillaTradeState.NONE);
		}
	}
}
