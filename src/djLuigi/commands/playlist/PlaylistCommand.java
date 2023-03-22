package djLuigi.commands.playlist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import djLuigi.DJ;
import djLuigi.interaction.MenuHandler;
import djLuigi.interaction.menus.paged.EditorListMenu;
import djLuigi.interaction.menus.paged.PlaylistSongsMenu;
import djLuigi.server.Server;
import djLuigi.audio.Song;
import djLuigi.commands.Command;
import djLuigi.commands.CommandCategory;
import djLuigi.commands.CommandData;
import djLuigi.playlist.Playlist;
import djLuigi.playlist.PlaylistManager;
import djLuigi.playlist.exceptions.PlaylistAccessException;
import djLuigi.playlist.loading.PlaylistLoadTrackHandler;
import djLuigi.utils.CommandUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

@CommandData
(
	command = "playlist", 
	description = "Used to create and modify playlists",
	// PARAMETERS ARE NOT STORED HERE! THEY ARE GENERATED DUE TO THE EXISTANCE OF SUBCOMMANDS!
	category = CommandCategory.Playlist
)
// A single command for doing almost everything with playlists
public class PlaylistCommand extends Command
{

	private HashMap<String, Subcommand> subcommands = new HashMap<String, Subcommand>();
	private HashMap<String, SubcommandData> subcommandData = new HashMap<String, SubcommandData>();
	
	public PlaylistCommand()
	{
		generateSubcommands();
	}
	
	@Override
	protected void setSlashCommandParameters(SlashCommandData data)
	{
		ArrayList<SubcommandData> commandDataList = new ArrayList<SubcommandData>();
		
		for (SubcommandData commandData : subcommandData.values())
		{
			commandDataList.add(commandData);
		}
		
		data.addSubcommands(commandDataList);
		
		// Add the subcommand groups
		// For now this is done manually
		
		SubcommandGroupData editorsCommand = new SubcommandGroupData("editors", "Edit playlist editors");
		
		var addEditor = new SubcommandData("add", "Add an editor")
				.addOption(OptionType.STRING, "playlist", "The playlist to edit", true)
				.addOption(OptionType.USER, "editor", "Who to make an editor", true);
		
		var removeEditor = new SubcommandData("remove", "Remove an editor")
				.addOption(OptionType.STRING, "playlist", "The playlist to edit", true)
				.addOption(OptionType.USER, "editor", "Who to remove from being an editor", true);
		
		var listEditors = new SubcommandData("list", "Lists the editors")
				.addOption(OptionType.STRING, "playlist", "The playlist to list the editors for", true);

		var clearEditors = new SubcommandData("clear", "Clears the editors")
				.addOption(OptionType.STRING, "playlist", "The playlist to clear the editors of", true);

		editorsCommand.addSubcommands(addEditor, removeEditor, listEditors, clearEditors);
		
		data.addSubcommandGroups(editorsCommand);
	}
	
	protected void generateSubcommands()
	{
		addSubcommand(this::createPlaylist, "create", "Creates a new playlist",
											new OptionData(OptionType.STRING, "name", "The name of the new playlist", true),
											new OptionData(OptionType.STRING, "description", "The description of the playlist", true));
		
		addSubcommand(this::deletePlaylist, "delete", "Deletes a playlist",
											new OptionData(OptionType.STRING, "playlist", "The name of the playlist to delete", true));
		
		addSubcommand(this::addSong, "add", "Adds a song to a playlist",
											new OptionData(OptionType.STRING, "playlist", "The name of the playlist to add the song to", true),
											new OptionData(OptionType.STRING, "song", "The link to the song to add. If not specified, the current song is added"));
		
		addSubcommand(this::removeSong, "remove", "Removes a song from a playlist",
											new OptionData(OptionType.STRING, "playlist", "The name of the playlist to remove the song from", true),
											new OptionData(OptionType.INTEGER, "song", "The index of the song to remove", true));
		
		addSubcommand(this::listSongs, "songs", "Lists the songs in a playlist",
											new OptionData(OptionType.STRING, "playlist", "The name of the playlist list the songs from", true));
		
		addSubcommand(this::playlistInfo, "info", "Lists info about a playlist",
											new OptionData(OptionType.STRING, "playlist", "The name of the playlist", true));
		
		// Add the subcommand groups
		// For now this is done manually 
		
		subcommands.put("editors/add", this::addEditor);
		subcommands.put("editors/remove", this::removeEditor);
		subcommands.put("editors/list", this::listEditors);
		subcommands.put("editors/clear", this::clearEditors);
	}
	
	protected void addSubcommand(Subcommand command, String name, String description, OptionData... options)
	{
		SubcommandData subcommand = new SubcommandData(name, description);
		subcommand.addOptions(options);
		
		subcommands.put(name, command);
		subcommandData.put(name, subcommand);
	}
	
