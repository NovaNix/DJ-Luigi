package DJLuigi.Commands.Playlist;

import java.util.ArrayList;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Interaction.MenuHandler;
import DJLuigi.Interaction.Menus.PlaylistListMenu;
import DJLuigi.Playlist.Playlist;
import DJLuigi.Playlist.PlaylistManager;
import DJLuigi.Server.Server;
import DJLuigi.utils.DiscordUtils;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@CommandData
(
	command = "listplaylists", 
	description = "Lists all available playlists",
	aliases = {"playlists", "plist"},
	category = CommandCategory.Playlist
)
public class ListPlaylistsCommand extends Command 
{

	@Override
	public void executeCommand(Server S, SlashCommandInteractionEvent event) 
	{
		MenuHandler.createMenu(PlaylistListMenu.MENU_PLAYLIST_LIST, event);
		
//		event.deferReply().queue(); // Tell the user that we know that they sent the message 
//		
//		ArrayList<Playlist> playlists = PlaylistManager.getPlaylists(S);
//		
//		StringBuilder list = new StringBuilder();
//		
//		list.append(playlists.size() + " playlist(s):");
//		list.append("```");
//		
//		for (int i = 0; i < playlists.size(); i++)
//		{
//			Playlist playlist = playlists.get(i);
//			list.append(playlist.name + ", " + DiscordUtils.getUsersName(playlist.creatorID) + " (" + playlist.size() + " songs)");
//			list.append("\n");
//		}
//		
//		list.append("```");
//		
//		event.getHook().sendMessage(list.toString());
		
	}

}
