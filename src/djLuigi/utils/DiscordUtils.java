package djLuigi.utils;

import java.time.Duration;

import djLuigi.DJ;

public class DiscordUtils
{
	public static String getUsersName(String userID)
	{
		return DJ.jda.getUserById(userID).getName();
	}
	
	public static String getLengthString(long length)
	{
		Duration duration = Duration.ofMillis(length);
		
		long HH = duration.toHours();
		long MM = duration.toMinutesPart();
		long SS = duration.toSecondsPart();
		
		if (HH > 0)
		{
			return String.format("%02d:%02d:%02d", HH, MM, SS);
		}
		
		else
		{
			return String.format("%02d:%02d", MM, SS);
		}
	}
}
