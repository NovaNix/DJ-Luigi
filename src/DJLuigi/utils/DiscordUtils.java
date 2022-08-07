package DJLuigi.utils;

import DJLuigi.DJ;

public class DiscordUtils
{
	public static String getUsersName(String userID)
	{
		return DJ.jda.getUserById(userID).getName();
	}
}