	@Override
	public void executeCommand(Server s, SlashCommandInteractionEvent event)
	{

		String path = event.getFullCommandName().replaceFirst(getCommandMessage() + " ", "");
		
		try
		{
			subcommands.get(path).executeCommand(s, event);
		} 
		
		catch (IOException e) // Error occurred while saving 
		{
			event.reply("Something went wrong while saving the playlist! Please contact a developer!").queue();
			e.printStackTrace();
		}
		
		catch (PlaylistAccessException e)
		{
			// Dont print the stacktrace because PlaylistAccessExceptions are caused by user error
			event.reply("An error occurred while executing the command!\n" + e.getMessage()).setEphemeral(true).queue();
		}
	}
	
	// Utility functions for making subcommand code cleaner
	
	// Gets the playlist with the same name
	// Throws a PlaylistAccessException if there is no playlist with that name or there are multiple playlists with that name
	private Playlist getPlaylist(String name) throws PlaylistAccessException
	{
		if (!PlaylistManager.hasPlaylist(name))
			throw new PlaylistAccessException("Unknown playlist: \"" + name + "\"", PlaylistAccessException.Type.UnknownPlaylist);
		
		if (PlaylistManager.hasDuplicatePlaylistName(name))
			throw new PlaylistAccessException("There are multiple playlists with the same `" + name + "`, please include the playlist tag with the name!", PlaylistAccessException.Type.DuplicatePlaylistNames);
		
		return PlaylistManager.getPlaylist(name);
	}
	
	// Ensures that whoever is executing the command has the ability to modify the playlist
	private void assertCanEdit(Playlist p, Member m) throws PlaylistAccessException
	{
		if (!p.memberCanEdit(m))
			throw new PlaylistAccessException("You don't have permission to edit this playlist!", PlaylistAccessException.Type.MissingPermissions);
	}
	
	// Ensures that whoever is executing the command is the owner of the playlist
	private void assertIsOwner(Playlist p, Member m) throws PlaylistAccessException
	{
		if (!p.creatorID.equals(m.getId()))
			throw new PlaylistAccessException("You must be the owner of the playlist to perform this action!", PlaylistAccessException.Type.MissingPermissions);
	}
	
	// Subcommand functions
	
	// playlist/create [name] [description]
	// Creates a new playlist. If name is not defined then it opens up a modal to create it 
	protected void createPlaylist(Server s, SlashCommandInteractionEvent event) throws JsonGenerationException, JsonMappingException, IOException
	{
		
		if (event.getOption("name") == null)
		{
			// Respond with modal
			// for now this is disabled by requiring parameters
			// TODO implement modal input
		}
		
		else
		{
			// Create playlist now
			String name = event.getOption("name").getAsString();
			String description = event.getOption("description").getAsString();
			
			if (!Playlist.isValidName(name))
			{
				event.reply("Invalid playlist name! You cannot include the character \"/\"").setEphemeral(true).queue();
				return;
			}
			
			if (name.length() > Playlist.NAME_MAX_CHARS)
			{
				event.reply("The playlist name is too long! Please pick a shorter name (Max = " + Playlist.NAME_MAX_CHARS + ", your name length = " + name.length() + ")").setEphemeral(true).queue();
				return;
			}
			
			if (description.length() > Playlist.MAX_DESCRIPTION_CHARS)
			{
				event.reply("The playlist description is too long! Please pick a shorter name").setEphemeral(true).queue();
				return;
			}	
			
			if (!CommandUtils.isValidFileName(name))
			{
				event.reply("Invalid playlist name! (Cannot include the characters '\"', '*', '<', '>', '?', '|')").setEphemeral(true).queue();
				return;
			}
			
			Playlist created = new Playlist(name, description, event.getUser().getId(), s.getId());
			PlaylistManager.addPlaylist(created);
			event.reply("Created playlist `" + created.getUniqueName() + "`").queue();
		}
	}

	// playlist/delete <playlist>
	// Deletes the specified playlist
	// TODO add confirmation
	protected void deletePlaylist(Server s, SlashCommandInteractionEvent event) throws PlaylistAccessException, IOException
	{
		Playlist p = getPlaylist(event.getOption("playlist").getAsString());
		assertIsOwner(p, event.getMember());

		if (PlaylistManager.deletePlaylist(p.name)) 
		{
			event.reply("Successfully deleted the playlist, " + p.getUniqueName() + "!").queue();
		}

		else 
		{
			event.reply("Something went wrong while deleting the playlist... Please notify a developer.").queue();
		}

	}
	
	// playlist/add <playlist> <song>
	// Adds a song to the specified playlist
	protected void addSong(Server s, SlashCommandInteractionEvent event) throws JsonGenerationException, JsonMappingException, IOException, PlaylistAccessException
	{
		Playlist p = getPlaylist(event.getOption("playlist").getAsString());
		assertCanEdit(p, event.getMember());

		if (event.getOption("song") == null) // Add current song
		{	
			if (s.queue.size() == 0)
			{
				event.reply("No song is currently playing!").queue();
				return;
			}

			AudioTrack currentSong = s.queue.getTrack(0);

			Song song = new Song(currentSong);

			p.addSong(song);
			event.reply("Added song: `" + song.name + "` to playlist " + p.name).queue();
		}

		else // Add specified song
		{	
			String song = event.getOption("song").getAsString();
			
			if (!CommandUtils.isValidURL(song))
			{
				event.reply("Invalid song link: `" + song + "`").queue();
				return;
			}

			event.deferReply().queue();
			
			DJ.playerManager.loadItem(song, new PlaylistLoadTrackHandler(s, p, event));
		}
	}
	
