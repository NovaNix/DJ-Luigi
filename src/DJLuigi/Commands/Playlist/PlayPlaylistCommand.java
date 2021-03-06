package DJLuigi.Commands.Playlist;

import java.util.ArrayList;

import DJLuigi.DJ;
import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Playlist.Playlist;
import DJLuigi.Playlist.PlaylistEntry;
import DJLuigi.Playlist.PlaylistManager;
import DJLuigi.Playlist.Loading.PlaylistLoadHandler;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "playplaylist", 
	description = "Adds a playlist to the queue",
	aliases = {"pp", "pplaylist"},
	category = CommandCategory.Playlist
)
public class PlayPlaylistCommand implements Command
{

	@Override
	public void executeCommand(Server S, ArrayList<String> Parameters, MessageReceivedEvent event) 
	{
		
		Member self = event.getGuild().getMember(DJ.jda.getSelfUser());
		
		if (!self.getVoiceState().inVoiceChannel())
		{
			S.JoinChannel(event.getMember().getVoiceState().getChannel());
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
		
		ArrayList<PlaylistEntry> songs = p.songs;
		
		for (int i = 0; i < songs.size(); i++)
		{
			DJ.playerManager.loadItem(songs.get(i).uri, new PlaylistLoadHandler(S));
		}
		
		S.SendMessage("Loaded " + songs.size() + " songs from playlist " + p.name);
		
	}

}
