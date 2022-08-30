package DJLuigi.Commands.Playlist;

import java.util.ArrayList;

import DJLuigi.DJ;
import DJLuigi.Audio.Song;
import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Commands.Parameter;
import DJLuigi.Playlist.Playlist;
import DJLuigi.Playlist.PlaylistManager;
import DJLuigi.Playlist.Loading.PlaylistLoadHandler;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

@CommandData
(
	command = "playplaylist", 
	description = "Adds a playlist to the queue",
	parameters = {
		@Parameter(name = "playlist", description = "The playlist that should be played", type = OptionType.STRING, required = true)
	},
	aliases = {"pp", "pplaylist"},
	category = CommandCategory.Playlist
)
public class PlayPlaylistCommand extends Command
{

	@Override
	public void executeCommand(Server S, SlashCommandInteractionEvent event) 
	{
		// TODO consider add dropdown if multiple playlists share the same name
		
		Member self = event.getGuild().getMember(DJ.jda.getSelfUser());
		
		AudioChannel currentChannel = self.getVoiceState().getChannel();
		AudioChannel userChannel = event.getMember().getVoiceState().getChannel();
		
		String playlistName = event.getOption("playlist").getAsString();
		
		if (!PlaylistManager.hasPlaylist(playlistName))
		{
			event.reply("Unknown playlist: \"" + playlistName + "\"").queue();
			return;
		}
		
		if (currentChannel == null)
		{
			if (userChannel != null)
			{
				S.JoinChannel(userChannel);
				currentChannel = userChannel;
			}
			
			else
			{
				event.reply("You must be in a voice channel for me to join!").queue();
				return;
			}
		}
		
		else if (!currentChannel.equals(userChannel))
		{
			S.JoinChannel(userChannel);
			currentChannel = userChannel;
		}
		
		Playlist p = PlaylistManager.getPlaylist(playlistName);
		
		ArrayList<Song> songs = p.songs;
		
		for (int i = 0; i < songs.size(); i++)
		{
			DJ.playerManager.loadItem(songs.get(i).uri, new PlaylistLoadHandler(S));
		}
		
		event.reply("Loaded `" + songs.size() + "` songs from playlist `" + p.displayName + "`").queue();
		
	}

}
