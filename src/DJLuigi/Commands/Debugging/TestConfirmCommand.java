package DJLuigi.Commands.Debugging;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@CommandData
(
	command = "testconfirm", 
	description = "(Debug) Creates a confirmation menu for testing",
	debug = true,
	category = CommandCategory.Other
)
public class TestConfirmCommand extends Command
{

	@Override
	public void executeCommand(Server S, SlashCommandInteractionEvent event) 
	{
		event.reply("This command is broken rn, come back later").queue();
//		new ReactionConfirmation("This is a test. Accept?", event, 
//				() -> S.SendMessage("Approved"),
//				() -> S.SendMessage("Denied"));
		
	}

}
