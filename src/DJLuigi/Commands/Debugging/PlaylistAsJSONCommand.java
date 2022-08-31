package DJLuigi.Commands.Debugging;

import com.fasterxml.jackson.core.JsonProcessingException;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Commands.Parameter;
import DJLuigi.Playlist.Playlist;
import DJLuigi.Playlist.PlaylistManager;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

@CommandData
(
	command = "playlistasjson", 
	description = "(Debug) Outputs a playlist as a json file",
	aliases = {"jsonplaylist", "playlistjson"},
	parameters = {
			@Parameter(name = "playlist", description = "The playlist that should be outputted", type = OptionType.STRING, required = true)
	},
	debug = true,
	category = CommandCategory.Other
)
public class PlaylistAsJSONCommand extends Command
{

	@Override
	public void executeCommand(Server S, SlashCommandInteractionEvent event)
	{
		
		String playlistName = event.getOption("playlist").getAsString();
		
		if (!PlaylistManager.hasPlaylist(playlistName))
		{
			event.reply("Unknown playlist: \"" + playlistName + "\"").queue();
			return;
		}
		
		Playlist p = PlaylistManager.getPlaylist(playlistName);
		
		try
		{
			event.reply("```json\n" + p.toJSON() + "```").queue();
		} catch (JsonProcessingException e)
		{
			event.reply("Failed to output playlist " + p.name + " as JSON. Check console for details...").queue();
			e.printStackTrace();
		}
		
	}
	
}
