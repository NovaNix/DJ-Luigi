package DJLuigi.Commands.Playlist;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Interaction.MenuHandler;
import DJLuigi.Interaction.Menus.PlaylistListMenu;
import DJLuigi.Server.Server;
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
		MenuHandler.createMenu(PlaylistListMenu.class, event);	
	}

}
