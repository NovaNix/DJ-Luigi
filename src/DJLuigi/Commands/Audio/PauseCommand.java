package DJLuigi.Commands.Audio;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@CommandData
(
	command = "pause", 
	description = "Pauses playback",
	aliases = {"stop"},
	category = CommandCategory.Audio
)
public class PauseCommand extends Command
{

	@Override
	public void executeCommand(Server s, SlashCommandInteractionEvent event) 
	{
		s.player.setPaused(true);
		event.reply("Playback Paused!").queue();
	}
	
}
