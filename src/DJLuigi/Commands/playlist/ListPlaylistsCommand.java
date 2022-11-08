package djLuigi.commands.playlist;

import djLuigi.interaction.MenuHandler;
import djLuigi.interaction.menus.paged.PlaylistListMenu;
import djLuigi.server.Server;
import djLuigi.commands.Command;
import djLuigi.commands.CommandCategory;
import djLuigi.commands.CommandData;
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
	public void executeCommand(Server s, SlashCommandInteractionEvent event) 
	{
		MenuHandler.createMenu(PlaylistListMenu.class, event);	
	}

}
