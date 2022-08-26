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
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "deleteplaylist", 
	description = "Deletes the playlist (WARNING: THERES NO GOING BACK!)",
	category = CommandCategory.Playlist
)
public class DeletePlaylistCommand extends Command
{

	@Override
	public void executeCommand(Server S, SlashCommandInteractionEvent event) 
	{
		event.reply("Sorry, this command is broken right now. Come back later").queue();
		
		
//		switch(Parameters.size())
//		{
//		case 0:
//			S.SendMessage("You have to specify a playlist name!");
//			break;
//		case 1:
//				
//			if (PlaylistManager.hasPlaylist(Parameters.get(0))) 
//			{
//				
//				Playlist p = PlaylistManager.getPlaylist(Parameters.get(0));
//				
//				if (!p.memberCanEdit(event.getMember()))
//				{
//					S.SendMessage("You don't have permission to edit this playlist!");
//					return;
//				}
//				
//				new ReactionConfirmation("Do you really want to delete playlist " + Parameters.get(0) + "? There's no going back...", event,
//						() -> deletePlaylist(S, Parameters.get(0)),
//						() -> S.SendMessage("Aborded Deletion."));
//			}
//
//			else 
//			{
//				S.SendMessage("Cannot find playlist \"" + Parameters.get(0) + "\"");
//			}
//				
//			break;
//		default:
//			S.SendMessage("There are too many parameters! Please don't use spaces!");
//			break;
//		}
		
	}
	
	private void deletePlaylist(Server S, String playlist)
	{
		if (PlaylistManager.deletePlaylist(playlist)) 
		{
			S.SendMessage("Successfully deleted the playlist!");
		}

		else 
		{
			S.SendMessage("Something went wrong...");
		}
	}

}
