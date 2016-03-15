package com.tenny1028.vanillatrade;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by jasper on 3/14/16.
 */
public class ShopConfigManager {
	VanillaTrade plugin;

	File shopsDir;

	public ShopConfigManager(VanillaTrade plugin) {
		this.plugin = plugin;

		shopsDir = new File(plugin.getDataFolder(),"shops");
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

	public ShopChest getShopChest(Location location){
		FileConfiguration config;
		try{
			config = getConfigOfShop(location);
		}catch (IOException|InvalidConfigurationException e){
			return null;
		}

		String paymentType = config.getString("payment.type", "GOLD_INGOT");
		int paymentAmount = config.getInt("payment.amount",1);
		String ownerUUID = config.getString("owner", "");

		try {
			ItemStack payment = new ItemStack(Material.matchMaterial(paymentType), paymentAmount);
			OfflinePlayer owner = plugin.getServer().getOfflinePlayer(UUID.fromString(ownerUUID));
			return new ShopChest(owner,location,payment);
		}catch(Exception e){
			return null;
		}
	}

	public void saveShopChest(ShopChest shopChest){
		FileConfiguration config;
		try{
			config = getConfigOfShop(shopChest.getLocation());
		}catch (IOException e){
//			try {
//				new File(getConfigFileName(shopChest.getLocation())).createNewFile();
				config = new YamlConfiguration();
//			} catch (IOException e1) {
//				e1.printStackTrace();
//				return;
//			}
		}catch (InvalidConfigurationException e){
			return;
		}

		config.set("payment.type",shopChest.getCost().getType().name());
		config.set("payment.amount",shopChest.getCost().getAmount());
		config.set("owner",shopChest.getOwner().getUniqueId().toString());

		try {
			config.save(new File(shopsDir,getConfigFileName(shopChest.getLocation())));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeShopChest(ShopChest shopChest){
		new File(shopsDir, getConfigFileName(shopChest.getLocation())).delete();
	}
}
