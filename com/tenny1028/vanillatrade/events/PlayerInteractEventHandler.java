package com.tenny1028.vanillatrade.events;

import com.tenny1028.vanillatrade.VanillaTrade;
import com.tenny1028.vanillatrade.protection.AccessLevel;
import com.tenny1028.vanillatrade.protection.LockedContainer;
import com.tenny1028.vanillatrade.protection.ShopChest;
import com.tenny1028.vanillatrade.VanillaTradeState;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jasper on 3/14/16.
 */
public class PlayerInteractEventHandler implements Listener {
	VanillaTrade plugin;

	public PlayerInteractEventHandler(VanillaTrade plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (e.getClickedBlock() == null) {
			return;
		}

		if (!e.getClickedBlock().getType().equals(Material.CHEST)) {
			LockedContainer container = plugin.getLockedContainerConfigManager().getLockedContainer(e.getClickedBlock().getLocation());
			if (container != null) {
				plugin.getLockedContainerConfigManager().unlockContainer(container);
			}

			return;
		}

		if (!e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}

		LockedContainer container = plugin.getLockedContainerConfigManager().getLockedContainer(e.getClickedBlock().getLocation());
		if (container == null) {
			if (plugin.getState(e.getPlayer()).equals(VanillaTradeState.SHOP_SETUP_CHOOSE_CHEST) || plugin.getState(e.getPlayer()).equals(VanillaTradeState.SHOP_SETUP_CHOOSE_PAYMENT_TYPE)) {
				setupNewShop(e);
			} else if (plugin.getState(e.getPlayer()).equals(VanillaTradeState.LOCK_SETUP_CHOOSE_CHEST)) {
				lockContainer(e);
			} else if (plugin.getState(e.getPlayer()).name().startsWith("LOCK_SETUP_")) {
				e.getPlayer().sendMessage(ChatColor.RED + "This chest is not locked. Type /lock to lock it.");
				plugin.setState(e.getPlayer(), VanillaTradeState.NONE);
			}
		} else {

			if (plugin.getState(e.getPlayer()).equals(VanillaTradeState.LOCK_SETUP_GET_OWNER)) {
				lockSetupGetOwner(e, container);
			} else if (container instanceof ShopChest) {
				ShopChest shop = (ShopChest) container;
				if (AccessLevel.hasPermission(shop.getAccessLevelOfIgnoreOp(e.getPlayer()), AccessLevel.FULL_ACCESS) ||
						(!plugin.getState(e.getPlayer()).equals(VanillaTradeState.NONE) && AccessLevel.hasPermission(shop.getAccessLevelOf(e.getPlayer()), AccessLevel.FULL_ACCESS))) {
					if (plugin.getState(e.getPlayer()).equals(VanillaTradeState.SHOP_SETUP_CHOOSE_CHEST) ||
							plugin.getState(e.getPlayer()).equals(VanillaTradeState.SHOP_SETUP_CHOOSE_PAYMENT_TYPE)) {
						setupExistingShop(e, shop);
					} else if (plugin.getState(e.getPlayer()).equals(VanillaTradeState.LOCK_SETUP_ADD_PLAYER)) {
						lockSetupAddPlayer(e, shop);
					} else if (plugin.getState(e.getPlayer()).equals(VanillaTradeState.LOCK_SETUP_CHMOD_FRIENDS)) {
						lockSetupChmodFriends(e, shop);
					} else if (plugin.getState(e.getPlayer()).equals(VanillaTradeState.LOCK_SETUP_CHMOD_PUBLIC)) {
						lockSetupChmodPublic(e, shop);
					} else if (plugin.getState(e.getPlayer()).equals(VanillaTradeState.LOCK_SETUP_REMOVE_PLAYER)) {
						lockSetupRemovePlayer(e, shop);
					} else if (plugin.getState(e.getPlayer()).equals(VanillaTradeState.LOCK_SETUP_UNLOCK)) {
						lockSetupUnlock(e, shop);
					} else if (plugin.getState(e.getPlayer()).equals(VanillaTradeState.LOCK_SETUP_SET_OWNER)) {
						lockSetupChangeOwner(e, shop);
					}
				} else if (AccessLevel.hasPermission(shop.getAccessLevelOfIgnoreOp(e.getPlayer()), AccessLevel.READ_WRITE)) {
					if (!plugin.getState(e.getPlayer()).equals(VanillaTradeState.NONE)) {
						e.setCancelled(true);
						e.getPlayer().sendMessage(ChatColor.RED + "You do not have permission.");
						plugin.setState(e.getPlayer(), VanillaTradeState.NONE);
					}
				} else {
					e.setCancelled(true);
					if (!plugin.getState(e.getPlayer()).equals(VanillaTradeState.NONE)) {
						e.getPlayer().sendMessage(ChatColor.RED + "You do not have permission to edit this shop.");
						return;
					}

					if (e.getPlayer().isSneaking()) {
						return;
					}

					openInventory(shop, e.getPlayer());
				}
			} else {
				if (AccessLevel.hasPermission(container.getAccessLevelOf(e.getPlayer()), AccessLevel.FULL_ACCESS)) {
					if (plugin.getState(e.getPlayer()).equals(VanillaTradeState.LOCK_SETUP_ADD_PLAYER)) {
						lockSetupAddPlayer(e, container);
					} else if (plugin.getState(e.getPlayer()).equals(VanillaTradeState.LOCK_SETUP_CHMOD_FRIENDS)) {
						lockSetupChmodFriends(e, container);
					} else if (plugin.getState(e.getPlayer()).equals(VanillaTradeState.LOCK_SETUP_CHMOD_PUBLIC)) {
						lockSetupChmodPublic(e, container);
					} else if (plugin.getState(e.getPlayer()).equals(VanillaTradeState.LOCK_SETUP_REMOVE_PLAYER)) {
						lockSetupRemovePlayer(e, container);
					} else if (plugin.getState(e.getPlayer()).equals(VanillaTradeState.LOCK_SETUP_UNLOCK)) {
						lockSetupUnlock(e, container);
					} else if (plugin.getState(e.getPlayer()).equals(VanillaTradeState.LOCK_SETUP_CHOOSE_CHEST)) {
						e.setCancelled(true);
						e.getPlayer().sendMessage(ChatColor.RED + "This chest is already locked.");
						plugin.setState(e.getPlayer(), VanillaTradeState.NONE);
					} else if (plugin.getState(e.getPlayer()).equals(VanillaTradeState.LOCK_SETUP_SET_OWNER)) {
						lockSetupChangeOwner(e, container);
					}
				} else if (AccessLevel.hasPermission(container.getAccessLevelOf(e.getPlayer()), AccessLevel.READ_ONLY)
						&& !container.getAccessLevelOf(e.getPlayer()).equals(AccessLevel.WRITE_ONLY)) {

				} else {
					e.setCancelled(true);

					if (!plugin.getState(e.getPlayer()).equals(VanillaTradeState.NONE)) {
						e.getPlayer().sendMessage(ChatColor.RED + "You do not own this chest.");
						plugin.setState(e.getPlayer(), VanillaTradeState.NONE);
						return;
					}

					e.getPlayer().sendMessage(ChatColor.RED + "You do not have permission to open " + container.getOwner().getName() + "'s chest.");
				}
			}
		}
	}

