package DJLuigi.Commands;

import java.util.ArrayList;
import java.util.HashMap;
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
import DJLuigi.Commands.Debugging.TestConfirmCommand;
import DJLuigi.Commands.Debugging.TestReactionListCommand;
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
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandHandler 
{
	
	public static HashMap<String, Command> commands = new HashMap<String, Command>();
	// TODO consider removing aliases as slash commands no longer support them
//	public static HashMap<String, Command> aliasCommands = new HashMap<String, Command>();

//	private static Pattern commandInfoFinder = Pattern.compile("(\\w+)\\s*(.*)", Pattern.CASE_INSENSITIVE);
//	private static Pattern commandParameterFinder = Pattern.compile("(\\S+)");
	
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
		loadCommand(new StatusCommand());
		
		
		// Initiate all commands used for debugging
		
		if (DJ.settings.debugMode)
		{
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
		
//		for (String alias : c.getAliases())
//		{
//			aliasCommands.put(alias, c);
//		}
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
		
		int serverCommandCount = guild.retrieveCommands().complete().size();
		
		System.out.println("Current number of commands in the guild: " + serverCommandCount);
		
		ArrayList<SlashCommandData> loadedCommands = new ArrayList<SlashCommandData>();
		
		for (Command c : commands.values())
		{
			try {
				loadedCommands.add(c.generateSlashCommand());			
			} catch (IllegalArgumentException e) 
			{
				System.err.println("Failed to generate command \"" + c.getCommandMessage() + "\"");
				e.printStackTrace();
			}
		}
		
		System.out.println(String.format("Generated %d/%d commands", loadedCommands.size(), commands.size()));
		
		guild.updateCommands().addCommands(loadedCommands).queue();
		
	}
	
	// Returns the specified command from the loaded commands. It will also work with aliases.
	// Returns null if the command does not exist
	public static Command getCommand(String name)
	{
		return commands.get(name);
	}
	
	public static void processCommand(Server server, SlashCommandInteractionEvent event)
	{

	    String requestedCommand = event.getName();
		
	    System.out.println("Processing command \"" + event.getCommandString() + "\"");
	    
	    Command c = null;
	    
	    if (commands.containsKey(requestedCommand))
	    {
	    	c = commands.get(requestedCommand);
	    }
	    
	    else
	    {
	    	System.out.println("Failed to find command...");
	    	event.reply("Invalid command: \"" + requestedCommand + "\"!").setEphemeral(true).queue();
	    	return;
	    }
	    
	    if (server.data.settings.djOnlyMode)
	    {
	    	if (!commandUtils.isMemberDJ(event.getMember()))
	    	{
	    		event.reply("The server is in DJ Only Mode!").setEphemeral(true).queue();
	    		return;
	    	}
	    }
	    
	    // The execution of the command is wrapped in a try catch block to provide a user friendly response if the command has a problem
	    try 
	    {
	    	c.executeCommand(server, event);
	    	
	    } catch (Exception e)
	    {
	    	System.err.println("The slash command " + event.getName() + " threw an error. Please fix.");
	    	e.printStackTrace();
	    	
	    	event.reply("Something went wrong while executing your command. Please notify a developer.").queue();
	    	return;
	    }
	    
	    // Ensure that the interaction was acknowledged. Discord requires them to be
	    if (!event.isAcknowledged())
	    {
	    	System.err.println("The slash command " + event.getName() + " did not acknowledge the event. Please fix.");
	    	event.reply("Something went wrong while trying to respond to your command. Please notify a developer.").queue();
	    }
	  
	}
	
}
