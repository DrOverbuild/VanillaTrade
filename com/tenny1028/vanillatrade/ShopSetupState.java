package com.tenny1028.vanillatrade;

import org.bukkit.Location;

/**
 * Created by jasper on 3/14/16.
 */
public enum ShopSetupState {

	NONE, CHOOSE_CHEST, CHOOSE_PAYMENT_TYPE, CHOOSE_PAYMENT_AMOUNT, TRADE_CONFIRMATION;

	Location currentShop = null;

	public void setCurrentShop(Location currentShop) {
		this.currentShop = currentShop;
	}

	public Location getCurrentShop() {
		return currentShop;
	}
}
