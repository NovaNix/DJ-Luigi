package DJLuigi.Commands.Debugging;

import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Playlist.Playlist;
import DJLuigi.Playlist.PlaylistManager;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "playlistasjson", 
	description = "(Debug) Outputs a playlist as a json file",
	aliases = {"jsonplaylist", "playlistjson"},
	debug = true,
	category = CommandCategory.Other
)
public class PlaylistAsJSONCommand extends Command
{

	@Override
	public void executeCommand(Server S, SlashCommandInteractionEvent event)
	{
		
		if (Parameters.size() == 0)
		{
			S.SendMessage("You need to specify the playlist!");
			return;
		}
		
		if (!PlaylistManager.hasPlaylist(Parameters.get(0)))
		{
			S.SendMessage("Unknown playlist: \"" + Parameters.get(0) + "\"");
			return;
		}
		
		Playlist p = PlaylistManager.getPlaylist(Parameters.get(0));
		
		try
		{
			S.SendMessage(p.toJSON());
		} catch (JsonProcessingException e)
		{
			S.SendMessage("Failed to output playlist " + p.name + " as JSON. Check console for details...");
			e.printStackTrace();
		}
		
	}
	
}
