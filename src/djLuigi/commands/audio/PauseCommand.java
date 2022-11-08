package djLuigi.commands.audio;

import djLuigi.server.Server;
import djLuigi.commands.Command;
import djLuigi.commands.CommandCategory;
import djLuigi.commands.CommandData;
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
