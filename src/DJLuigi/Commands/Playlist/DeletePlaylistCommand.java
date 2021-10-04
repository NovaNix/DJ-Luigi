package DJLuigi.Commands.Playlist;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.IO.BotSetting;
import DJLuigi.Playlist.Playlist;
import DJLuigi.Playlist.PlaylistManager;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "deleteplaylist", 
	description = "Deletes the playlist (WARNING: THERES NO GOING BACK!)",
	djOnly = true,
	category = CommandCategory.Playlist
)
public class DeletePlaylistCommand implements Command
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
				
			if (PlaylistManager.hasPlaylist(Parameters.get(0))) 
			{
				if (PlaylistManager.deletePlaylist(Parameters.get(0))) 
				{
					S.SendMessage("Successfully deleted the playlist!");
				}

				else 
				{
					S.SendMessage("Something went wrong...");
				}
			}

			else 
			{
				S.SendMessage("Cannot find playlist \"" + Parameters.get(0) + "\"");
			}
				
			break;
		default:
			S.SendMessage("There are too many parameters! Please don't use spaces!");
			break;
		}
		
	}

}