	private void openInventory(ShopChest shop, Player p) {
		Inventory chestInv = shop.getChest().getInventory();
		Inventory inv = plugin.getServer().createInventory(p, chestInv.getSize(), ChatColor.BOLD + "Trade with " + shop.getOwner().getName());

		for (int i = 0; i < chestInv.getContents().length; i++) {
			ItemStack item = chestInv.getContents()[i];
			if (item != null) {
				if (!item.getType().equals(shop.getCost().getType())) {
					ItemStack copy = new ItemStack(item);
					String loreLine = ChatColor.GOLD + "Cost: " + shop.getCost().getAmount() + " " +
							shop.getCost().getType().name().replace("_", " ").toLowerCase() + ((shop.getCost().getAmount() == 1) ? "" : "s");
					ItemMeta copyMeta = copy.getItemMeta();
					List<String> lore;
					if (copyMeta.hasLore()) {
						lore = copyMeta.getLore();
					} else {
						lore = new ArrayList<>();
					}
					lore.add(loreLine);
					copyMeta.setLore(lore);
					copy.setItemMeta(copyMeta);
					inv.setItem(i, copy);
				}
			}
		}

		p.openInventory(inv);
		VanillaTradeState state = VanillaTradeState.BROWSING_SHOP;
		state.setCurrentBlock(shop.getLocation());
		plugin.setState(p, state);
	}

