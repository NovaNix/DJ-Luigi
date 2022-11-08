package djLuigi.commands.debugging;

import com.fasterxml.jackson.core.JsonProcessingException;

import djLuigi.server.Server;
import djLuigi.commands.Command;
import djLuigi.commands.CommandCategory;
import djLuigi.commands.CommandData;
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
