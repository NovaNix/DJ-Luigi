package DJLuigi.Commands.Playlist;

import java.util.ArrayList;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Interaction.List.ReactionList;
import DJLuigi.Playlist.Playlist;
import DJLuigi.Playlist.PlaylistManager;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "listsongs", 
	description = "Lists the songs in the playlist",
	aliases = {"songs"},
	category = CommandCategory.Playlist
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
			return;
		}
		
		Playlist p = PlaylistManager.getPlaylist(Parameters.get(0));
		
		if (p.size() > 0)
		{
			S.SendMessage("Playlist Size: " + p.size() + " Songs");
			new ReactionList(p, 0, event);
		}
		
		else
		{
			S.SendMessage("There are no songs in the playlist!");
		}
		
	}

}
