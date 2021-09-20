package DJLuigi.utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

public class commandUtils 
{

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
			combined.append(" ");
		}
		
		return combined.toString();
	}
	
	public static boolean isMemberDJ(Member u)
	{
		List<Role> Roles = u.getRoles();
		
		for (int i = 0; i < Roles.size(); i++)
		{
			if (Roles.get(i).getName().equalsIgnoreCase("dj"))
			{
				return true;
			}
		}
		
		return false;
		
	}
	
}
