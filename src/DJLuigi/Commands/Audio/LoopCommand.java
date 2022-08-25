package DJLuigi.Commands.Audio;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Commands.Parameter;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

@CommandData
(
	command = "loop", 
	description = "Sets if the queue should be looped or not",
	parameters = {
		@Parameter(name = "looped", description = "The new looped status", type = OptionType.BOOLEAN)
	},
	aliases = {"l"},
	djOnly = true,
	category = CommandCategory.Audio
)
public class LoopCommand extends Command
{

	@Override
	public void executeCommand(Server S, SlashCommandInteractionEvent event) 
	{
		if (event.getOption("looped") == null)
		{
			event.reply("Set looped status to : `" + S.queue.toggleLoop() + "`!").queue();
		}
		
		else
		{
			boolean loopStatus = event.getOption("looped").getAsBoolean();
			S.queue.setLoop(loopStatus);
			event.reply("Set looped status to : `" + loopStatus + "`!").queue();
		}
		
	}

}
