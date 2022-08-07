package DJLuigi.Commands.Audio;

import java.util.ArrayList;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "fs", 
	description = "Skips the song without a vote.",
	aliases = {"forceskip"},
	djOnly = true,
	category = CommandCategory.Audio
)
public class ForceSkipCommand implements Command
{

	@Override
	public void executeCommand(Server S, ArrayList<String> Parameters, MessageReceivedEvent event) 
	{
		S.queue.skip();
		
		S.SendMessage("Song Skipped!");
	}
	
}
