package io.github.novanix.djluigi.commands.audio;

import io.github.novanix.djluigi.commands.Command;
import io.github.novanix.djluigi.commands.CommandCategory;
import io.github.novanix.djluigi.commands.CommandData;
import io.github.novanix.djluigi.server.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@CommandData
(
	command = "shuffle", 
	description = "Shuffles the queue",
	aliases = {"shuff"},
	djOnly = true,
	category = CommandCategory.Audio
)
public class ShuffleCommand extends Command
{

	@Override
	public void executeCommand(Server s, SlashCommandInteractionEvent event)
	{
		s.queue.shuffle();
		event.reply("The queue has been shuffled!").queue();
	}

}
