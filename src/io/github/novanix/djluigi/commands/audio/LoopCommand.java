package io.github.novanix.djluigi.commands.audio;

import io.github.novanix.djluigi.commands.Command;
import io.github.novanix.djluigi.commands.CommandCategory;
import io.github.novanix.djluigi.commands.CommandData;
import io.github.novanix.djluigi.commands.Parameter;
import io.github.novanix.djluigi.server.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

@CommandData
(
	command = "loop", 
	description = "Sets if the queue should be looped or not",
	parameters = {
		@Parameter(name = "looped", description = "The new looped status", type = OptionType.BOOLEAN, required = false)
	},
	aliases = {"l"},
	djOnly = true,
	category = CommandCategory.Audio
)
public class LoopCommand extends Command
{

	@Override
	public void executeCommand(Server s, SlashCommandInteractionEvent event) 
	{
		OptionMapping loopParameter = event.getOption("looped");
		
		if (loopParameter == null)
		{
			s.queue.toggleLoop();
		}
		
		else
		{
			s.queue.setLoop(loopParameter.getAsBoolean());	
		}
		
		event.reply("Set looped status to : `" + s.queue.looped + "`!").queue();
		
	}

}
