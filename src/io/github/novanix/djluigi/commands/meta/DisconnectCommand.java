package io.github.novanix.djluigi.commands.meta;

import io.github.novanix.djluigi.commands.Command;
import io.github.novanix.djluigi.commands.CommandCategory;
import io.github.novanix.djluigi.commands.CommandData;
import io.github.novanix.djluigi.server.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@CommandData
(
	command = "disconnect", 
	description = "Removed the bot from VC.",
	aliases = {"dc", "leave"},
	category = CommandCategory.Control
)
public class DisconnectCommand extends Command
{

	@Override
	public void executeCommand(Server s, SlashCommandInteractionEvent event) 
	{
		if (s.isInVC())
		{
			s.leaveVC();
			event.reply("Disconnected!").queue();
		}
		
		else
		{
			event.reply("I am not in a voice channel!").setEphemeral(true).queue();
		}
		
	}

}