	private void setupNewShop(PlayerInteractEvent e) {
		e.setCancelled(true);
		e.getPlayer().sendMessage(ChatColor.GOLD + "+++++++++++ SHOP SETUP +++++++++++");
		e.getPlayer().sendMessage(ChatColor.GRAY + "Setting up a new Shop chest.");
		e.getPlayer().sendMessage(ChatColor.GRAY + "Enter desired payment type or choose another chest.");
		e.getPlayer().sendMessage(ChatColor.GRAY + "Examples: " + ChatColor.GOLD + "diamond" + ChatColor.GRAY + " or " + ChatColor.GOLD + "gold_nugget");
		e.getPlayer().sendMessage(ChatColor.GOLD + "+++++++++++++++++++++++++++++++++");
		VanillaTradeState state = VanillaTradeState.SHOP_SETUP_CHOOSE_PAYMENT_TYPE;
		state.setCurrentBlock(e.getClickedBlock().getLocation());
		plugin.setState(e.getPlayer(), state);
	}

	private void setupExistingShop(PlayerInteractEvent e, ShopChest shop) {
		e.setCancelled(true);
		e.getPlayer().sendMessage(ChatColor.GOLD + "++++++++++++ SHOP SETUP ++++++++++++");
		e.getPlayer().sendMessage(ChatColor.GRAY + "Editing an existing Shop chest.");
		e.getPlayer().sendMessage(ChatColor.GRAY + "Cost of items in chest: " + ChatColor.GOLD + shop.getCost().getAmount() + " " +
				shop.getCost().getType().name().toLowerCase().replace("_", " ") + ((shop.getCost().getAmount() > 1 || shop.getCost().getAmount() == 0) ? "s" : ""));
		e.getPlayer().sendMessage(ChatColor.GRAY + "Enter desired payment type or choose another chest.");
		e.getPlayer().sendMessage(ChatColor.GRAY + "Examples: " + ChatColor.GOLD + "diamond" + ChatColor.GRAY + " or " + ChatColor.GOLD + "gold_nugget");
		e.getPlayer().sendMessage(ChatColor.GOLD + "+++++++++++++++++++++++++++++++++++");
		VanillaTradeState state = VanillaTradeState.SHOP_SETUP_CHOOSE_PAYMENT_TYPE;
		state.setCurrentBlock(e.getClickedBlock().getLocation());
		plugin.setState(e.getPlayer(), state);
	}

	private void lockContainer(PlayerInteractEvent e) {
		e.setCancelled(true);
		LockedContainer container = new LockedContainer(e.getPlayer(), e.getClickedBlock().getLocation());
		plugin.getLockedContainerConfigManager().saveContainers(container);
		e.getPlayer().sendMessage(ChatColor.GOLD + "Chest locked! " + ChatColor.GRAY + "To give permission to" +
				" players to use this chest, type " + ChatColor.GOLD + "/lock add <player>" + ChatColor.GRAY + ".");
		plugin.setState(e.getPlayer(), VanillaTradeState.NONE);
	}

