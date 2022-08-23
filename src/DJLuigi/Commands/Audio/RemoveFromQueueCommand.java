package DJLuigi.Commands.Audio;

import java.util.ArrayList;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Commands.Parameter;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

@CommandData
(
	command = "remove", 
	description = "Removes a song from the queue",
	parameters = {
		@Parameter(name = "index", description = "The index of the song to remove.", type = OptionType.INTEGER, required = true)					
	},
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
			
			else if (songIndex >= S.queue.size())
			{
				S.SendMessage("That index doesnt exist!");
			}
			
			else
			{
				S.SendMessage("Removed song `" + S.queue.get(songIndex).name + "` from the queue.");
				S.queue.remove(songIndex);
			}
			
			
		} catch (NumberFormatException e)
		{
			S.SendMessage(Parameters.get(0) + " is not a valid number!");
		}
		
	}

	
	
}
