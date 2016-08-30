package com.tenny1028.vanillatrade.commands;

import com.tenny1028.vanillatrade.VanillaTrade;
import com.tenny1028.vanillatrade.VanillaTradeState;
import com.tenny1028.vanillatrade.protection.AccessLevel;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jasper on 4/5/16.
 */
public class LockCommand implements CommandExecutor, TabCompleter {
	VanillaTrade plugin;

	public LockCommand(VanillaTrade plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage("You must be a player!");
			return true;
		}

		Player player = (Player)sender;

		if(args.length == 0){
			plugin.setState(player, VanillaTradeState.LOCK_SETUP_CHOOSE_CHEST);
			player.sendMessage(ChatColor.GRAY + "Right click a chest to lock.");
			return true;
		}

		if(args.length == 1){
			if(args[0].equalsIgnoreCase("add")){
				player.sendMessage(ChatColor.RED + "Usage: /lock add <player>");
				return true;
			}

			if(args[0].equalsIgnoreCase("remove")){
				player.sendMessage(ChatColor.RED + "Usage: /lock remove <player>");
				return true;
			}

			if(args[0].equalsIgnoreCase("owner")){
				player.sendMessage(ChatColor.GRAY + "Right click chest to get owner");
				plugin.setState(player, VanillaTradeState.LOCK_SETUP_GET_OWNER);
				return true;
			}

			if(args[0].equalsIgnoreCase("chmod")){
				player.sendMessage(ChatColor.RED + "Usage: /lock chmod <friends|public> <NO_ACCESS|WRITE_ONLY|READ_ONLY|READ_WRITE|FULL_ACCESS>");
				return true;
			}

			player.sendMessage(ChatColor.RED + "Usage: /lock <add|remove|chmod|owner|>");
			return true;
		}

		if(args[0].equalsIgnoreCase("add")){
			OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(args[1]);

			if(offlinePlayer == null){
				player.sendMessage(ChatColor.RED + "This user doesn't exist");
				return true;
			}

			player.sendMessage(ChatColor.GRAY + "Right click chest to add player");
			VanillaTradeState state = VanillaTradeState.LOCK_SETUP_ADD_PLAYER;
			state.setPlayer(offlinePlayer);
			plugin.setState(player,state);
		}else if(args[0].equalsIgnoreCase("remove")){
			OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(args[1]);

			if(offlinePlayer == null){
				player.sendMessage(ChatColor.RED + "This user doesn't exist");
				return true;
			}

			player.sendMessage(ChatColor.GRAY + "Right click chest to remove player");
			VanillaTradeState state = VanillaTradeState.LOCK_SETUP_REMOVE_PLAYER;
			state.setPlayer(offlinePlayer);
			plugin.setState(player,state);
		}else if(args[0].equalsIgnoreCase("owner")){
			OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(args[1]);

			if(offlinePlayer == null){
				player.sendMessage(ChatColor.RED + "This user doesn't exist");
				return true;
			}

			player.sendMessage(ChatColor.GRAY + "Right click chest to change owner");
			VanillaTradeState state = VanillaTradeState.LOCK_SETUP_SET_OWNER;
			state.setPlayer(offlinePlayer);
			plugin.setState(player,state);
		}else if(args[0].equalsIgnoreCase("chmod")){
			if(!(args[1].equalsIgnoreCase("friends")||args[1].equalsIgnoreCase("public"))){
				player.sendMessage(ChatColor.RED + "Usage: /lock chmod <friends|public> <permission>");
				return true;
			}

			if(args.length == 3){
				if(args[1].equalsIgnoreCase("friends")){
					try{
						AccessLevel.valueOf(args[2].toUpperCase());
					}catch (IllegalArgumentException e){
						player.sendMessage("Usage: /lock chmod " + args[1] + " <NO_ACCESS|WRITE_ONLY|READ_ONLY|READ_WRITE|FULL_ACCESS>");
						return true;
					}
					VanillaTradeState state = VanillaTradeState.LOCK_SETUP_CHMOD_FRIENDS;
					state.setPermission(AccessLevel.valueOf(args[2]));
					plugin.setState(player,state);
					player.sendMessage(ChatColor.GRAY + "Right click chest to change permissions of the chest.");
				}else{
					try{
						AccessLevel.valueOf(args[2].toUpperCase());
					}catch (IllegalArgumentException e){
						player.sendMessage("Usage: /lock chmod " + args[1] + " <NO_ACCESS|WRITE_ONLY|READ_ONLY|READ_WRITE|FULL_ACCESS>");
						return true;
					}
					VanillaTradeState state = VanillaTradeState.LOCK_SETUP_CHMOD_PUBLIC;
					state.setPermission(AccessLevel.valueOf(args[2]));
					plugin.setState(player,state);
					player.sendMessage(ChatColor.GRAY + "Right click chest to change permissions of the chest");
				}
			}else{
				player.sendMessage("Usage: /lock chmod " + args[1] + " <NO_ACCESS|WRITE_ONLY|READ_ONLY|READ_WRITE|FULL_ACCESS>");
			}
		}else{
			player.sendMessage(ChatColor.RED + "Usage: /lock <add|chomod|owner|remove>");
		}

		// /lock arguments: /lock, /lock add <player>, /lock chmod friends <access level>, /lock chmod public <access level>

		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
		List<String> possibilities = new ArrayList<>();

		if(args.length == 0){
			possibilities.add("add");
			possibilities.add("chmod");
			possibilities.add("owner");
			possibilities.add("remove");
			return possibilities;
		}

		if(args.length == 1){
			possibilities.add("add");
			possibilities.add("chmod");
			possibilities.add("owner");
			possibilities.add("remove");

			for(String possibility : possibilities.toArray(new String[]{})){
				if(!possibility.startsWith(args[0].toLowerCase())){
					possibilities.remove(possibility);
				}
			}
		}

		if(args.length == 2){
			if(args[0].equalsIgnoreCase("add")||args[0].equalsIgnoreCase("remove")||args[0].equalsIgnoreCase("owner")){
				for(Player p : plugin.getServer().getOnlinePlayers()){
					possibilities.add(p.getName());
				}

				for(String possibility : possibilities.toArray(new String[]{})){
					if(!possibility.toLowerCase().startsWith(args[1].toLowerCase())){
						possibilities.remove(possibility);
					}
				}
			}else if(args[0].equalsIgnoreCase("chmod")){
				possibilities.add("friends");
				possibilities.add("public");

				for(String possibility : possibilities.toArray(new String[]{})){
					if(!possibility.startsWith(args[1].toLowerCase())){
						possibilities.remove(possibility);
					}
				}
			}
		}

		if(args.length == 3){
			if(args[0].equalsIgnoreCase("chmod")&&
					(args[1].equalsIgnoreCase("friends")||args[1].equalsIgnoreCase("public"))){
				for(AccessLevel value:AccessLevel.values()){
					possibilities.add(value.name());
				}

				for(String possibility : possibilities.toArray(new String[]{})){
					if(!possibility.startsWith(args[2].toUpperCase())){
						possibilities.remove(possibility);
					}
				}
			}
		}

		return possibilities;
	}
}
