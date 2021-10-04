package DJLuigi.Commands.Playlist;

import java.io.IOException;
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
	command = "createplaylist", 
	description = "Creates a new playlist. (Note: playlist names should not have spaces)",
	djOnly = true,
	category = CommandCategory.Playlist
)
public class CreatePlaylistCommand implements Command
{

	@Override
	public void executeCommand(Server S, ArrayList<String> Parameters, MessageReceivedEvent event)
	{
		switch(Parameters.size())
		{
		case 0:
			S.SendMessage("You have to specify a playlist name!");
			break;
		case 1:
			try {
				
				if (PlaylistManager.hasPlaylist(Parameters.get(0)))
				{
					S.SendMessage("There is already a playlist with that name! (Right now deleted playlists count)");
					return;
				}
				
				Playlist created = new Playlist(Parameters.get(0), event.getAuthor().getId(), S.guildID);
				PlaylistManager.addPlaylist(created);
				S.SendMessage("Created playlist: " + created.name);
			} catch (IOException e) {
				S.SendMessage("Sorry, but there was an error!");
				e.printStackTrace();
			}
			break;
		default:
			S.SendMessage("There are too many parameters! Please don't use spaces!");
			break;
		}
		
		
		
	}

}
