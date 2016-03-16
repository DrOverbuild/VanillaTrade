package com.tenny1028.vanillatrade;

import com.tenny1028.vanillatrade.commands.ShopCommand;
import com.tenny1028.vanillatrade.events.ExtraEventsHandler;
import com.tenny1028.vanillatrade.events.PlayerAsyncChatEventHandler;
import com.tenny1028.vanillatrade.events.PlayerInteractEventHandler;
import com.tenny1028.vanillatrade.events.PlayerInventoryInteractEventHandler;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jasper on 3/14/16.
 */
public class VanillaTrade extends JavaPlugin{

	public static VanillaTrade instance;

	Map<Player, ShopState> playerState = new HashMap<>();
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
		for(Player p : playerState.keySet()){
			if(getState(p).equals(ShopState.BROWSING_SHOP)||getState(p).equals(ShopState.TRADE_CONFIRMATION)){
				p.closeInventory();
			}else if(!getState(p).equals(ShopState.NONE)){

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
	}

	public ShopState getState(Player p){
		if(playerState.containsKey(p)){
			return playerState.get(p);
		}else{
			return ShopState.NONE;
		}
	}

	public void setState(Player p, ShopState state){
		if(state.equals(ShopState.NONE)){
			playerState.remove(p);
		}else {
			playerState.put(p, state);
		}

		getLogger().info("State of " + p.getName() + ": " + state.name());
	}

	public ShopConfigManager getShopConfigManager() {
		return shopConfigManager;
	}

	public Chest getSisterChest(Chest chest){
		BlockFace[] faces = new BlockFace[]{BlockFace.NORTH,BlockFace.EAST,BlockFace.SOUTH,BlockFace.WEST};
		for(BlockFace face:faces){
			Block relative = chest.getBlock().getRelative(face);
			if(relative!=null && relative.getType().equals(Material.CHEST)){
				return (Chest)relative.getState();
			}
		}
		return null;
	}

	public boolean isDoubleChest(Chest chest){
		return getSisterChest(chest) != null;
	}
}
