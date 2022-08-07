package DJLuigi.Commands.Playlist;

import java.io.IOException;
import java.util.ArrayList;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import DJLuigi.DJ;
import DJLuigi.Audio.Song;
import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Playlist.Playlist;
import DJLuigi.Playlist.PlaylistManager;
import DJLuigi.Playlist.Loading.PlaylistLoadTrackHandler;
import DJLuigi.Server.Server;
import DJLuigi.utils.commandUtils;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "paddsong", 
	description = "Adds a song to a playlist. If no song is specified, it adds the currently playing song.",
	aliases = {"addsong", "padd"},
	category = CommandCategory.Playlist
)
public class AddSongCommand implements Command
{

	@Override
	public void executeCommand(Server S, ArrayList<String> Parameters, MessageReceivedEvent event) 
	{
		if (Parameters.size() == 0)
		{
			S.SendMessage("You need to specify the playlist!");
			return;
		}
		
		else if (Parameters.size() > 2)
		{
			S.SendMessage("Thats too many parameters!");
			return;
		}
		
		if (!PlaylistManager.hasPlaylist(Parameters.get(0)))
		{
			S.SendMessage("Unknown playlist: \"" + Parameters.get(0) + "\"");
			return;
		}
		
		Playlist p = PlaylistManager.getPlaylist(Parameters.get(0));
		
		if (!p.memberCanEdit(event.getMember()))
		{
			S.SendMessage("You don't have permission to edit this playlist!");
			return;
		}

		if (Parameters.size() == 1) // Add current song
		{	
			if (S.queue.size() == 0)
			{
				S.SendMessage("No song is currently playing!");
				return;
			}
			
			AudioTrack currentSong = S.queue.getTrack(0);
			
			Song song = new Song(currentSong);
			
			try {
				p.addSong(song);
				S.SendMessage("Added song: `" + song.name + "` to playlist " + p.name);
			} catch (IOException e) {
				S.SendMessage("Something went wrong!");
				e.printStackTrace();
				return;
			}

		}
		
		else // Add specified song
		{	
			if (!commandUtils.isValidURL(Parameters.get(1)))
			{
				S.SendMessage("Invalid song link: " + Parameters.get(1));
				return;
			}
			
			DJ.playerManager.loadItem(Parameters.get(1), new PlaylistLoadTrackHandler(S, p));
		}
		
	}

}
