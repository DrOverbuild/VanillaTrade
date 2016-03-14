package com.tenny1028.vanillatrade.commands;

import com.tenny1028.vanillatrade.ShopSetupState;
import com.tenny1028.vanillatrade.VanillaTrade;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by jasper on 3/14/16.
 */
public class ShopCommand implements CommandExecutor {
	VanillaTrade plugin;

	public ShopCommand(VanillaTrade plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

		if(sender instanceof Player){
			sender.sendMessage(ChatColor.GOLD + "Right click a chest to begin shop setup.");
			plugin.setShopSetupState((Player)sender, ShopSetupState.CHOOSE_CHEST);
		}

		return true;
	}
}
