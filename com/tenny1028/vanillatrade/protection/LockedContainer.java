package com.tenny1028.vanillatrade.protection;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jasper on 4/3/16.
 */
public class LockedContainer {
	OfflinePlayer owner;
	Location location;
	FileConfiguration config = null;
	AccessLevel publicAccessLevel = AccessLevel.NO_ACCESS;
	AccessLevel friendsAccessLevel = AccessLevel.READ_WRITE;
	List<String> friends = new ArrayList<>();

	public LockedContainer(OfflinePlayer owner, Location location) {
		if(owner==null||location==null){
			throw new IllegalArgumentException();
		}
		this.owner = owner;
		this.location = location;
	}

	public LockedContainer(OfflinePlayer owner, Location location, FileConfiguration config, AccessLevel publicAccessLevel, AccessLevel friendsAccessLevel, List<String> friends) {
		if(owner==null||location==null){
			throw new IllegalArgumentException();
		}
		this.owner = owner;
		this.location = location;
		this.config = config;
		this.publicAccessLevel = publicAccessLevel;
		this.friendsAccessLevel = friendsAccessLevel;
		this.friends = friends;
	}

	public LockedContainer(OfflinePlayer owner, Location location, AccessLevel publicAccessLevel, AccessLevel friendsAccessLevel, List<String> friends) {
		if(owner==null||location==null){
			throw new IllegalArgumentException();
		}
		this.owner = owner;
		this.location = location;
		this.publicAccessLevel = publicAccessLevel;
		this.friendsAccessLevel = friendsAccessLevel;
		this.friends = friends;
	}

	public Container getContainerBlock(){
		return (Container)getLocation().getBlock().getState();
	}

	public OfflinePlayer getOwner() {
		return owner;
	}

	public Location getLocation() {
		return location;
	}

	public FileConfiguration getConfig() {
		return config;
	}

	public AccessLevel getPublicAccessLevel() {
		return publicAccessLevel;
	}

	public AccessLevel getFriendsAccessLevel() {
		return friendsAccessLevel;
	}

	public List<String> getFriends() {
		return friends;
	}

	public void setOwner(OfflinePlayer owner) {
		this.owner = owner;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public void setConfig(FileConfiguration config) {
		this.config = config;
	}

	public void setPublicAccessLevel(AccessLevel level){
		this.publicAccessLevel = level;
	}

	public void setFriendsAccessLevel(AccessLevel friendsAccessLevel) {
		this.friendsAccessLevel = friendsAccessLevel;
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}

	public void addFriend(OfflinePlayer friend){
		this.friends.add(friend.getUniqueId().toString());
	}

	public void removeFriend(OfflinePlayer friend){
		this.friends.remove(friend.getUniqueId().toString());
	}

	public boolean playerIsFriend(OfflinePlayer player){
		return this.friends.contains(player.getUniqueId().toString());
	}

	public AccessLevel getAccessLevelOf(OfflinePlayer player){
		if(player.isOnline()){
			if(player.getPlayer().hasPermission("vanillatrade.op")){
				return AccessLevel.FULL_ACCESS;
			}
		}

		return getAccessLevelOfIgnoreOp(player);
	}

	public AccessLevel getAccessLevelOfIgnoreOp(OfflinePlayer player){
		if(getOwner().getName().equals(player.getName())){
			return AccessLevel.FULL_ACCESS;
		}

		if(playerIsFriend(player)){
			return getFriendsAccessLevel();
		}

		return  getPublicAccessLevel();
	}
}
