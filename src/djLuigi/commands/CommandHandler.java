package djLuigi.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import djLuigi.DJ;
import djLuigi.commands.audio.ClearQueueCommand;
import djLuigi.commands.audio.CurrentSongCommand;
import djLuigi.commands.audio.ForceSkipCommand;
import djLuigi.commands.audio.PauseCommand;
import djLuigi.commands.audio.LoopCommand;
import djLuigi.commands.audio.PlayCommand;
import djLuigi.commands.audio.QueueCommand;
import djLuigi.commands.audio.RemoveCommand;
import djLuigi.commands.audio.ResumeCommand;
import djLuigi.commands.audio.ShuffleCommand;
import djLuigi.commands.debugging.PermissionsTestCommand;
import djLuigi.commands.debugging.PlaylistAsJSONCommand;
import djLuigi.commands.debugging.QueueAsJSONCommand;
import djLuigi.commands.debugging.TestConfirmCommand;
import djLuigi.commands.debugging.TestMenuListCommand;
import djLuigi.commands.meta.AboutCommand;
import djLuigi.commands.meta.DisconnectCommand;
import djLuigi.commands.meta.HelpCommand;
import djLuigi.commands.meta.JoinCommand;
import djLuigi.commands.meta.SettingsCommand;
import djLuigi.commands.playlist.ListPlaylistsCommand;
import djLuigi.commands.playlist.PlayPlaylistCommand;
import djLuigi.commands.playlist.PlaylistCommand;
import djLuigi.commands.playlist.ReloadPlaylistsCommand;
import djLuigi.server.Server;
import djLuigi.server.ServerHandler;
import djLuigi.utils.CommandUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

public class CommandHandler 
{
	
	public static HashMap<String, Command> commands = new HashMap<String, Command>();
	public static HashMap<CommandCategory, List<Command>> commandsByCategories = new HashMap<CommandCategory, List<Command>>();
	
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
		
		loadCommand(new RemoveCommand());
		
		loadCommand(new LoopCommand());
		
		loadCommand(new ShuffleCommand());
		
		// Playlist Commands
		
		loadCommand(new PlaylistCommand());
		
		loadCommand(new ListPlaylistsCommand());
		
		loadCommand(new PlayPlaylistCommand());
		loadCommand(new ReloadPlaylistsCommand());
		
		// Misc Commands
		
		loadCommand(new HelpCommand());
		
		loadCommand(new SettingsCommand());
		loadCommand(new AboutCommand());
		
		
		// Initiate all commands used for debugging
		
		if (DJ.settings.debugMode)
		{
			loadCommand(new TestConfirmCommand());
			loadCommand(new TestMenuListCommand());
			loadCommand(new QueueAsJSONCommand());
			loadCommand(new PlaylistAsJSONCommand());
			loadCommand(new PermissionsTestCommand());
		}
		
		System.out.println("Loaded " + commands.size() + " Commands!");
		
	}
	
	private static void loadCommand(Command c)
	{
		commands.put(c.getCommandMessage(), c);
		
		// Add the command to the category list
		
		List<Command> categoryCommandsList = commandsByCategories.get(c.getCategory());
		
		if (categoryCommandsList == null)
		{
			categoryCommandsList = new ArrayList<Command>();
			commandsByCategories.put(c.getCategory(), categoryCommandsList);
		}
		
		categoryCommandsList.add(c);
	}
	
	public static void initSlashCommands()
	{
		System.out.println("Loading slash commands...");
		
		// Load global commands
		
		ArrayList<SlashCommandData> globalCommands = new ArrayList<SlashCommandData>();
		
		for (Command c : commands.values())
		{
			if (c.isGlobal())
			{
				globalCommands.add(c.generateSlashCommand());
			}
		}
		
		DJ.jda.updateCommands().addCommands(globalCommands).queue();
		
		System.out.println("Loaded " + globalCommands.size() + " global commands");
		
		// Load local commands
		
		for (Server s : ServerHandler.getServers())
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
		
		int globalCommands = 0; // The number of global commands to take out of the total loaded commands cound
		
		for (Command c : commands.values())
		{
			if (!c.isGlobal())
			{
				try {
					loadedCommands.add(c.generateSlashCommand());			
				} catch (IllegalArgumentException e) 
				{
					System.err.println("Failed to generate command \"" + c.getCommandMessage() + "\"");
					e.printStackTrace();
				}
			}
			
			else
			{
				globalCommands++;
			}
		}
		
		System.out.println(String.format("Generated %d/%d commands", loadedCommands.size(), commands.size() - globalCommands));
		
		guild.updateCommands().addCommands(loadedCommands).queue();
		
	}
	
	// Returns the specified command from the loaded commands. It will also work with aliases.
	// Returns null if the command does not exist
	public static Command getCommand(String name)
	{
		return commands.get(name);
	}
	
	// Returns a list of all of the commands with the specific category
	public static List<Command> getCommands(CommandCategory category)
	{
		return commandsByCategories.get(category);
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
	    	if (!CommandUtils.isMemberDJ(event.getMember()))
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