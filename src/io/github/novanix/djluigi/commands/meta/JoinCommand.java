package io.github.novanix.djluigi.commands.meta;

import io.github.novanix.djluigi.commands.Command;
import io.github.novanix.djluigi.commands.CommandCategory;
import io.github.novanix.djluigi.commands.CommandData;
import io.github.novanix.djluigi.server.Server;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@CommandData
(
	command = "join", 
	description = "Makes the bot join VC",
	aliases = {"connect", "summon"},
	category = CommandCategory.Control
)
public class JoinCommand extends Command
{

	@Override
	public void executeCommand(Server s, SlashCommandInteractionEvent event) 
	{
		GuildVoiceState state = event.getMember().getVoiceState();
		
		if (!state.inAudioChannel())
		{
			event.reply("You're not in a channel!").setEphemeral(true).queue();
			
			return;
		}
		
		AudioChannelUnion channel = state.getChannel();
		
		s.joinChannel(channel);
		
		event.reply("Joined `" + channel.getName() + "`").queue();
	}
	
}
