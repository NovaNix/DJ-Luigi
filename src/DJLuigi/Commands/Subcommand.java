package DJLuigi.Commands;

import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface Subcommand
{
	public void executeCommand(Server S, SlashCommandInteractionEvent event);
}
