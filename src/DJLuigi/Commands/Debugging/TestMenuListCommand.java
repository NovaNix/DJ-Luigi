package DJLuigi.Commands.Debugging;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Interaction.MenuHandler;
import DJLuigi.Interaction.Menus.TestListMenu;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@CommandData
(
	command = "testlist", 
	description = "(Debug) Creates a list menu for testing",
	debug = true,
	category = CommandCategory.Other
)
public class TestMenuListCommand extends Command
{

	@Override
	public void executeCommand(Server S, SlashCommandInteractionEvent event) 
	{
		MenuHandler.createMenu(TestListMenu.class, event);
	}

}
