package DJLuigi.Commands.Audio;

import DJLuigi.DJ;
import DJLuigi.Audio.SlashLoadResultHandler;
import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Commands.Parameter;
import DJLuigi.Server.Server;
import DJLuigi.utils.commandUtils;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Member;
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
	public void executeCommand(Server S, SlashCommandInteractionEvent event) 
	{
		Member self = event.getGuild().getMember(DJ.jda.getSelfUser());
		
		AudioChannel currentChannel = self.getVoiceState().getChannel();
		AudioChannel userChannel = event.getMember().getVoiceState().getChannel();
		
		String song = event.getOption("song").getAsString();
		
		if (userChannel == null)
		{
			event.reply("You must be in a voice channel for me to join!").setEphemeral(true).queue();
			return;
		}
		
		if (currentChannel == null)
		{
			S.JoinChannel(userChannel);
			currentChannel = userChannel;
		}
		
		else if (!currentChannel.equals(userChannel))
		{
			S.JoinChannel(userChannel);
			currentChannel = userChannel;
		}

		// Tell the user that we are loading
		event.deferReply().queue();
		
		if (commandUtils.isValidURL(song))
		{
			DJ.playerManager.loadItem(song, new SlashLoadResultHandler(S, event));
		}
		
		else
		{
			DJ.playerManager.loadItem("ytsearch:" + song, new SlashLoadResultHandler(S, event));
		}
		
	}
}
