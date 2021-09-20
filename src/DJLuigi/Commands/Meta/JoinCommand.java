package DJLuigi.Commands.Meta;

import java.util.ArrayList;

import DJLuigi.DJ;
import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandData;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "join", 
	description = "Makes the bot join VC",
	aliases = {"connect", "summon"}
)
public class JoinCommand implements Command
{

	@Override
	public void executeCommand(Server S, ArrayList<String> parameters, MessageReceivedEvent event) 
	{
		GuildVoiceState state = event.getMember().getVoiceState();
		
		if (!state.inVoiceChannel())
		{
			S.SendMessage("Youre not in a channel!");
			
			return;
		}
		
		VoiceChannel channel = state.getChannel();
		
		S.JoinChannel(channel);
		
		if (S.trackScheduler.Tracks.size() == 0)
		{
			DJ.playerManager.loadItem("https://www.youtube.com/watch?v=nQTxUKsL7iw", S.resultHandler);
		}
	}
	
}
