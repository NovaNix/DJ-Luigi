package DJLuigi.Commands.Debugging;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Interaction.List.ReactionListable;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@CommandData
(
	command = "testlist", 
	description = "(Debug) Creates a list menu for testing",
	debug = true,
	category = CommandCategory.Other
)
public class TestReactionListCommand extends Command implements ReactionListable
{

	@Override
	public void executeCommand(Server S, SlashCommandInteractionEvent event) 
	{
		//new ReactionList(this, 0, event);
	}

	@Override
	public String getValue(int index) 
	{
		return "Value " + index;
	}

	@Override
	public int size() 
	{
		return 26;
	}

	@Override
	public int itemsPerPage() 
	{	
		return 10;
	}

	@Override
	public String getName() 
	{
		return "Test List";
	}

	
}
