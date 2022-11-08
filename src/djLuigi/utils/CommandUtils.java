package djLuigi.utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import java.awt.Color;

public class CommandUtils 
{

	public static final Color ERROR_COLOR = new Color(0xf30c0d); 
	
	// The permissions required to be considered a DJ
	public static final long DJ_REQUIRED_PERMISSIONS = Permission.ALL_VOICE_PERMISSIONS;
	
	// Pulled from https://www.geeksforgeeks.org/check-if-url-is-valid-or-not-in-java/
	public static boolean isValidURL(String url)
    {
        /* Try creating a valid URL */
        try {
            new URL(url).toURI();
            return true;
        }
          
        // If there was an Exception
        // while creating URL object
        catch (Exception e) {
            return false;
        }
    }
	
	public static String parametersToString(ArrayList<String> parameters)
	{
		StringBuilder combined = new StringBuilder();
		
		for (int i = 0; i < parameters.size(); i++)
		{
			combined.append(parameters.get(i));
			
			if (i < parameters.size() - 1)
			{
				combined.append(" ");
			}
		}
		
		return combined.toString();
	}
	
	public static boolean hasEnoughParameters(ArrayList<String> parameters, int min, int max)
	{
		return (parameters.size() >= min && parameters.size() <= max);
	}
	
	public static boolean isMemberDJ(Member u)
	{
		return u.hasPermission(Permission.getPermissions(DJ_REQUIRED_PERMISSIONS));
	}
	
	public static final Character[] INVALID_SPECIFIC_CHARS = {'"', '*', '<', '>', '?', '|', '\000'};
	
	public static boolean isValidFileName(String name)
	{
		if (name == null || name.isEmpty() || name.length() > 255) {
	        return false;
	    }
	    return Arrays.stream(INVALID_SPECIFIC_CHARS)
	      .noneMatch(ch -> name.contains(ch.toString()));
	}
	
}
