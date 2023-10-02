package io.github.novanix.djluigi.commands.audio;

import io.github.novanix.djluigi.commands.Command;
import io.github.novanix.djluigi.commands.CommandCategory;
import io.github.novanix.djluigi.commands.CommandData;
import io.github.novanix.djluigi.interaction.MenuHandler;
import io.github.novanix.djluigi.interaction.menus.paged.QueueMenu;
import io.github.novanix.djluigi.server.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@CommandData
(
	command = "queue", 
	description = "Lists the songs in the queue",
	aliases = {"list", "q"},
	category = CommandCategory.Audio
)
public class QueueCommand extends Command
{

	@Override
	public void executeCommand(Server s, SlashCommandInteractionEvent event) 
	{
		
		if (s.queue.size() > 0)
		{
			MenuHandler.createMenu(QueueMenu.class, event);
		}
		
		else
		{
			event.reply("There are no songs in the queue!").queue();
		}
		
	}

}
