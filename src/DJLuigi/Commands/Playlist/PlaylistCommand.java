package DJLuigi.Commands.Playlist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import DJLuigi.DJ;
import DJLuigi.Audio.Song;
import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Commands.Subcommand;
import DJLuigi.Interaction.MenuHandler;
import DJLuigi.Interaction.Menus.PlaylistSongsMenu;
import DJLuigi.Playlist.Playlist;
import DJLuigi.Playlist.PlaylistManager;
import DJLuigi.Playlist.Loading.PlaylistLoadTrackHandler;
import DJLuigi.Server.Server;
import DJLuigi.utils.commandUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command.SubcommandGroup;
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
		
		editorsCommand.addSubcommands(addEditor, removeEditor, listEditors);
		
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
											new OptionData(OptionType.STRING, "song", "The song to add. If not specified, the current song is added"));
		
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
	}
	
	protected void addSubcommand(Subcommand command, String name, String description, OptionData... options)
	{
		SubcommandData subcommand = new SubcommandData(name, description);
		subcommand.addOptions(options);
		
		subcommands.put(name, command);
		subcommandData.put(name, subcommand);
	}
	
	@Override
	public void executeCommand(Server S, SlashCommandInteractionEvent event)
	{
		String path = event.getCommandPath().replaceFirst(getCommandMessage() + "/", "");
		
		subcommands.get(path).executeCommand(S, event);
	}
	
	// playlist/create [name] [description]
	// Creates a new playlist. If name is not defined then it opens up a modal to create it 
	protected void createPlaylist(Server S, SlashCommandInteractionEvent event)
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
			
			try
			{
				Playlist created = new Playlist(name, description, event.getUser().getId(), S.guildID);
				PlaylistManager.addPlaylist(created);
				event.reply("Created playlist `" + created.getUniqueName() + "`").queue();
			} catch (IOException e)
			{
				event.reply("Something went wrong while saving the playlist! Please contact a developer!").queue();
				e.printStackTrace();
			}

		}
	}

	// playlist/delete <playlist>
	// Deletes the specified playlist
	// TODO add confirmation
	protected void deletePlaylist(Server S, SlashCommandInteractionEvent event)
	{
		String playlistName = event.getOption("playlist").getAsString();
		
		if (PlaylistManager.hasPlaylist(playlistName)) 
		{
			Playlist p = PlaylistManager.getPlaylist(playlistName);

			if (!p.memberCanEdit(event.getMember()))
			{
				event.reply("You don't have permission to edit this playlist!").queue();
				return;
			}

			if (PlaylistManager.deletePlaylist(playlistName)) 
			{
				event.reply("Successfully deleted the playlist!").queue();
			}

			else 
			{
				event.reply("Something went wrong...").queue();
			}
			
//			new ReactionConfirmation("Do you really want to delete playlist " + Parameters.get(0) + "? There's no going back...", event,
//												() -> deletePlaylist(S, Parameters.get(0)),
//												() -> S.SendMessage("Aborded Deletion."));
		}

		else 
		{
			event.reply("Cannot find playlist \"" + playlistName + "\"").queue();
		}
	}
	
	// playlist/add <playlist> <song>
	// Adds a song to the specified playlist
	protected void addSong(Server S, SlashCommandInteractionEvent event)
	{
		String playlistName = event.getOption("playlist").getAsString();
		
		if (!PlaylistManager.hasPlaylist(playlistName))
		{
			event.reply("Unknown playlist: \"" + playlistName + "\"").queue();
			return;
		}

		Playlist p = PlaylistManager.getPlaylist(playlistName);

		if (!p.memberCanEdit(event.getMember()))
		{
			event.reply("You don't have permission to edit this playlist!").queue();
			return;
		}

		if (event.getOption("song") == null) // Add current song
		{	
			if (S.queue.size() == 0)
			{
				event.reply("No song is currently playing!").queue();
				return;
			}

			AudioTrack currentSong = S.queue.getTrack(0);

			Song song = new Song(currentSong);

			try {
				p.addSong(song);
				event.reply("Added song: `" + song.name + "` to playlist " + p.name).queue();
			} catch (IOException e) {
				event.reply("Something went wrong while saving the playlist! Please contact a developer!").queue();
				e.printStackTrace();
				return;
			}

		}

		else // Add specified song
		{	
			String song = event.getOption("song").getAsString();
			
			if (!commandUtils.isValidURL(song))
			{
				event.reply("Invalid song link: " + song).queue();
				return;
			}

			// TODO edit the messages in PlaylistLoadTrackHandler to output to the hook
			DJ.playerManager.loadItem(song, new PlaylistLoadTrackHandler(S, p));
			event.reply("Adding song...").queue();
		}
	}
	
	// playlist/remove <playlist> <song>
	// Removes a song from the specified playlist
	// If the song is a number, it removes that index from the playlist. Otherwise it searches the playlist for a song with the name specified
	protected void removeSong(Server S, SlashCommandInteractionEvent event)
	{
		String playlistName = event.getOption("playlist").getAsString();
		int songIndex = event.getOption("song").getAsInt() - 1;
		
		if (!PlaylistManager.hasPlaylist(playlistName))
		{
			event.reply("Unknown playlist: \"" + playlistName + "\"").queue();
			return;
		}

		Playlist p = PlaylistManager.getPlaylist(playlistName);
		
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
		
		else if (songIndex >= S.queue.size())
		{
			event.reply("That index is out of bounds! (There are only " + p.size() + " songs in the playlist!)").queue();
			return;
		}
		
		try
		{
			Song removed = p.removeSong(songIndex);
			event.reply("Removed song " + removed.name + " from the playlist.").queue();
		} catch (IOException e)
		{
			event.reply("Something went wrong while saving the playlist! Please contact a developer!").queue();
			e.printStackTrace();
		}
		
	}
	
	// playlist/songs <playlist>
	// Lists the songs in the playlist
	protected void listSongs(Server S, SlashCommandInteractionEvent event)
	{
		//event.reply("Ran list songs").queue();
		String playlistName = event.getOption("playlist").getAsString();
		
		if (!PlaylistManager.hasPlaylist(playlistName))
		{
			event.reply("Unknown playlist: \"" + playlistName + "\"").queue();
			return;
		}
		
		MenuHandler.createMenu(PlaylistSongsMenu.class, event, playlistName);
	}
	
	// playlist/info
	// Tells info about a playlist
	protected void playlistInfo(Server S, SlashCommandInteractionEvent event)
	{
		String playlistName = event.getOption("playlist").getAsString();
		
		if (!PlaylistManager.hasPlaylist(playlistName))
		{
			event.reply("Unknown playlist: \"" + playlistName + "\"").queue();
			return;
		}
			
		Playlist p = PlaylistManager.getPlaylist(playlistName);
			
		User user = DJ.jda.getUserById(p.creatorID);
			
		MessageEmbed embed = new EmbedBuilder()
				.setTitle(p.displayName)
				.setColor(DJ.getPrimaryColor())
				.setFooter("Owner: " + user.getName() + ", " + p.editors.size() + " editors", user.getAvatarUrl())
				.setThumbnail("https://i.redd.it/b2pilioyu7u21.jpg")
				.addField("Songs", p.songs.size() + " Songs", false)
				.addField("Is Public", "" + p.isPublic, false)
				.addField("Edit Status", p.editPermissions + " (will be replaced with more readable value later)", false)
				.build();
			
		event.replyEmbeds(embed).queue();
	}
	
	// playlist/editors/add
	protected void addEditor(Server S, SlashCommandInteractionEvent event)
	{
		String playlistName = event.getOption("playlist").getAsString();
		User newEditor = event.getOption("editor").getAsUser();
		
		if (!PlaylistManager.hasPlaylist(playlistName))
		{
			event.reply("Unknown playlist: \"" + playlistName + "\"").queue();
			return;
		}
		
		Playlist p = PlaylistManager.getPlaylist(playlistName);
		
		if (!p.memberCanEdit(event.getMember())) 
		{
			event.reply("You do not have permission to edit this playlist!").queue();
			return;
		}
		
		if (p.isEditor(newEditor))
		{
			event.reply(newEditor.getAsMention() + " is already an editor!").queue();
			return;
		}
		
		try {
			p.addEditor(newEditor);
			event.reply("Added " + newEditor.getAsMention() + " as an editor").queue();
		} catch (IOException e)
		{
			event.reply("Something went wrong while saving the playlist! Please contact a developer!").queue();
			e.printStackTrace();
		} 
		
	}
	
	// playlist/editors/remove
	protected void removeEditor(Server S, SlashCommandInteractionEvent event)
	{
		String playlistName = event.getOption("playlist").getAsString();
		User removedEditor = event.getOption("editor").getAsUser();
		
		if (!PlaylistManager.hasPlaylist(playlistName))
		{
			event.reply("Unknown playlist: \"" + playlistName + "\"").queue();
			return;
		}
		
		Playlist p = PlaylistManager.getPlaylist(playlistName);
		
		if (!p.memberCanEdit(event.getMember())) 
		{
			event.reply("You do not have permission to edit this playlist!").queue();
			return;
		}
		
		if (!p.isEditor(removedEditor))
		{
			event.reply(removedEditor.getAsMention() + " is not an editor!").queue();
			return;
		}
		
		try {
			p.removeEditor(removedEditor);
			event.reply("Removed " + removedEditor.getAsMention() + " from being an editor").queue();
		} catch (IOException e)
		{
			event.reply("Something went wrong while saving the playlist! Please contact a developer!").queue();
			e.printStackTrace();
		} 
	}
	
	// playlist/editors/list
	protected void listEditors(Server S, SlashCommandInteractionEvent event)
	{
		event.reply("Ran list editor").queue();
	}
	
	// playlist/settings
	
}
