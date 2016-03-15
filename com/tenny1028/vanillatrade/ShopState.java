package com.tenny1028.vanillatrade;

import org.bukkit.Location;

/**
 * Created by jasper on 3/14/16.
 */
public enum ShopState {

	NONE, SETUP_CHOOSE_CHEST, SETUP_CHOOSE_PAYMENT_TYPE, SETUP_CHOOSE_PAYMENT_AMOUNT, BROWSING_SHOP, TRADE_CONFIRMATION;

	Location currentShop = null;

	public void setCurrentShop(Location currentShop) {
		this.currentShop = currentShop;
	}

	public Location getCurrentShop() {
		return currentShop;
	}
}