	private void lockSetupAddPlayer(PlayerInteractEvent e, LockedContainer container) {
		e.setCancelled(true);
		VanillaTradeState state = plugin.getState(e.getPlayer());
		if (!container.playerIsFriend(state.getPlayer())) {
			container.addFriend(state.getPlayer());
			plugin.getLockedContainerConfigManager().saveContainers(container);
		}
		e.getPlayer().sendMessage(ChatColor.GRAY + "Player " + ChatColor.GOLD + state.getPlayer().getName() + ChatColor.GRAY + " has been added to this chest.");
		plugin.setState(e.getPlayer(), VanillaTradeState.NONE);
	}

	private void lockSetupChangeOwner(PlayerInteractEvent e, LockedContainer container) {
		e.setCancelled(true);
		VanillaTradeState state = plugin.getState(e.getPlayer());
		container.setOwner(state.getPlayer());
		plugin.getLockedContainerConfigManager().saveContainers(container);
		e.getPlayer().sendMessage(ChatColor.GRAY + "Player " + ChatColor.GOLD + state.getPlayer().getName() + ChatColor.GRAY + " is now the owner of this chest.");
		plugin.setState(e.getPlayer(), VanillaTradeState.NONE);
	}

	private void lockSetupGetOwner(PlayerInteractEvent e, LockedContainer container){
		e.setCancelled(true);
		e.getPlayer().sendMessage(ChatColor.GRAY + "The owner of this chest is " + ChatColor.GOLD + container.getOwner().getName());
		plugin.setState(e.getPlayer(), VanillaTradeState.NONE);
	}

	private void lockSetupChmodPublic(PlayerInteractEvent e, LockedContainer container) {
		e.setCancelled(true);
		VanillaTradeState state = plugin.getState(e.getPlayer());
		container.setPublicAccessLevel(state.getPermission());
		plugin.getLockedContainerConfigManager().saveContainers(container);
		e.getPlayer().sendMessage(ChatColor.GRAY + "Public access level for this chest has been set to " + ChatColor.GOLD + state.getPermission().name() + ChatColor.GRAY + ".");
		plugin.setState(e.getPlayer(), VanillaTradeState.NONE);
	}

	private void lockSetupChmodFriends(PlayerInteractEvent e, LockedContainer container) {
		e.setCancelled(true);
		VanillaTradeState state = plugin.getState(e.getPlayer());
		container.setFriendsAccessLevel(state.getPermission());
		plugin.getLockedContainerConfigManager().saveContainers(container);
		e.getPlayer().sendMessage(ChatColor.GRAY + "Friend access level for this chest has been set to " + ChatColor.GOLD + state.getPermission().name() + ChatColor.GRAY + ".");
		plugin.setState(e.getPlayer(), VanillaTradeState.NONE);
	}

	private void lockSetupRemovePlayer(PlayerInteractEvent e, LockedContainer container) {
		e.setCancelled(true);
		VanillaTradeState state = plugin.getState(e.getPlayer());
		if (container.playerIsFriend(state.getPlayer())) {
			container.removeFriend(state.getPlayer());
			plugin.getLockedContainerConfigManager().saveContainers(container);
			e.getPlayer().sendMessage(ChatColor.GRAY + "Player " + state.getPlayer().getName() + " has been removed to this chest.");
		} else {
			e.getPlayer().sendMessage(ChatColor.RED + "Player " + state.getPlayer().getName() + " has not been added to this chest");
		}
		plugin.setState(e.getPlayer(), VanillaTradeState.NONE);
	}

	private void lockSetupUnlock(PlayerInteractEvent e, LockedContainer container) {
		e.setCancelled(true);
		plugin.getLockedContainerConfigManager().unlockContainer(container);
		if (plugin.isDoubleChest(container.getChest())) {
			plugin.getLockedContainerConfigManager().unlockContainer(plugin.getLockedContainerConfigManager().getLockedContainer(
					plugin.getSisterChest(container.getChest()).getLocation()));
		}
		e.getPlayer().sendMessage(ChatColor.GRAY + "This chest has been unlocked.");
		plugin.setState(e.getPlayer(), VanillaTradeState.NONE);
	}
}
