package DJLuigi.Commands.Debugging;

import java.util.ArrayList;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

// Used to test that the parameter gathering command is functional
@CommandData
(
	command = "listparameters", 
	description = "(Debug) Lists parameters used in the command",
	debug = true,
	category = CommandCategory.Other
)
public class SendParametersCommand implements Command
{

	@Override
	public void executeCommand(Server S, ArrayList<String> Parameters, MessageReceivedEvent event) 
	{
		S.SendMessage("***Listing " + Parameters.size() + " parameters...***");
		
		for (int i = 0; i < Parameters.size(); i++)
		{
			S.SendMessage(Parameters.get(i));
		}
		
	}
	
}
