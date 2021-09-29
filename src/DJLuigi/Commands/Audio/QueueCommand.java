package DJLuigi.Commands.Audio;

import java.util.ArrayList;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandData;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "queue", 
	description = "Lists the songs in the queue",
	aliases = {"list", "q"}
)
public class QueueCommand implements Command
{

	@Override
	public void executeCommand(Server S, ArrayList<String> Parameters, MessageReceivedEvent event) 
	{
		ArrayList<AudioTrack> Tracks = S.trackScheduler.Tracks;
		
		S.SendMessage("Queue Size: " + Tracks.size());
		
		StringBuilder list = new StringBuilder();
		
		list.append("```\n");
		
		for (int i = 0; i < Tracks.size(); i++)
		{
			list.append(Tracks.get(i).getInfo().title);
			list.append("\n");
			
		}
		
		list.append("```");
		
		S.SendMessage(list.toString());
	}

}
