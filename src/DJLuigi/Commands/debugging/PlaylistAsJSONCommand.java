package djLuigi.commands.debugging;

import com.fasterxml.jackson.core.JsonProcessingException;

import djLuigi.server.Server;
import djLuigi.commands.Command;
import djLuigi.commands.CommandCategory;
import djLuigi.commands.CommandData;
import djLuigi.commands.Parameter;
import djLuigi.playlist.Playlist;
import djLuigi.playlist.PlaylistManager;
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
	public void executeCommand(Server s, SlashCommandInteractionEvent event)
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
