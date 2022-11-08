package djLuigi.commands.audio;

import djLuigi.server.Server;
import djLuigi.commands.Command;
import djLuigi.commands.CommandCategory;
import djLuigi.commands.CommandData;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@CommandData
(
	command = "resume", 
	description = "Resumes playback",
	aliases = {"start"},
	category = CommandCategory.Audio
)
public class ResumeCommand extends Command
{

	@Override
	public void executeCommand(Server s, SlashCommandInteractionEvent event) 
	{
		s.player.setPaused(false);
		event.reply("Queue Resumed!").queue();
	}
	
}
