package com.tenny1028.vanillatrade.commands;

import com.tenny1028.vanillatrade.VanillaTrade;
import com.tenny1028.vanillatrade.VanillaTradeState;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by jasper on 4/6/16.
 */
public class UnlockCommand implements CommandExecutor {
	VanillaTrade plugin;

	public UnlockCommand(VanillaTrade plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
		if(!(commandSender instanceof Player)){
			commandSender.sendMessage("You must be a player.");
			return true;
		}

		Player player = (Player)commandSender;
		plugin.setState(player, VanillaTradeState.LOCK_SETUP_UNLOCK);
		player.sendMessage(ChatColor.GRAY + "Right click a container to unlock.");
		return true;
	}
}
