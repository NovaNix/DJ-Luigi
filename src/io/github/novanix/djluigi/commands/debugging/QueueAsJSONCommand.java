package io.github.novanix.djluigi.commands.debugging;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.github.novanix.djluigi.commands.Command;
import io.github.novanix.djluigi.commands.CommandCategory;
import io.github.novanix.djluigi.commands.CommandData;
import io.github.novanix.djluigi.server.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@CommandData
(
	command = "queueasjson", 
	description = "(Debug) Outputs the current queue as a json file",
	aliases = {"jsonqueue", "queuejson"},
	debug = true,
	category = CommandCategory.Other
)
public class QueueAsJSONCommand extends Command
{

	@Override
	public void executeCommand(Server s, SlashCommandInteractionEvent event)
	{
		try
		{
			event.reply("```JSON\n" + s.queue.toJSON() + "```").queue();
		} catch (JsonProcessingException e)
		{
			event.reply("Failed to output queue as JSON. Check console for details...").queue();
			e.printStackTrace();
		}
		
	}
	
}
