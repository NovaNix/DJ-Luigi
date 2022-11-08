package djLuigi.commands.debugging;

import djLuigi.server.Server;
import djLuigi.commands.Command;
import djLuigi.commands.CommandCategory;
import djLuigi.commands.CommandData;
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
	public void executeCommand(Server s, SlashCommandInteractionEvent event) 
	{
		event.reply("This command is broken right now, come back later").queue();
//		new ReactionConfirmation("This is a test. Accept?", event, 
//				() -> S.SendMessage("Approved"),
//				() -> S.SendMessage("Denied"));
		
	}

}
