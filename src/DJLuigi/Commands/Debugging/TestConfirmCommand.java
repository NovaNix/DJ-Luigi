package DJLuigi.Commands.Debugging;

import java.util.ArrayList;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Interaction.ReactionConfirmation;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "testconfirm", 
	description = "(Debug) Creates a confirmation menu for testing",
	debug = true,
	category = CommandCategory.Other
)
public class TestConfirmCommand implements Command
{

	@Override
	public void executeCommand(Server S, ArrayList<String> Parameters, MessageReceivedEvent event) 
	{
		
		new ReactionConfirmation("This is a test. Accept?", event, 
				() -> S.SendMessage("Approved"),
				() -> S.SendMessage("Denied"));
		
	}

}
