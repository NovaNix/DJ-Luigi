package DJLuigi.Commands.Audio;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@CommandData
(
	command = "clearqueue", 
	description = "Clears the queue",
	aliases = {"clear"},
	djOnly = true,
	category = CommandCategory.Audio
)
public class ClearQueueCommand extends Command
{

	@Override
	public void executeCommand(Server S, SlashCommandInteractionEvent event) 
	{
		S.queue.clear();
		event.reply("Queue Cleared!").queue();
	}

}
