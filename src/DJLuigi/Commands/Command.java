package DJLuigi.Commands;

import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public abstract class Command 
{

	private CommandData data;
	
	public Command()
	{
		this.data = this.getClass().getAnnotation(CommandData.class);
		
		if (data == null)
		{
			System.err.println("CANNOT FIND THE COMMANDDATA ANNOTATION ON CLASS " + this.getClass().getName() + ". FIX IMMEDIATELY!");
		}
	}
	
	public abstract void executeCommand(Server S, SlashCommandInteractionEvent event);
	
	public String getCommandMessage()
	{
		return data.command();
	}
	
	public String getDescription()
	{
		return data.description();
	}
	
	public boolean isDJOnly()
	{
		return data.djOnly();
	}
	
	public boolean isOwnerOnly()
	{
		return data.ownerOnly();
	}
	
	public String[] getAliases()
	{
		return data.aliases();
	}
	
	public CommandCategory getCategory()
	{
		return data.category();
	}
	
	public int getSortOrder()
	{
		return data.sortOrder();
	}
	
	public Parameter[] getParameters()
	{
		return data.parameters();
	}
	
}
