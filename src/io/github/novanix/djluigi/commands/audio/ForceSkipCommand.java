package io.github.novanix.djluigi.commands.audio;

import io.github.novanix.djluigi.audio.Song;
import io.github.novanix.djluigi.commands.Command;
import io.github.novanix.djluigi.commands.CommandCategory;
import io.github.novanix.djluigi.commands.CommandData;
import io.github.novanix.djluigi.server.Server;
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
