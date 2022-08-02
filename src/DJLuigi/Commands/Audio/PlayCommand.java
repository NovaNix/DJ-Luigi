package DJLuigi.Commands.Audio;

import java.util.ArrayList;

import DJLuigi.DJ;
import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Server.Server;
import DJLuigi.utils.commandUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "play", 
	description = "Plays a song",
	aliases = {"p"},
	category = CommandCategory.Audio
)
public class PlayCommand implements Command
{

	@Override
	public void executeCommand(Server S, ArrayList<String> Parameters, MessageReceivedEvent event) 
	{
		Member self = event.getGuild().getMember(DJ.jda.getSelfUser());
		
		if (!self.getVoiceState().inVoiceChannel())
		{
			S.JoinChannel(event.getMember().getVoiceState().getChannel());
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
