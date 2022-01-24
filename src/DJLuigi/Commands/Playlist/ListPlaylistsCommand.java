package DJLuigi.Commands.Playlist;

import java.util.ArrayList;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Playlist.Playlist;
import DJLuigi.Playlist.PlaylistManager;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "listplaylists", 
	description = "Reloads all of the playlists",
	aliases = {"playlists"},
	category = CommandCategory.Playlist
)
public class ListPlaylistsCommand implements Command 
{

	@Override
	public void executeCommand(Server S, ArrayList<String> Parameters, MessageReceivedEvent event) 
	{
		ArrayList<Playlist> playlists = PlaylistManager.getPlaylists(S);
		
		StringBuilder list = new StringBuilder();
		
		list.append(playlists.size() + " playlist(s):");
		list.append("```");
		
		for (int i = 0; i < playlists.size(); i++)
		{
			list.append(playlists.get(i).name);
			list.append("\n");
		}
		
		list.append("```");
		
		S.SendMessage(list.toString());
		
	}

}
