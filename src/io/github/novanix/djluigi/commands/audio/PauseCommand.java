package io.github.novanix.djluigi.commands.audio;

import io.github.novanix.djluigi.commands.Command;
import io.github.novanix.djluigi.commands.CommandCategory;
import io.github.novanix.djluigi.commands.CommandData;
import io.github.novanix.djluigi.server.Server;
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
