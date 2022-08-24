package DJLuigi.Commands.Audio;

import DJLuigi.Audio.Song;
import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Server.Server;
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
	public void executeCommand(Server S, SlashCommandInteractionEvent event) 
	{
		if (S.queue.size() == 0)
		{
			event.reply("There's nothing to skip!").queue();
		}
		
		event.reply("Skipping...").queue();
		
		Song removed = S.queue.skip();
		
		event.getHook().editOriginal("Skipped `" + removed.name + "`...").queue();
		
	}
	
}
