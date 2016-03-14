package com.tenny1028.vanillatrade.events;

import com.tenny1028.vanillatrade.VanillaTrade;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Created by jasper on 3/14/16.
 */
public class PlayerInventoryInteractEventHandler implements Listener {
	VanillaTrade plugin;

	public PlayerInventoryInteractEventHandler(VanillaTrade plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerInventoryInteract(InventoryClickEvent e){

	}

}
