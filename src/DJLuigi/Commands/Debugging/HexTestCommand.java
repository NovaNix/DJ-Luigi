package DJLuigi.Commands.Debugging;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Commands.Parameter;
import DJLuigi.Server.Server;
import DJLuigi.utils.commandUtils;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

@CommandData
(
	command = "hextest", 
	description = "(Debug) Outputs the hex code of a number",
	parameters = {
			@Parameter(name = "number", description = "The number to get the hex code of", type = OptionType.INTEGER, required = true)
	},
	debug = true,
	category = CommandCategory.Other
)
public class HexTestCommand extends Command
{

	@Override
	public void executeCommand(Server S, SlashCommandInteractionEvent event)
	{
		int num = event.getOption("number").getAsInt();
		event.reply(commandUtils.numberToHex(num)).queue();
		
	}

}
