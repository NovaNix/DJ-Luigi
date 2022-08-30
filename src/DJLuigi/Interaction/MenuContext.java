package DJLuigi.Interaction;

import DJLuigi.DJ;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;

// Information about the state of a menu
// Is used to allow for server or member specific menus without the creation of custom menu objects for each context
public class MenuContext
{
	
	public final Server server;
	public final String user;
	public final String channelID;
	
	public final String[] stateInfo; // An array of strings that are used to store state between interactions
	
	public MenuContext(GenericComponentInteractionCreateEvent event, String[] state)
	{
		server = DJ.getServer(event.getGuild().getId());
		user = event.getMember().getId();
		channelID = event.getChannel().getId();
		
		stateInfo = state;
	}
	
	public MenuContext(SlashCommandInteractionEvent event, String[] state)
	{
		server = DJ.getServer(event.getGuild().getId());
		user = event.getMember().getId();
		channelID = event.getChannel().getId();
		
		stateInfo = state;
	}
	
	public MenuContext(GenericComponentInteractionCreateEvent event)
	{
		server = DJ.getServer(event.getGuild().getId());
		user = event.getMember().getId();
		channelID = event.getChannel().getId();
		
		stateInfo = new String[] {};
	}
	
	public MenuContext(SlashCommandInteractionEvent event)
	{
		server = DJ.getServer(event.getGuild().getId());
		user = event.getMember().getId();
		channelID = event.getChannel().getId();
		
		stateInfo = new String[] {};
	}
	
}