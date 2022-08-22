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
	
	public default boolean isOwnerOnly()
	{
		CommandData data = this.getClass().getAnnotation(CommandData.class);
		
		if (data != null)
		{
			return data.ownerOnly();
		}
		
		else
		{
			return true; // RETURN TRUE BECAUSE OWNER ONLY COMMANDS ARE POWERFUL AND IF SOMETHING GOES WRONG NOT ALLOWING THE COMMAND IS BETTER THAN ALLOWING IT
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
	
	public default CommandCategory getCategory()
	{
		CommandData data = this.getClass().getAnnotation(CommandData.class);
		
		if (data != null)
		{
			return data.category();
		}
		
		else
		{
			return CommandCategory.Other;
		}
	}
	
	public default int getSortOrder()
	{
		CommandData data = this.getClass().getAnnotation(CommandData.class);
		
		if (data != null)
		{
			return data.sortOrder();
		}
		
		else
		{
			return 10;
		}
	}
	
	public default Parameter[] getParameters()
	{
		CommandData data = this.getClass().getAnnotation(CommandData.class);
		
		if (data != null)
		{
			return data.parameters();
		}
		
		else
		{
			return new Parameter[] {};
		}
	}
	
}
