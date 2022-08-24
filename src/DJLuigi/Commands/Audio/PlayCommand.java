package DJLuigi.Commands.Audio;

import java.util.ArrayList;

import DJLuigi.DJ;
import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Commands.Parameter;
import DJLuigi.Server.Server;
import DJLuigi.utils.commandUtils;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
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
		
		if (Parameters.size() == 0)
		{
			S.SendMessage("You have to specify a song!");
			return;
		}
		
		if (currentChannel == null)
		{
			if (userChannel != null)
			{
				S.JoinChannel(userChannel);
				currentChannel = userChannel;
			}
			
			else
			{
				S.SendMessage("You must be in a voice channel for me to join!");
				return;
			}
		}
		
		else if (!currentChannel.equals(userChannel))
		{
			S.JoinChannel(userChannel);
			currentChannel = userChannel;
		}
		
		String combinedParameters = commandUtils.parametersToString(Parameters);
		
		S.SendMessage("Loading `" + combinedParameters + "`");
		
		if (commandUtils.isValidURL(Parameters.get(0)))
		{
			DJ.playerManager.loadItem(Parameters.get(0), S.resultHandler);
		}
		
		else
		{
			DJ.playerManager.loadItem("ytsearch:" + combinedParameters, S.resultHandler);
		}
		
	}
}
