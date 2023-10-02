package io.github.novanix.djluigi.commands.debugging;

import io.github.novanix.djluigi.commands.Command;
import io.github.novanix.djluigi.commands.CommandCategory;
import io.github.novanix.djluigi.commands.CommandData;
import io.github.novanix.djluigi.commands.Parameter;
import io.github.novanix.djluigi.playlist.Playlist;
import io.github.novanix.djluigi.playlist.PlaylistEditPermissions;
import io.github.novanix.djluigi.playlist.PlaylistManager;
import io.github.novanix.djluigi.server.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

@CommandData
(
	command = "getpermissions", 
	description = "(Debug) Outputs the current user permissions for a playlist",
	parameters = {
			@Parameter(name = "playlist", description = "The playlist that should be tested", type = OptionType.STRING, required = true)
	},
	debug = true,
	category = CommandCategory.Other
)
public class PermissionsTestCommand extends Command
{

	@Override
	public void executeCommand(Server s, SlashCommandInteractionEvent event)
	{
		String playlistName = event.getOption("playlist").getAsString();
		
		if (!PlaylistManager.hasPlaylist(playlistName))
		{
			event.reply("Unknown playlist: \"" + playlistName + "\"").queue();
			return;
		}
		
		Playlist p = PlaylistManager.getPlaylist(playlistName);
		
		int userBitmask = PlaylistEditPermissions.getUserPermissions(event.getMember(), p);
		
		event.reply("Your permissions for playlist `" + p.getUniqueName() + "` is `" + Integer.toBinaryString(userBitmask) + "`").queue();
		
	}

}
