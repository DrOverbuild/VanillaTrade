package com.tenny1028.vanillatrade;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

/**
 * Created by jasper on 3/14/16.
 */
public class ShopChest {
	OfflinePlayer owner;
	Location location;
	ItemStack cost;

	@Deprecated
	BlockFace signLocation = null;

	@Deprecated
	public ShopChest(OfflinePlayer owner, Location location, ItemStack cost, BlockFace signLocation) {
		if(owner==null||location==null||cost==null){
			throw new IllegalArgumentException();
		}
		this.owner = owner;
		this.location = location;
		this.cost = cost;
		this.signLocation = signLocation;
	}

	public ShopChest(OfflinePlayer owner, Location location, ItemStack cost) {
		if(owner==null||location==null||cost==null){
			throw new IllegalArgumentException();
		}
		this.owner = owner;
		this.location = location;
		this.cost = cost;
	}

	public OfflinePlayer getOwner() {
		return owner;
	}

	public Location getLocation() {
		return location;
	}

	public ItemStack getCost() {
		return cost;
	}

	@Deprecated
	public BlockFace getSignLocation() {
		return signLocation;
	}

	public void setOwner(OfflinePlayer owner) {
		this.owner = owner;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setCost(ItemStack cost) {
		this.cost = cost;
	}

	@Deprecated
	public void setSignLocation(BlockFace signLocation) {
		this.signLocation = signLocation;
	}
}
