package DJLuigi.Commands.Audio;

import java.util.ArrayList;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

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
		// TODO output the name of the skipped song in the message
		S.queue.skip();
		
		event.reply("Song Skipped!").queue();
	}
	
}
