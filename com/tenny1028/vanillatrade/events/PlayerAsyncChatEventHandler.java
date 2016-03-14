package com.tenny1028.vanillatrade.events;

import com.tenny1028.vanillatrade.VanillaTrade;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by jasper on 3/14/16.
 */
public class PlayerAsyncChatEventHandler implements Listener {
	VanillaTrade plugin;

	public PlayerAsyncChatEventHandler(VanillaTrade plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e){

	}
}
