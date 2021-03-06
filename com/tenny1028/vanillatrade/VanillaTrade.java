package com.tenny1028.vanillatrade;

import com.tenny1028.vanillatrade.commands.ShopCommand;
import com.tenny1028.vanillatrade.commands.UnlockCommand;
import com.tenny1028.vanillatrade.commands.LockCommand;
import com.tenny1028.vanillatrade.events.ExtraEventsHandler;
import com.tenny1028.vanillatrade.events.PlayerAsyncChatEventHandler;
import com.tenny1028.vanillatrade.events.PlayerInteractEventHandler;
import com.tenny1028.vanillatrade.events.PlayerInventoryInteractEventHandler;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jasper on 3/14/16.
 */
public class VanillaTrade extends JavaPlugin{

	public static VanillaTrade instance;

	Map<Player, VanillaTradeState> playerState = new HashMap<>();
	ConfigManager shopConfigManager;

	@Override
	public void onEnable() {
		instance = this;

		shopConfigManager = new ConfigManager(this);

		loadEvents();
		loadCommands();


	}

	@Override
	public void onDisable() {
		for(Player p : playerState.keySet()){
			if(getState(p).equals(VanillaTradeState.BROWSING_SHOP)||getState(p).equals(VanillaTradeState.TRADE_CONFIRMATION)){
				p.closeInventory();
			}else if(!getState(p).equals(VanillaTradeState.NONE)){

			}
		}
	}

	public void loadEvents(){
		getServer().getPluginManager().registerEvents(new PlayerAsyncChatEventHandler(this),this);
		getServer().getPluginManager().registerEvents(new PlayerInteractEventHandler(this),this);
		getServer().getPluginManager().registerEvents(new PlayerInventoryInteractEventHandler(this),this);
		getServer().getPluginManager().registerEvents(new ExtraEventsHandler(this),this);
	}

	public void loadCommands(){
		getCommand("shop").setExecutor(new ShopCommand(this));
		LockCommand lockCommand = new LockCommand(this);
		getCommand("lock").setExecutor(lockCommand);
		getCommand("lock").setTabCompleter(lockCommand);
		getCommand("unlock").setExecutor(new UnlockCommand(this));
	}

	public VanillaTradeState getState(Player p){
		return playerState.getOrDefault(p, VanillaTradeState.NONE);
	}

	public void setState(Player p, VanillaTradeState state){
		if(state.equals(VanillaTradeState.NONE)){
			playerState.remove(p);
		}else {
			playerState.put(p, state);
		}

		getLogger().info("State of " + p.getName() + ": " + state.name());
	}

	public ConfigManager getLockedContainerConfigManager() {
		return shopConfigManager;
	}

	public Chest getSisterChest(Chest thisChest){
		InventoryHolder holder = thisChest.getInventory().getHolder();
		if (holder instanceof DoubleChest) {
			DoubleChest bigBoi = (DoubleChest) holder;
			Chest leftChest = (Chest) bigBoi.getLeftSide();
			Chest rightChest = (Chest) bigBoi.getRightSide();

			if (thisChest.equals(leftChest)) {
				return rightChest;
			} else if (thisChest.equals(rightChest)) {
				return leftChest;
			}

		}

		return null;
	}

	public boolean isDoubleChest(Container container){
		if (!(container instanceof Chest)) {
			return false;
		}

		InventoryHolder holder = container.getInventory().getHolder();
		return holder instanceof DoubleChest;
	}
}
