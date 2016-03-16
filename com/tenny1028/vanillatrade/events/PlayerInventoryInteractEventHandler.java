package com.tenny1028.vanillatrade.events;

import com.tenny1028.vanillatrade.ShopChest;
import com.tenny1028.vanillatrade.ShopState;
import com.tenny1028.vanillatrade.VanillaTrade;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Created by jasper on 3/14/16.
 */
public class PlayerInventoryInteractEventHandler implements Listener {
	public final static String confirmationInvName = ChatColor.BOLD + "Are you sure?";

	VanillaTrade plugin;

	public PlayerInventoryInteractEventHandler(VanillaTrade plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerInventoryInteract(InventoryClickEvent e){

		if(!(e.getWhoClicked() instanceof Player)){
			return;
		}

		Player player = (Player)e.getWhoClicked();

		if(!e.getInventory().getName().startsWith(ChatColor.BOLD + "Trade with")&&!e.getInventory().getName().equals(confirmationInvName)){
			return;
		}

		e.setCancelled(true);

		if(!e.getClickedInventory().getName().startsWith(ChatColor.BOLD + "Trade with")&&!e.getClickedInventory().getName().equals(confirmationInvName)){
			return;
		}

		if(e.getCurrentItem() == null){
			return;
		}

		if(plugin.getState(player).equals(ShopState.BROWSING_SHOP)){
			ShopChest currentShop = plugin.getShopConfigManager().getShopChest(plugin.getState(player).getCurrentShop());
			int numberOfItemsInCustomersInventory = 0;
			for(ItemStack i : player.getInventory().getContents()){
				if(i != null){
					if(i.getType().equals(currentShop.getCost().getType())){
						numberOfItemsInCustomersInventory += i.getAmount();
					}
				}
			}

			if(numberOfItemsInCustomersInventory < currentShop.getCost().getAmount()){
				player.sendMessage(ChatColor.RED + "You do not have enough " + currentShop.getCost().getType().name().toLowerCase() +
						"s. You have " + numberOfItemsInCustomersInventory + ", you need " + currentShop.getCost().getAmount() + ".");
				player.closeInventory();
				return;
			}

			ItemStack clickedItem = e.getCurrentItem();

			if(clickedItem == null){
				return;
			}

			if(clickedItem.getType().equals(Material.AIR)){
				return;
			}

			Inventory inv = plugin.getServer().createInventory(player,27,confirmationInvName);

			ItemStack greenWool = new ItemStack(Material.WOOL,1,DyeColor.LIME.getData());
			ItemMeta greenWoolMeta = greenWool.getItemMeta();
			greenWoolMeta.setDisplayName(ChatColor.GREEN + "YES");
			greenWool.setItemMeta(greenWoolMeta);
			inv.setItem(0,greenWool);
			inv.setItem(1,greenWool);
			inv.setItem(2,greenWool);
			inv.setItem(9,greenWool);
			inv.setItem(10,greenWool);
			inv.setItem(11,greenWool);
			inv.setItem(18,greenWool);
			inv.setItem(19,greenWool);
			inv.setItem(20,greenWool);

			ItemStack redWool = new ItemStack(Material.WOOL,1,DyeColor.RED.getData());
			ItemMeta redWoolMeta = redWool.getItemMeta();
			redWoolMeta.setDisplayName(ChatColor.RED + "NO");
			redWool.setItemMeta(redWoolMeta);
			inv.setItem(6,redWool);
			inv.setItem(7,redWool);
			inv.setItem(8,redWool);
			inv.setItem(15,redWool);
			inv.setItem(16,redWool);
			inv.setItem(17,redWool);
			inv.setItem(24,redWool);
			inv.setItem(25,redWool);
			inv.setItem(26,redWool);

			inv.setItem(13, clickedItem);

			player.openInventory(inv);

			ShopState state = ShopState.TRADE_CONFIRMATION;
			state.setCurrentShop(currentShop.getLocation());
			plugin.setState(player,state);
		}else{
			ShopChest currentShop = plugin.getShopConfigManager().getShopChest(plugin.getState(player).getCurrentShop());
			ItemStack item = e.getCurrentItem();
			if(item.hasItemMeta()&&item.getItemMeta().hasDisplayName()){
				if(item.getItemMeta().getDisplayName().toLowerCase().contains("yes")){
					ItemStack itemBeingPurchased = e.getClickedInventory().getItem(13);
					if(itemBeingPurchased != null) {
						player.getInventory().removeItem(currentShop.getCost());
						ItemMeta currentItemMeta = itemBeingPurchased.getItemMeta();
						if(currentItemMeta.hasLore()&&currentItemMeta.getLore().size() > 1){
							List<String> lore = currentItemMeta.getLore();
							lore.remove(lore.size()-1);
							currentItemMeta.setLore(lore);
						}else{
							currentItemMeta.setLore(null);
						}
						itemBeingPurchased.setItemMeta(currentItemMeta);
						player.getInventory().addItem(itemBeingPurchased);
						currentShop.getChest().getInventory().removeItem(itemBeingPurchased);
						currentShop.getChest().getInventory().addItem(currentShop.getCost());
					}
				}

				player.closeInventory();
				plugin.setState(player,ShopState.NONE);
			}
		}
	}

	@EventHandler
	public void onPlayerInventoryClose(InventoryCloseEvent e){
		if(e.getPlayer() instanceof Player) {
			if(plugin.getState((Player)e.getPlayer()).equals(ShopState.BROWSING_SHOP)||plugin.getState((Player)e.getPlayer()).equals(ShopState.TRADE_CONFIRMATION)) {
				plugin.setState((Player)e.getPlayer(),ShopState.NONE);
			}
		}
	}

}
