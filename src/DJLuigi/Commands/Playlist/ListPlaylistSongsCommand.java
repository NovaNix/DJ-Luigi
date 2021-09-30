package DJLuigi.Commands.Playlist;

import java.util.ArrayList;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandData;
import DJLuigi.Playlist.Playlist;
import DJLuigi.Playlist.PlaylistManager;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "listsongs", 
	description = "Lists the songs in the playlist",
	aliases = {"songs"}
)
public class ListPlaylistSongsCommand implements Command
{

	@Override
	public void executeCommand(Server S, ArrayList<String> Parameters, MessageReceivedEvent event) 
	{
		if (Parameters.size() == 0)
		{
			S.SendMessage("You need to specify the playlist!");
			return;
		}
		
		if (!PlaylistManager.hasPlaylist(Parameters.get(0)))
		{
			S.SendMessage("Unknown playlist: \"" + Parameters.get(0) + "\"");
		}
		
		Playlist p = PlaylistManager.getPlaylist(Parameters.get(0));
		
		StringBuilder list = new StringBuilder();
		
		list.append(p.songs.size() + " songs(s):");
		list.append("```");
		
		for (int i = 0; i < p.songs.size(); i++)
		{
			list.append(p.songs.get(i).name);
			list.append("\n");
		}
		
		list.append("```");
		
		S.SendMessage(list.toString());
		
	}

}
