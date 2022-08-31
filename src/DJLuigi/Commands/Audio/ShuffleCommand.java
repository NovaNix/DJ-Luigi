package DJLuigi.Commands.Audio;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Server.Server;
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
	public void executeCommand(Server S, SlashCommandInteractionEvent event)
	{
		S.queue.shuffle();
		event.reply("The queue has been shuffled!").queue();
	}

}
