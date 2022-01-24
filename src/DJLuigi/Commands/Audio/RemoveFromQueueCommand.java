package DJLuigi.Commands.Audio;

import java.util.ArrayList;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "remove", 
	description = "Removes a song from the queue",
	aliases = {"removesong"},
	category = CommandCategory.Audio
)
public class RemoveFromQueueCommand implements Command
{

	@Override
	public void executeCommand(Server S, ArrayList<String> Parameters, MessageReceivedEvent event) 
	{
		try
		{
			int songIndex = Integer.parseInt(Parameters.get(0)) - 1;
		
			if (songIndex < 0)
			{
				S.SendMessage("Song Index Cannot be Negative!");
			}
			
			else if (songIndex == 0)
			{
				S.SendMessage("You can't remove a song thats currently playing!");
			}
			
			else if (songIndex >= S.trackScheduler.Tracks.size())
			{
				S.SendMessage("That index doesnt exist!");
			}
			
			else
			{
				S.SendMessage("Removed song " + S.trackScheduler.Tracks.get(songIndex).getInfo().title + " from the queue.");
				S.trackScheduler.remove(songIndex);
			}
			
			
		} catch (NumberFormatException e)
		{
			S.SendMessage(Parameters.get(0) + " is not a valid number!");
		}
		
	}

	
	
}
