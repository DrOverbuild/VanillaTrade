package com.tenny1028.vanillatrade.events;

import com.tenny1028.vanillatrade.VanillaTrade;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by jasper on 3/14/16.
 */
public class PlayerInteractEventHandler implements Listener{
	VanillaTrade plugin;

	public PlayerInteractEventHandler(VanillaTrade plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e){

	}
}
