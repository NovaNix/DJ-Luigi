package DJLuigi.Commands.Audio;

import java.util.ArrayList;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "shuffle", 
	description = "Shuffles the queue",
	aliases = {"shuff"},
	djOnly = true,
	category = CommandCategory.Audio
)
public class ShuffleCommand implements Command
{

	@Override
	public void executeCommand(Server S, ArrayList<String> Parameters, MessageReceivedEvent event)
	{
		S.trackScheduler.shuffle();
		S.SendMessage("The queue has been shuffled!");
	}

}
