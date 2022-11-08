package DJLuigi.Commands.Debugging;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Commands.Parameter;
import DJLuigi.Playlist.Playlist;
import DJLuigi.Playlist.PlaylistEditPermissions;
import DJLuigi.Playlist.PlaylistManager;
import DJLuigi.Server.Server;
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
