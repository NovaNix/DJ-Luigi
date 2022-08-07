package DJLuigi.Commands.Meta;

import java.util.ArrayList;
import java.util.Comparator;

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
		
		ArrayList<Command> commands = new ArrayList<Command>();
		
		for (Command c : CommandHandler.commands.values())
		{
			commands.add(c);
		}
		
		Comparator<Command> sortCommands = (Command c1, Command c2) -> {
			return c1.getCategory().order - c2.getCategory().order;
		};
		
		commands.sort(sortCommands);
		
		String currentCategory = "";
		
		for (Command c : commands)
		{
			if (currentCategory != c.getCategory().name())
			{
				currentCategory = c.getCategory().name();
				commandList.append("\n\t" + currentCategory + "\n");
			}
			
			commandList.append(c.getCommandMessage() + ": " + c.getDescription());
			commandList.append("\n");
		}
		
		commandList.append("```");
		
		S.SendMessage(commandList.toString());
	}

}
