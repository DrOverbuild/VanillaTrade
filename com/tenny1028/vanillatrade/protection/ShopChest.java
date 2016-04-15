package com.tenny1028.vanillatrade.protection;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by jasper on 3/14/16.
 */
public class ShopChest extends LockedContainer {
	ItemStack cost;

	public ShopChest(OfflinePlayer owner, Location location, ItemStack cost) {
		super(owner,location);
		if(cost==null){
			throw new IllegalArgumentException();
		}
		this.cost = cost;
	}

	public ShopChest(OfflinePlayer owner, Location location, AccessLevel friendsAccessLevel, List<String> friends, ItemStack cost) {
		super(owner, location, AccessLevel.NO_ACCESS, friendsAccessLevel, friends);
		this.cost = cost;
	}

	public void setCost(ItemStack cost) {
		this.cost = cost;
	}

	public ItemStack getCost() {
		return cost;
	}
}
