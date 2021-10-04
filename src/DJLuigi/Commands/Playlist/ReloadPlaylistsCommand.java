package DJLuigi.Commands.Playlist;

import java.util.ArrayList;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Playlist.PlaylistManager;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "reload", 
	description = "Reloads all of the playlists",
	djOnly = true,
	category = CommandCategory.Playlist
)
public class ReloadPlaylistsCommand implements Command
{

	@Override
	public void executeCommand(Server S, ArrayList<String> Parameters, MessageReceivedEvent event) 
	{
		S.SendMessage("Reloading...");
		
		PlaylistManager.reloadPlaylists();
		
		S.SendMessage("Reloaded " + PlaylistManager.playlists.size() + " playlist(s)");
	}

}
