package io.github.novanix.djluigi.commands.debugging;

import io.github.novanix.djluigi.commands.Command;
import io.github.novanix.djluigi.commands.CommandCategory;
import io.github.novanix.djluigi.commands.CommandData;
import io.github.novanix.djluigi.interaction.MenuHandler;
import io.github.novanix.djluigi.interaction.menus.paged.TestListMenu;
import io.github.novanix.djluigi.server.Server;
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
