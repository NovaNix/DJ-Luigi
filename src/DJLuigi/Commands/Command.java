package DJLuigi.Commands;

import java.util.ArrayList;

import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface Command 
{

	public void executeCommand(Server S, ArrayList<String> Parameters, MessageReceivedEvent event);
	
	public default String getCommandMessage()
	{
		CommandData data = this.getClass().getAnnotation(CommandData.class);
		
		if (data != null)
		{
			return data.command();
		}
		
		else
		{
			System.out.println("COMMAND MESSAGE IS MISSING! FIX!!!!!!");
			return "MISSING COMMAND";
		}
	}
	
	public default String getDescription()
	{
		CommandData data = this.getClass().getAnnotation(CommandData.class);
		
		if (data != null)
		{
			return data.description();
		}
		
		else
		{
			System.out.println("COMMAND DESCRIPTION IS MISSING! FIX!!!!!!");
			return "MISSING DESCRIPTION";
		}
	}
	
	public default boolean isDJOnly()
	{
		CommandData data = this.getClass().getAnnotation(CommandData.class);
		
		if (data != null)
		{
			return data.djOnly();
		}
		
		else
		{
			return false;
		}
	}
	
	public default String[] getAliases()
	{
		CommandData data = this.getClass().getAnnotation(CommandData.class);
		
		if (data != null)
		{
			return data.aliases();
		}
		
		else
		{
			return new String[] {};
		}
	}
	
}
