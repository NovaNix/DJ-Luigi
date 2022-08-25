package DJLuigi.Commands.Audio;

import DJLuigi.Audio.Song;
import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Commands.Parameter;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
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
public class RemoveFromQueueCommand extends Command
{

	@Override
	public void executeCommand(Server S, SlashCommandInteractionEvent event) 
	{
		int songIndex = event.getOption("index").getAsInt() - 1;
		
		if (songIndex == -1)
		{
			event.reply("Index cannot be 0!").queue();
		}
		
		else if (songIndex < 0)
		{
			event.reply("Song Index Cannot be Negative!").queue();
		}
			
		else if (songIndex == 0)
		{
			event.reply("You can't remove a song thats currently playing!").queue();
		}
			
		else if (songIndex >= S.queue.size())
		{
			event.reply("That index is out of bounds! (Current queue length is " + S.queue.size() + ")").queue();;
		}
			
		else
		{
			Song removed = S.queue.remove(songIndex);
			event.reply("Removed song `" + removed.name + "` from the queue.").queue();
			
		}
	}
	
	// TODO look into adding a min and max range for the slash command
	
//	@Override
//	protected void setSlashCommandParameters(SlashCommandData data)
//	{
//		OptionData indexOption = new OptionData(OptionType.INTEGER, "index", "The index of the song to remove.", true);
//		indexOption.
//		
//		data.addOptions(indexOption);
//	}

	
	
}
