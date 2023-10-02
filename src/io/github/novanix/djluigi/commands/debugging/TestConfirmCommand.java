package io.github.novanix.djluigi.commands.debugging;

import io.github.novanix.djluigi.commands.Command;
import io.github.novanix.djluigi.commands.CommandCategory;
import io.github.novanix.djluigi.commands.CommandData;
import io.github.novanix.djluigi.server.Server;
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
