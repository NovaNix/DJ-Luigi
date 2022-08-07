package DJLuigi.Commands.Debugging;

import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonProcessingException;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "queueasjson", 
	description = "(Debug) Outputs the current queue as a json file",
	aliases = {"jsonqueue", "queuejson"},
	debug = true,
	category = CommandCategory.Other
)
public class QueueAsJSONCommand implements Command
{

	@Override
	public void executeCommand(Server S, ArrayList<String> Parameters, MessageReceivedEvent event)
	{
		try
		{
			S.SendMessage(S.queue.toJSON());
		} catch (JsonProcessingException e)
		{
			S.SendMessage("Failed to output queue as JSON. Check console for details...");
			e.printStackTrace();
		}
		
	}
	
}
