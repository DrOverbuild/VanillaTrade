package com.tenny1028.vanillatrade.util.itemstack;

import org.bukkit.Material;

/**
 * Created by jasper on 8/26/16.
 */
public enum CommonPayments {
	BONE_MEAL(Material.AIR, (short)0);

	CommonPayments(Material type, short data){
		this.type = type;
		this.data = data;
	}

	public Material type;
	public short data;
}
