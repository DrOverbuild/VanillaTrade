package com.tenny1028.vanillatrade.protection;

/**
 * Created by jasper on 4/4/16.
 *
 * The access publicAccessLevel that non-owners have.
 */
public enum AccessLevel {

	/**
	 * Whoever has this permission can break, chmod, and everything else to the chest.
	 */
	FULL_ACCESS,

	/**
	 * Whoever has this permission can view and modify contents of chest but cannot break the chest or claim ownership.
	 */
	READ_WRITE,

	/**
	 * When public has this permission, hoppers can add contents, but players have NO_ACCESS.
	 */
	WRITE_ONLY,

	/**
	 * Whoever has this permission can view contents of chest but not modify, break, or claim ownership of the chest.
	 */
	READ_ONLY,

	/**
	 * Whoever has this permission cannot view contents of chest.
	 */
	NO_ACCESS;

	public static boolean hasPermission(AccessLevel levelOfPlayer, AccessLevel levelNeeded){
		return levelNeeded.ordinal() >= levelOfPlayer.ordinal();
	}
}
