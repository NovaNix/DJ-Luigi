package djLuigi.commands.audio;

import djLuigi.interaction.MenuHandler;
import djLuigi.interaction.menus.paged.QueueMenu;
import djLuigi.server.Server;
import djLuigi.commands.Command;
import djLuigi.commands.CommandCategory;
import djLuigi.commands.CommandData;
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
	public void executeCommand(Server s, SlashCommandInteractionEvent event) 
	{
		
		if (s.queue.size() > 0)
		{
			MenuHandler.createMenu(QueueMenu.class, event);
		}
		
		else
		{
			event.reply("There are no songs in the queue!").queue();
		}
		
	}

}
