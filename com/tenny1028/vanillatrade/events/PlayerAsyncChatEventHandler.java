package com.tenny1028.vanillatrade.events;

import com.tenny1028.vanillatrade.ShopChest;
import com.tenny1028.vanillatrade.ShopState;
import com.tenny1028.vanillatrade.VanillaTrade;
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
		ShopState state = plugin.getState(e.getPlayer());

		if(state.equals(ShopState.NONE)){
			return;
		}

		if(e.getMessage().equalsIgnoreCase("cancel")){
			e.setCancelled(true);

			e.getPlayer().sendMessage("");
			e.getPlayer().sendMessage(ChatColor.GOLD + "+++++++++++ SHOP SETUP +++++++++++");
			e.getPlayer().sendMessage(ChatColor.GRAY + "Cancelled shop setup.");
			e.getPlayer().sendMessage(ChatColor.GOLD + "+++++++++++++++++++++++++++++++++");
			e.getPlayer().sendMessage("");

			plugin.setState(e.getPlayer(), ShopState.NONE);
			return;
		}

		if(state.equals(ShopState.SETUP_CHOOSE_PAYMENT_TYPE)){
			e.setCancelled(true);

			Material paymentType = null;

			try{
				int id = Integer.parseInt(e.getMessage());
				paymentType = Material.getMaterial(id);
			}catch (NumberFormatException ex) {
				paymentType = Material.matchMaterial(e.getMessage().replace(" ","_"));
			}

			if(paymentType == null){
				e.getPlayer().sendMessage("");
				e.getPlayer().sendMessage(ChatColor.GOLD + e.getMessage() + ChatColor.GRAY +" is not a valid item. Please try again.");

				return;
			}

			if(state.getCurrentShop()!=null) {
				ShopChest shop = plugin.getShopConfigManager().getShopChest(state.getCurrentShop());
				if(shop == null) {
					shop = new ShopChest(e.getPlayer(),plugin.getState(e.getPlayer()).getCurrentShop(), new ItemStack(paymentType));
					if(plugin.isDoubleChest(shop.getChest())){
						plugin.getShopConfigManager().saveShopChest(new ShopChest(shop.getOwner(),plugin.getSisterChest(shop.getChest()).getLocation(),shop.getCost()));
					}
				}
				shop.setCost(new ItemStack(paymentType));
				plugin.getShopConfigManager().saveShopChest(shop);

				e.getPlayer().sendMessage("");
				e.getPlayer().sendMessage(ChatColor.GRAY + "Payment type set to " + ChatColor.GOLD + paymentType.name());
				e.getPlayer().sendMessage("");
				e.getPlayer().sendMessage(ChatColor.GOLD + "+++++++++++ SHOP SETUP +++++++++++");
				e.getPlayer().sendMessage(ChatColor.GRAY + "Enter desired payment amount.");
				e.getPlayer().sendMessage(ChatColor.GOLD + "+++++++++++++++++++++++++++++++++");


				state = ShopState.SETUP_CHOOSE_PAYMENT_AMOUNT;
				state.setCurrentShop(shop.getLocation());
				plugin.setState(e.getPlayer(),state);
			}

		}else if(state.equals(ShopState.SETUP_CHOOSE_PAYMENT_AMOUNT)){
			e.setCancelled(true);
			int paymentAmount = 1;

			try{
				paymentAmount = Integer.parseInt(e.getMessage());
			}catch (NumberFormatException ex){
				e.getPlayer().sendMessage("");
				e.getPlayer().sendMessage(ChatColor.GRAY + "Please enter a number.");
				return;
			}

			ShopChest shop = plugin.getShopConfigManager().getShopChest(state.getCurrentShop());

			if(paymentAmount < 0 || paymentAmount > shop.getCost().getMaxStackSize()){
				e.getPlayer().sendMessage("");
				e.getPlayer().sendMessage(ChatColor.GRAY + "Please enter a number between " + ChatColor.GOLD + "0" + ChatColor.GRAY +
						" and " + ChatColor.GOLD + shop.getCost().getMaxStackSize() + ChatColor.GRAY + ".");
				return;
			}

			shop.getCost().setAmount(paymentAmount);
			plugin.getShopConfigManager().saveShopChest(shop);

			e.getPlayer().sendMessage("");
			e.getPlayer().sendMessage(ChatColor.GRAY + "Payment amount set to " + ChatColor.GOLD + paymentAmount);
			e.getPlayer().sendMessage("");
			e.getPlayer().sendMessage(ChatColor.GOLD + "+++++++++++ SHOP SETUP +++++++++++");
			e.getPlayer().sendMessage(ChatColor.GRAY + "Shop setup complete.");
			e.getPlayer().sendMessage(ChatColor.GOLD + "+++++++++++++++++++++++++++++++++");
			e.getPlayer().sendMessage("");

			plugin.setState(e.getPlayer(), ShopState.NONE);
		}
	}
}
