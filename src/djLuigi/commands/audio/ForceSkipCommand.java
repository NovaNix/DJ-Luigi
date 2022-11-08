package djLuigi.commands.audio;

import djLuigi.server.Server;
import djLuigi.audio.Song;
import djLuigi.commands.Command;
import djLuigi.commands.CommandCategory;
import djLuigi.commands.CommandData;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@CommandData
(
	command = "fs", 
	description = "Skips the song without a vote.",
	aliases = {"forceskip"},
	djOnly = true,
	category = CommandCategory.Audio
)
public class ForceSkipCommand extends Command
{

	@Override
	public void executeCommand(Server s, SlashCommandInteractionEvent event) 
	{
		if (s.queue.size() == 0)
		{
			event.reply("There's nothing to skip!").queue();
			return;
		}
		
		event.reply("Skipping...").queue();
		
		Song removed = s.queue.skip();
		
		event.getHook().editOriginal("Skipped `" + removed.name + "`...").queue();
		
	}
	
}
