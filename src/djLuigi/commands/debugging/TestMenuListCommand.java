package djLuigi.commands.debugging;

import djLuigi.interaction.MenuHandler;
import djLuigi.interaction.menus.paged.TestListMenu;
import djLuigi.server.Server;
import djLuigi.commands.Command;
import djLuigi.commands.CommandCategory;
import djLuigi.commands.CommandData;
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
	public void executeCommand(Server s, SlashCommandInteractionEvent event) 
	{
		MenuHandler.createMenu(TestListMenu.class, event);
	}

}
