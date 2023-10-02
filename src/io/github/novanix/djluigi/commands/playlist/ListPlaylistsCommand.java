package io.github.novanix.djluigi.commands.playlist;

import io.github.novanix.djluigi.commands.Command;
import io.github.novanix.djluigi.commands.CommandCategory;
import io.github.novanix.djluigi.commands.CommandData;
import io.github.novanix.djluigi.interaction.MenuHandler;
import io.github.novanix.djluigi.interaction.menus.paged.PlaylistListMenu;
import io.github.novanix.djluigi.server.Server;
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
