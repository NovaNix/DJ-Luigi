package DJLuigi.Commands.Playlist;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@CommandData
(
	command = "playlist", 
	description = "A general purpose command for using playlists",
	parameters = {
		
	},
	category = CommandCategory.Playlist
)
public class EditPlaylistCommand extends Command
{

	@Override
	public void executeCommand(Server S, SlashCommandInteractionEvent event)
	{
		// TODO Auto-generated method stub
		
		
		
	}

}
