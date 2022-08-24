package DJLuigi.Commands.Playlist;

import java.util.ArrayList;

import DJLuigi.DJ;
import DJLuigi.Audio.Song;
import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Playlist.Playlist;
import DJLuigi.Playlist.PlaylistManager;
import DJLuigi.Playlist.Loading.PlaylistLoadHandler;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "playplaylist", 
	description = "Adds a playlist to the queue",
	aliases = {"pp", "pplaylist"},
	category = CommandCategory.Playlist
)
public class PlayPlaylistCommand extends Command
{

	@Override
	public void executeCommand(Server S, SlashCommandInteractionEvent event) 
	{
		Member self = event.getGuild().getMember(DJ.jda.getSelfUser());
		
		AudioChannel currentChannel = self.getVoiceState().getChannel();
		AudioChannel userChannel = event.getMember().getVoiceState().getChannel();
		
		if (Parameters.size() == 0)
		{
			S.SendMessage("You have to specify a song!");
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
				S.SendMessage("You must be in a voice channel for me to join!");
				return;
			}
		}
		
		else if (!currentChannel.equals(userChannel))
		{
			S.JoinChannel(userChannel);
			currentChannel = userChannel;
		}
		
		if (!PlaylistManager.hasPlaylist(Parameters.get(0)))
		{
			S.SendMessage("Unknown playlist: \"" + Parameters.get(0) + "\"");
			return;
		}
		
		Playlist p = PlaylistManager.getPlaylist(Parameters.get(0));
		
		if (!p.memberCanEdit(event.getMember()))
		{
			S.SendMessage("You can't access this playlist!");
			return;
		}
		
		ArrayList<Song> songs = p.songs;
		
		for (int i = 0; i < songs.size(); i++)
		{
			DJ.playerManager.loadItem(songs.get(i).uri, new PlaylistLoadHandler(S));
		}
		
		S.SendMessage("Loaded " + songs.size() + " songs from playlist " + p.name);
		
	}

}
