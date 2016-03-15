package com.tenny1028.vanillatrade;

import com.tenny1028.vanillatrade.commands.ShopCommand;
import com.tenny1028.vanillatrade.events.ExtraEventsHandler;
import com.tenny1028.vanillatrade.events.PlayerAsyncChatEventHandler;
import com.tenny1028.vanillatrade.events.PlayerInteractEventHandler;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jasper on 3/14/16.
 */
public class VanillaTrade extends JavaPlugin{

	public static VanillaTrade instance;

	Map<Player, ShopSetupState> playerShopSetupState = new HashMap<>();
	ShopConfigManager shopConfigManager;

	@Override
	public void onEnable() {
		instance = this;

		shopConfigManager = new ShopConfigManager(this);

		loadEvents();
		loadCommands();


	}

	@Override
	public void onDisable() {

	}

	public void loadEvents(){
		getServer().getPluginManager().registerEvents(new PlayerAsyncChatEventHandler(this),this);
		getServer().getPluginManager().registerEvents(new PlayerInteractEventHandler(this),this);
		getServer().getPluginManager().registerEvents(new ExtraEventsHandler(this),this);
	}

	public void loadCommands(){
		getCommand("shop").setExecutor(new ShopCommand(this));
	}

	public ShopSetupState getSetupState(Player p){
		if(playerShopSetupState.containsKey(p)){
			return playerShopSetupState.get(p);
		}else{
			return ShopSetupState.NONE;
		}
	}

	public void setShopSetupState(Player p,ShopSetupState state){
		if(state.equals(ShopSetupState.NONE)){
			playerShopSetupState.remove(p);
		}else {
			playerShopSetupState.put(p, state);
		}
	}

	public ShopConfigManager getShopConfigManager() {
		return shopConfigManager;
	}
}
