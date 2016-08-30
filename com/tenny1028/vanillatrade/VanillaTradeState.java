package com.tenny1028.vanillatrade;

import com.tenny1028.vanillatrade.protection.AccessLevel;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

/**
 * Created by jasper on 3/14/16.
 */
public enum VanillaTradeState {

	NONE, SHOP_SETUP_CHOOSE_CHEST, SHOP_SETUP_CHOOSE_PAYMENT_TYPE, SHOP_SETUP_CHOOSE_PAYMENT_AMOUNT, BROWSING_SHOP,
	TRADE_CONFIRMATION, LOCK_SETUP_CHOOSE_CHEST, LOCK_SETUP_ADD_PLAYER, LOCK_SETUP_REMOVE_PLAYER,
	LOCK_SETUP_CHMOD_FRIENDS, LOCK_SETUP_CHMOD_PUBLIC, LOCK_SETUP_UNLOCK, LOCK_SETUP_SET_OWNER, LOCK_SETUP_GET_OWNER;

	Location currentBlock = null;

	int itemSlot = -1;

	AccessLevel permission = null;

	OfflinePlayer player = null;

	public int getItemSlot() {
		return itemSlot;
	}

	public Location getCurrentBlock() {
		return currentBlock;
	}

	public AccessLevel getPermission() {
		return permission;
	}

	public OfflinePlayer getPlayer() {
		return player;
	}

	public void setItemSlot(int itemSlot) {
		this.itemSlot = itemSlot;
	}

	public void setCurrentBlock(Location currentBlock) {
		this.currentBlock = currentBlock;
	}

	public void setPermission(AccessLevel permission) {
		this.permission = permission;
	}

	public void setPlayer(OfflinePlayer player) {
		this.player = player;
	}
}
