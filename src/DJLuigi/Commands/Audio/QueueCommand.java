package DJLuigi.Commands.Audio;

import java.util.ArrayList;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Interaction.List.ReactionList;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "queue", 
	description = "Lists the songs in the queue",
	aliases = {"list", "q"},
	category = CommandCategory.Audio
)
public class QueueCommand implements Command
{

	@Override
	public void executeCommand(Server S, ArrayList<String> Parameters, MessageReceivedEvent event) 
	{
		ArrayList<AudioTrack> Tracks = S.trackScheduler.Tracks;
		
		if (Tracks.size() > 0)
		{
			S.SendMessage("Queue Size: " + Tracks.size());
			new ReactionList(S.trackScheduler, 0, event);
		}
		
		else
		{
			S.SendMessage("There are no songs in the queue!");
		}
		
	}

}
