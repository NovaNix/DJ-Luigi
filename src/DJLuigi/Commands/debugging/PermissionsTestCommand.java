package djLuigi.commands.debugging;

import djLuigi.server.Server;
import djLuigi.commands.Command;
import djLuigi.commands.CommandCategory;
import djLuigi.commands.CommandData;
import djLuigi.commands.Parameter;
import djLuigi.playlist.Playlist;
import djLuigi.playlist.PlaylistEditPermissions;
import djLuigi.playlist.PlaylistManager;
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
