package DJLuigi.Commands.Meta;

import java.util.ArrayList;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "disconnect", 
	description = "Removed the bot from VC.",
	aliases = {"dc", "leave"},
	category = CommandCategory.Control
)
public class DisconnectCommand implements Command
{

	@Override
	public void executeCommand(Server S, ArrayList<String> Parameters, MessageReceivedEvent event) 
	{
		S.LeaveVC();
		S.SendMessage("Disconnected!");
	}

}
