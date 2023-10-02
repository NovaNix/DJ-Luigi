package io.github.novanix.djluigi.commands;

import io.github.novanix.djluigi.server.Server;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

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
	
	public abstract void executeCommand(Server s, SlashCommandInteractionEvent event);
	
	public String getCommandMessage()
	{
		return data.command();
	}
	
	public String getDescription()
	{
		return data.description();
	}
	
	public boolean isGlobal()
	{
		return data.global();
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
	
	public SlashCommandData generateSlashCommand()
	{
		SlashCommandData data = Commands.slash(getCommandMessage(), getDescription());
		
		setSlashCommandParameters(data);
		
		setSlashCommandPermissions(data);
			
		return data;
	}
	
	// Sets the parameters for the slash command
	protected void setSlashCommandParameters(SlashCommandData data)
	{
		for (int i = 0; i < getParameters().length; i++)
		{
			Parameter parameter = getParameters()[i];
			
			data.addOption(parameter.type(), parameter.name(), parameter.description(), parameter.required());
		}
	}
	
	// Sets the default permissions for the slash command
	protected void setSlashCommandPermissions(SlashCommandData data)
	{
		if (isDJOnly())
		{
			data.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ALL_VOICE_PERMISSIONS));
		}
		
		else if (isOwnerOnly())
		{
			// By setting it to disabled only administrators can use it
			data.setDefaultPermissions(DefaultMemberPermissions.DISABLED);
		}
	}
	
}
