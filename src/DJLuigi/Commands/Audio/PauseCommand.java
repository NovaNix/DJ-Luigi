package DJLuigi.Commands.Audio;

import java.util.ArrayList;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "pause", 
	description = "Pauses playback",
	aliases = {"stop"},
	category = CommandCategory.Audio
)
public class PauseCommand implements Command
{

	@Override
	public void executeCommand(Server S, ArrayList<String> Parameters, MessageReceivedEvent event) 
	{
		S.player.setPaused(true);
	}
	
}
