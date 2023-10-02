package io.github.novanix.djluigi.interaction.menus.paged;

import java.util.Comparator;
import java.util.List;

import io.github.novanix.djluigi.DJ;
import io.github.novanix.djluigi.commands.Command;
import io.github.novanix.djluigi.commands.CommandCategory;
import io.github.novanix.djluigi.commands.CommandHandler;
import io.github.novanix.djluigi.interaction.MenuContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class HelpMenu extends PagedMenu
{
	
	// A comparator that handles sorting commands
	private static Comparator<Command> sortCommands = (Command c1, Command c2) -> {
		// Compare the primary order of the command type
		int order = c1.getCategory().order - c2.getCategory().order;

		// If the commands are of the same category, sort them by their sort order
		if (order == 0)
		{
			return c1.getSortOrder() - c2.getSortOrder();
		}

		else
		{
			return order;
		}
	};
	
	@Override
	protected MessageEmbed getPage(int page, MenuContext context)
	{
		CommandCategory category = CommandCategory.values()[page];
		
		List<Command> commands = CommandHandler.getCommands(category);
		commands.sort(sortCommands);
		
		EmbedBuilder embedBuilder = new EmbedBuilder()
				.setTitle(category.name() + " Commands")
				.setColor(DJ.getPrimaryColor())
				.setFooter(String.format("Page %d of %d", (page + 1), getPageCount(context)), null);
		
		for (Command command : commands)
		{
			embedBuilder.addField(command.getCommandMessage(), command.getDescription(), false);
		}
		
		return embedBuilder.build();
	}

	@Override
	protected int getPageCount(MenuContext context)
	{
		return CommandCategory.values().length;
	}

}
