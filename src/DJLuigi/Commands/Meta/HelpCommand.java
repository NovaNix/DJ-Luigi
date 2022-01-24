package DJLuigi.Commands.Meta;

import java.util.ArrayList;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Commands.CommandHandler;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "help", 
	description = "Lists the available commands",
	aliases = {"?", "h"},
	category = CommandCategory.Other
)
public class HelpCommand implements Command
{

	@Override
	public void executeCommand(Server S, ArrayList<String> Parameters, MessageReceivedEvent event) 
	{
		S.SendMessage("Commands:");
		
		StringBuilder commandList = new StringBuilder();
		
		commandList.append("```YML\n");
		
		for (Command c : CommandHandler.commands.values())
		{
			commandList.append(c.getCommandMessage() + ": " + c.getDescription());
			commandList.append("\n");
		}
		
		commandList.append("```");
		
		S.SendMessage(commandList.toString());
	}

}
