package DJLuigi.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import DJLuigi.DJ;
import DJLuigi.Commands.Audio.ClearQueueCommand;
import DJLuigi.Commands.Audio.CurrentSongCommand;
import DJLuigi.Commands.Audio.ForceSkipCommand;
import DJLuigi.Commands.Audio.LoopCommand;
import DJLuigi.Commands.Audio.PauseCommand;
import DJLuigi.Commands.Audio.PlayCommand;
import DJLuigi.Commands.Audio.QueueCommand;
import DJLuigi.Commands.Audio.RemoveFromQueueCommand;
import DJLuigi.Commands.Audio.ResumeCommand;
import DJLuigi.Commands.Audio.ShuffleCommand;
import DJLuigi.Commands.Debugging.PlaylistAsJSONCommand;
import DJLuigi.Commands.Debugging.QueueAsJSONCommand;
import DJLuigi.Commands.Debugging.SendParametersCommand;
import DJLuigi.Commands.Debugging.TestConfirmCommand;
import DJLuigi.Commands.Debugging.TestReactionListCommand;
import DJLuigi.Commands.Meta.ClearSettingsCommand;
import DJLuigi.Commands.Meta.DisconnectCommand;
import DJLuigi.Commands.Meta.HelpCommand;
import DJLuigi.Commands.Meta.JoinCommand;
import DJLuigi.Commands.Meta.SettingsCommand;
import DJLuigi.Commands.Meta.StatusCommand;
import DJLuigi.Commands.Playlist.AddSongCommand;
import DJLuigi.Commands.Playlist.CreatePlaylistCommand;
import DJLuigi.Commands.Playlist.DeletePlaylistCommand;
import DJLuigi.Commands.Playlist.ListPlaylistSongsCommand;
import DJLuigi.Commands.Playlist.ListPlaylistsCommand;
import DJLuigi.Commands.Playlist.PlayPlaylistCommand;
import DJLuigi.Commands.Playlist.PlaylistInfoCommand;
import DJLuigi.Commands.Playlist.ReloadPlaylistsCommand;
import DJLuigi.Commands.Playlist.RemoveSongCommand;
import DJLuigi.Server.Server;
import DJLuigi.utils.commandUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandHandler 
{
	
	public static HashMap<String, Command> commands = new HashMap<String, Command>();
	public static HashMap<String, Command> aliasCommands = new HashMap<String, Command>();

	private static Pattern commandInfoFinder = Pattern.compile("(\\w+)\\s*(.*)", Pattern.CASE_INSENSITIVE);
	private static Pattern commandParameterFinder = Pattern.compile("(\\S+)");
	
	// Loads all of the commands and prepares the command handler
	public static void init()
	{	
		// Initiate all of the commands used
		
		// Basic Commands
		
		loadCommand(new JoinCommand());
		loadCommand(new DisconnectCommand());
		loadCommand(new PlayCommand());
		loadCommand(new PauseCommand());
		loadCommand(new ResumeCommand());
		
		loadCommand(new CurrentSongCommand());
		
		// Queue Commands
		
		loadCommand(new ForceSkipCommand());
		loadCommand(new QueueCommand());
		loadCommand(new ClearQueueCommand());
		
		loadCommand(new RemoveFromQueueCommand());
		
		loadCommand(new LoopCommand());
		
		loadCommand(new ShuffleCommand());
		
		// Playlist Commands
		
		loadCommand(new CreatePlaylistCommand());
		loadCommand(new DeletePlaylistCommand());
		
		loadCommand(new AddSongCommand());
		loadCommand(new RemoveSongCommand());
		
		loadCommand(new ListPlaylistsCommand());
		loadCommand(new ListPlaylistSongsCommand());
		
		loadCommand(new PlayPlaylistCommand());
		
		loadCommand(new ReloadPlaylistsCommand());
		loadCommand(new PlaylistInfoCommand());
		
		// Misc Commands
		
		loadCommand(new HelpCommand());
		
		loadCommand(new SettingsCommand());
		loadCommand(new ClearSettingsCommand());
		loadCommand(new StatusCommand());
		
		
		// Initiate all commands used for debugging
		
		if (DJ.settings.debugMode)
		{
			loadCommand(new SendParametersCommand());
			loadCommand(new TestConfirmCommand());
			loadCommand(new TestReactionListCommand());
			loadCommand(new QueueAsJSONCommand());
			loadCommand(new PlaylistAsJSONCommand());
		}
		
		System.out.println("Loaded " + commands.size() + " Commands!");
		
	}
	
	private static void loadCommand(Command c)
	{
		commands.put(c.getCommandMessage(), c);
		
		for (String alias : c.getAliases())
		{
			aliasCommands.put(alias, c);
		}
	}
	
	public static void initSlashCommands()
	{
		System.out.println("Loading slash commands...");
		
		for (Server s : DJ.Servers.values())
		{
			generateSlashCommands(s.getGuild());
		}
		
		System.out.println("Finished loading slash commands for each server");
	}
	
	private static void generateSlashCommands(Guild guild)
	{
		System.out.println("Loading slash commands for \"" + guild.getName() + "\"");
		
		ArrayList<SlashCommandData> loadedCommands = new ArrayList<SlashCommandData>();
		
		for (Command c : commands.values())
		{
			try {
				loadedCommands.add(generateSlashCommand(c));			
			} catch (IllegalArgumentException e) 
			{
				System.err.println("Failed to generate command \"" + c.getCommandMessage() + "\"");
				e.printStackTrace();
			}
		}
		
		System.out.println(String.format("Generated %d/%d commands", loadedCommands.size(), commands.size()));
		
		guild.updateCommands().addCommands(loadedCommands).queue();
		
	}
	
	private static SlashCommandData generateSlashCommand(Command c)
	{
		SlashCommandData data = Commands.slash(c.getCommandMessage(), c.getDescription());
		
		for (int i = 0; i < c.getParameters().length; i++)
		{
			Parameter parameter = c.getParameters()[i];
			
			data.addOption(parameter.type(), parameter.name(), parameter.description(), parameter.required());
		}
		
		if (c.isDJOnly())
		{
			data.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ALL_VOICE_PERMISSIONS));
		}
			
		return data;
	}
	
	// Returns the specified command from the loaded commands. It will also work with aliases.
	// Returns null if the command does not exist
	public static Command getCommand(String name)
	{
		Command c = commands.get(name);
		
		if (c != null)
		{
			return c; 
		}
		
		else
		{
			return aliasCommands.get(name);
		}
	}
	
	public static void processCommand(Server server, SlashCommandInteractionEvent event)
	{
//		Matcher matcher = commandInfoFinder.matcher(event.getMessage().getContentRaw());
//		
//	    if (!matcher.find()) 
//	    { 
//	    	System.out.println("Invalid regex result! Regex might be broken!");
//	    	
//	    	return; 
//	    } 
//	    
	    String requestedCommand = event.getName();
		
	    System.out.println("Found command: " + requestedCommand);
	    
	    Command c = null;
	    
	    if (commands.containsKey(requestedCommand))
	    {
	    	c = commands.get(requestedCommand);
	    }
	    
//	    else if (aliasCommands.containsKey(requestedCommand))
//	    {
//	    	c = aliasCommands.get(requestedCommand);
//	    }
	    
	    else
	    {
	    	server.SendMessage("Invalid command: \"" + requestedCommand + "\"!");
	    	return;
	    }
	    
	    if (server.data.settings.djOnlyMode)
	    {
	    	if (!commandUtils.isMemberDJ(event.getMember()))
	    	{
	    		server.SendMessage("The server is in DJ Only Mode!");
	    		return;
	    	}
	    }
	    	
	    //ArrayList<String> parameters = extractParameters(matcher.group(2));
	    
	    c.executeCommand(server, event);
	    
	    // Ensure that the interaction was acknowledged. Discord requires them to be
	    if (!event.isAcknowledged())
	    {
	    	event.reply("I FORGOT TO ADD A RESPONSE FOR THIS MESSAGE! NOTIFY A DEVELOPER IMMEDIETLY!").queue();
	    }
	  
	}
	
	private static ArrayList<String> extractParameters(String input)
	{
		Matcher parameterMatcher = commandParameterFinder.matcher(input);
    	
    	ArrayList<String> parameters = new ArrayList<String>();
    	
    	while (parameterMatcher.find()) 
    	{
    		parameters.add(parameterMatcher.group());
    	}
    	
    	return parameters;
	}
	
}
