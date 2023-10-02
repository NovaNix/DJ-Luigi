package io.github.novanix.djluigi.commands.audio;

import io.github.novanix.djluigi.DJ;
import io.github.novanix.djluigi.audio.SlashLoadResultHandler;
import io.github.novanix.djluigi.commands.Command;
import io.github.novanix.djluigi.commands.CommandCategory;
import io.github.novanix.djluigi.commands.CommandData;
import io.github.novanix.djluigi.commands.Parameter;
import io.github.novanix.djluigi.server.Server;
import io.github.novanix.djluigi.utils.CommandUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

@CommandData
(
	command = "play", 
	description = "Plays a song",
	aliases = {"p"},
	parameters = {
			@Parameter(name = "song", description = "The song that should be played", type = OptionType.STRING, required = true)
	},
	category = CommandCategory.Audio
)
public class PlayCommand extends Command
{

	@Override
	public void executeCommand(Server s, SlashCommandInteractionEvent event) 
	{
		Member self = event.getGuild().getMember(DJ.jda.getSelfUser());
		
		AudioChannelUnion currentChannel = self.getVoiceState().getChannel();
		AudioChannelUnion userChannel = event.getMember().getVoiceState().getChannel();
		
		String song = event.getOption("song").getAsString();
		
		if (userChannel == null)
		{
			event.reply("You must be in a voice channel for me to join!").setEphemeral(true).queue();
			return;
		}
		
		if (currentChannel == null)
		{
			s.joinChannel(userChannel);
			currentChannel = userChannel;
		}
		
		else if (!currentChannel.equals(userChannel))
		{
			s.joinChannel(userChannel);
			currentChannel = userChannel;
		}

		// Tell the user that we are loading
		event.deferReply().queue();
		
		if (CommandUtils.isValidURL(song))
		{
			DJ.playerManager.loadItem(song, new SlashLoadResultHandler(s, event));
		}
		
		else
		{
			DJ.playerManager.loadItem("ytsearch:" + song, new SlashLoadResultHandler(s, event));
		}
		
	}
}
