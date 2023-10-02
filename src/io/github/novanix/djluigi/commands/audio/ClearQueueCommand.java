package io.github.novanix.djluigi.commands.audio;

import io.github.novanix.djluigi.commands.Command;
import io.github.novanix.djluigi.commands.CommandCategory;
import io.github.novanix.djluigi.commands.CommandData;
import io.github.novanix.djluigi.server.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@CommandData
(
	command = "clear", 
	description = "Clears the queue",
	aliases = {"clearqueue"},
	djOnly = true,
	category = CommandCategory.Audio
)
public class ClearQueueCommand extends Command
{

	@Override
	public void executeCommand(Server s, SlashCommandInteractionEvent event) 
	{
		s.queue.clear();
		event.reply("Queue Cleared!").queue();
	}

}