	// playlist/remove <playlist> <song>
	// Removes a song from the specified playlist
	// If the song is a number, it removes that index from the playlist. Otherwise it searches the playlist for a song with the name specified
	protected void removeSong(Server s, SlashCommandInteractionEvent event) throws JsonGenerationException, JsonMappingException, IOException, PlaylistAccessException
	{
		Playlist p = getPlaylist(event.getOption("playlist").getAsString());
		int songIndex = event.getOption("song").getAsInt() - 1;
		
		assertCanEdit(p, event.getMember());
		
		if (songIndex == -1)
		{
			event.reply("Index cannot be 0!").queue();
			return;
		}
		
		else if (songIndex < 0)
		{
			event.reply("Song Index Cannot be Negative!").queue();
			return;
		}
		
		else if (songIndex >= s.queue.size())
		{
			event.reply("That index is out of bounds! (There are only " + p.size() + " songs in the playlist!)").queue();
			return;
		}
		
		Song removed = p.removeSong(songIndex);
		event.reply("Removed song " + removed.name + " from the playlist.").queue();

	}
	
	// playlist/songs <playlist>
	// Lists the songs in the playlist
	protected void listSongs(Server s, SlashCommandInteractionEvent event) throws PlaylistAccessException
	{
		Playlist p = getPlaylist(event.getOption("playlist").getAsString());
		
		MenuHandler.createMenu(PlaylistSongsMenu.class, event, p.name);
	}
	
	// playlist/info
	// Tells info about a playlist
	protected void playlistInfo(Server s, SlashCommandInteractionEvent event) throws PlaylistAccessException
	{
		Playlist p = getPlaylist(event.getOption("playlist").getAsString());
			
		User user = DJ.jda.retrieveUserById(p.creatorID).complete();
			
		MessageEmbed embed = new EmbedBuilder()
				.setTitle(p.displayName)
				.setColor(DJ.getPrimaryColor())
				.setFooter("Owner: " + user.getName() + ", " + p.editors.size() + " editors", user.getAvatarUrl())
				.setThumbnail("https://i.redd.it/b2pilioyu7u21.jpg")
				.addField("Songs", p.songs.size() + " Songs", false)
				.addField("Is Public", "" + p.isPublic, false)
				.addField("Edit Status", Integer.toBinaryString(p.editPermissions) + " (will be replaced with more readable value later)", false)
				.build();
			
		event.replyEmbeds(embed).queue();
	}
	
	// playlist/editors/add
	protected void addEditor(Server s, SlashCommandInteractionEvent event) throws JsonGenerationException, JsonMappingException, IOException, PlaylistAccessException
	{
		Playlist p = getPlaylist(event.getOption("playlist").getAsString());
		User newEditor = event.getOption("editor").getAsUser();
		
		assertIsOwner(p, event.getMember());
		
		if (p.isEditor(newEditor))
		{
			event.reply(newEditor.getAsMention() + " is already an editor!").queue();
			return;
		}
		
		p.addEditor(newEditor);
		event.reply("Added " + newEditor.getAsMention() + " as an editor").queue();	
	}
	
	// playlist/editors/remove
	protected void removeEditor(Server s, SlashCommandInteractionEvent event) throws JsonGenerationException, JsonMappingException, IOException, PlaylistAccessException
	{
		Playlist p = getPlaylist(event.getOption("playlist").getAsString());
		User removedEditor = event.getOption("editor").getAsUser();
		
		assertIsOwner(p, event.getMember());
		
		if (!p.isEditor(removedEditor))
		{
			event.reply(removedEditor.getAsMention() + " is not an editor!").queue();
			return;
		}
		
		p.removeEditor(removedEditor);
		event.reply("Removed " + removedEditor.getAsMention() + " from being an editor").queue();

	}
	
	// playlist/editors/list
	protected void listEditors(Server s, SlashCommandInteractionEvent event) throws PlaylistAccessException
	{
		Playlist p = getPlaylist(event.getOption("playlist").getAsString());
		
		MenuHandler.createMenu(EditorListMenu.class, event, p.name);
	}
	
	// playlist/editors/clear
	protected void clearEditors(Server s, SlashCommandInteractionEvent event) throws PlaylistAccessException, JsonGenerationException, JsonMappingException, IOException
	{
		Playlist p = getPlaylist(event.getOption("playlist").getAsString());
		
		assertIsOwner(p, event.getMember());
		
		p.clearEditors();
		
		event.reply("Cleared editors for playlist `" + p.displayName + "`").queue();
	}
	
	// A subcommand of the playlist command
	// The subcommands can throw IOException and PlaylistAccessException to push error handling code out of the subcommand, making it easier to standardize responses and make the subcommand code smaller
	private static interface Subcommand 
	{
		public void executeCommand(Server s, SlashCommandInteractionEvent event) throws IOException, PlaylistAccessException;
	}
	
}
