package DJLuigi.Commands.Audio;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Interaction.MenuHandler;
import DJLuigi.Interaction.Menus.QueueMenu;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@CommandData
(
	command = "queue", 
	description = "Lists the songs in the queue",
	aliases = {"list", "q"},
	category = CommandCategory.Audio
)
public class QueueCommand extends Command
{

	@Override
	public void executeCommand(Server S, SlashCommandInteractionEvent event) 
	{
		
		if (S.queue.size() > 0)
		{
			MenuHandler.createMenu(QueueMenu.class, event);
		}
		
		else
		{
			event.reply("There are no songs in the queue!").queue();
		}
		
	}

}
