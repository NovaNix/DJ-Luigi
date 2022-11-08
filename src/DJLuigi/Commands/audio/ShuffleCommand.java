package djLuigi.commands.audio;

import djLuigi.server.Server;
import djLuigi.commands.Command;
import djLuigi.commands.CommandCategory;
import djLuigi.commands.CommandData;
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
