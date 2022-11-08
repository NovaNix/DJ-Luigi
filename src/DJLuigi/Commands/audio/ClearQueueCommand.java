package djLuigi.commands.audio;

import djLuigi.server.Server;
import djLuigi.commands.Command;
import djLuigi.commands.CommandCategory;
import djLuigi.commands.CommandData;
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
