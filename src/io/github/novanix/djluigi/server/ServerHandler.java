package io.github.novanix.djluigi.server;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import io.github.novanix.djluigi.DJ;
import io.github.novanix.djluigi.commands.CommandHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ServerHandler extends ListenerAdapter 
{
	private static HashMap<String, Server> servers = new HashMap<String, Server>();
	
	public ServerHandler()
	{
		
	}
	
	// Loads all of the servers the bot is a part of. 
	// Returns the number of servers loaded
	public static int loadServers()
	{
		List<Guild> guilds = DJ.jda.getGuilds();
		
		for (Guild guild : guilds)
		{
			servers.put(guild.getId(), new Server(guild.getId()));
		}
		
		return guilds.size();
	}
	
	public static Server getServer(String id)
	{
		return servers.get(id);
	}
	
	public static Server getServer(Guild g)
	{
		return servers.get(g.getId());
	}
	
	public static Collection<Server> getServers()
	{
		return servers.values();
	}
	
	public static int getLoadedServerCount()
	{
		return servers.size();
	}
	
	// Event Handling
	
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
	{
		Server host = getServer(event.getGuild());
		host.setActiveTextChannel(event.getChannel());
		
		CommandHandler.processCommand(host, event);
	}
	
	@Override
	public void onGuildJoin(GuildJoinEvent event)
	{
		Guild guild = event.getGuild();
		servers.put(guild.getId(), new Server(guild.getId()));
	}
	
	
	// TODO implement auto complete support
	@Override
	public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event)
	{

	}
	
	@Override
	public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event)
	{
		AudioChannelUnion left = event.getChannelLeft();

		if (left == null)
			return;
		
		Server host = getServer(left.getGuild());

		if (host.isAloneInVC())
		{
			host.leaveVC();
		}
	}
}
