package DJLuigi.Commands.Audio;

import java.util.ArrayList;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "loop", 
	description = "Toggles if the queue should be looped",
	aliases = {"l"},
	djOnly = true,
	category = CommandCategory.Audio
)
public class LoopCommand implements Command
{

	@Override
	public void executeCommand(Server S, ArrayList<String> Parameters, MessageReceivedEvent event) 
	{
		S.trackScheduler.Looped = !S.trackScheduler.Looped;
		
		S.SendMessage("Set looped status to : `" + S.trackScheduler.Looped + "`!");
		
	}

}
