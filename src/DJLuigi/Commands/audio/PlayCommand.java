package djLuigi.commands.audio;

import djLuigi.DJ;
import djLuigi.server.Server;
import djLuigi.audio.SlashLoadResultHandler;
import djLuigi.commands.Command;
import djLuigi.commands.CommandCategory;
import djLuigi.commands.CommandData;
import djLuigi.commands.Parameter;
import djLuigi.utils.CommandUtils;
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
	public void executeCommand(Server s, SlashCommandInteractionEvent event) 
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
