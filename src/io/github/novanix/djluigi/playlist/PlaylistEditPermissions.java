package io.github.novanix.djluigi.playlist;

import io.github.novanix.djluigi.utils.CommandUtils;
import net.dv8tion.jda.api.entities.Member;

public class PlaylistEditPermissions 
{

	public static final int EDIT_EVERYONE = 0B00000001; 
	public static final int EDIT_DJ = 0B00000010;
	public static final int EDIT_EDITORS = 0B00000100;
	public static final int EDIT_OWNER = 0B00001000;
	
	public static final int USER_EVERYONE = 0B00000001; // Represents that the user is a part of everyone (is always set)
	public static final int USER_DJ = 0B00000010; // Represents that the user is a DJ in the specific server
	public static final int USER_EDITOR = 0B00000100; // Represents that the user is an editor of the playlist
	public static final int USER_OWNER = 0B00001000; // Represents that the user is the owner of the playlist
	
	public static int getUserPermissions(Member m, Playlist p)
	{
		int permissionsBitmask = USER_EVERYONE;
		
		if (CommandUtils.isMemberDJ(m))
			permissionsBitmask = permissionsBitmask | USER_DJ;
		
		if (p.isEditor(m.getUser()))
			permissionsBitmask = permissionsBitmask | USER_EDITOR;
		
		if (p.creatorID.equals(m.getId()))
			permissionsBitmask = permissionsBitmask | USER_OWNER;
		
		return permissionsBitmask;
	}
	
}
