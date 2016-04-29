package com.tenny1028.vanillatrade;

import com.tenny1028.vanillatrade.protection.AccessLevel;
import com.tenny1028.vanillatrade.protection.LockedContainer;
import com.tenny1028.vanillatrade.protection.ShopChest;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * Created by jasper on 3/14/16.
 */
public class LockedContainerConfigManager {
	VanillaTrade plugin;

	File shopsDir;

	public LockedContainerConfigManager(VanillaTrade plugin) {
		this.plugin = plugin;

		shopsDir = new File(plugin.getDataFolder(),"protected-containers");
		shopsDir.mkdirs();
	}

	public String getConfigFileName(Location location){
		return location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ() + ".yml";
	}

	public YamlConfiguration getConfigOfShop(Location location) throws IOException, InvalidConfigurationException {
		YamlConfiguration config = new YamlConfiguration();
		config.load(new File(shopsDir, getConfigFileName(location)));
		return config;
	}

	public LockedContainer getLockedContainer(Location location){
		FileConfiguration config;
		try{
			config = getConfigOfShop(location);
		}catch (IOException|InvalidConfigurationException e){
			return null;
		}
		String ownerUUID = config.getString("owner", "");
		String publicAccessLevel = config.getString("access-level.public",AccessLevel.NO_ACCESS.name());
		String friendsAccessLevel = config.getString("access-level.friends",AccessLevel.READ_WRITE.name());
		List<String> friends = config.getStringList("friends");
		ItemStack payment = ItemStackManager.getPayment(config);

		try {
			OfflinePlayer owner = plugin.getServer().getOfflinePlayer(UUID.fromString(ownerUUID));

			if(payment != null){
				return new ShopChest(owner,location,AccessLevel.valueOf(friendsAccessLevel),friends,payment);
			}

			return new LockedContainer(owner,location,config,AccessLevel.valueOf(publicAccessLevel),AccessLevel.valueOf(friendsAccessLevel),friends);
		}catch(Exception e){
			return null;
		}
	}

	public void saveContainer(LockedContainer container){
		if(container instanceof ShopChest){
			saveShopChest((ShopChest)container);
			return;
		}

		FileConfiguration config;
		try{
			config = getConfigOfShop(container.getLocation());
		}catch (IOException e){
			config = new YamlConfiguration();
		}catch (InvalidConfigurationException e){
			return;
		}

		config.set("owner",container.getOwner().getUniqueId().toString());
		config.set("access-level.public", container.getPublicAccessLevel().name());
		config.set("access-level.friends", container.getFriendsAccessLevel().name());
		config.set("friends",container.getFriends());

		try {
			config.save(new File(shopsDir,getConfigFileName(container.getLocation())));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveContainers(LockedContainer container){
		saveContainer(container);
		if(plugin.isDoubleChest(container.getChest())){
			saveContainer(new LockedContainer(container.getOwner(),plugin.getSisterChest(container.getChest()).getLocation(),
					container.getPublicAccessLevel(),container.getFriendsAccessLevel(),container.getFriends()));
		}
	}

	public void unlockContainer(LockedContainer container){
		new File(shopsDir, getConfigFileName(container.getLocation())).delete();
	}

	public ShopChest getShopChest(Location location){
		LockedContainer container = getLockedContainer(location);
		if(container instanceof ShopChest){
			return (ShopChest) container;
		}
		return null;
	}

	public void saveShopChest(ShopChest shopChest){
		FileConfiguration config;
		try{
			config = getConfigOfShop(shopChest.getLocation());
		}catch (IOException e){
			config = new YamlConfiguration();
		}catch (InvalidConfigurationException e){
			return;
		}

		config.set("payment.type",shopChest.getCost().getType().name());
		config.set("payment.amount",shopChest.getCost().getAmount());
		config.set("payment.data",shopChest.getCost().getDurability());
		config.set("owner",shopChest.getOwner().getUniqueId().toString());
		config.set("is-shop",true);
		config.set("public-permission", AccessLevel.NO_ACCESS.toString());
		config.set("friends-permission", shopChest.getFriendsAccessLevel().toString());
		config.set("friends",shopChest.getFriends());
		try {
			config.save(new File(shopsDir,getConfigFileName(shopChest.getLocation())));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveShopChests(ShopChest shopChest){
		saveShopChest(shopChest);
		if(plugin.isDoubleChest(shopChest.getChest())){
			saveShopChest(new ShopChest(shopChest.getOwner(),plugin.getSisterChest(shopChest.getChest()).getLocation(),shopChest.getCost()));
		}
	}

	public void removeShopChest(ShopChest shopChest){
		new File(shopsDir, getConfigFileName(shopChest.getLocation())).delete();
	}
}
