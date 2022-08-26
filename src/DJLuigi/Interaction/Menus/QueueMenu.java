package DJLuigi.Interaction.Menus;

import DJLuigi.DJ;
import DJLuigi.Audio.Queue;
import DJLuigi.Interaction.MenuContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class QueueMenu extends PagedMenu
{

	public static final String MENU_QUEUE = "QueueMenu";
	
	public static final int songsPerPage = 10;
	
	public QueueMenu()
	{
		super(MENU_QUEUE);
	}

	@Override
	protected MessageEmbed getPage(int page, MenuContext context)
	{
		Queue queue = context.server.queue;
		
		StringBuilder description = new StringBuilder();
		
		int shownSongs = 0;
		
		for (int i = 0; i < songsPerPage; i++)
		{
			if (i + (page * songsPerPage) >= queue.size())
			{
				break;
			}
			
			description.append((i + 1) + (songsPerPage * page) + ". " + queue.get(i * songsPerPage).getQueueEntryString());
			description.append("\n");
			shownSongs++;
		}
		
		MessageEmbed embed = new EmbedBuilder()
				.setTitle((queue.looped ? "🔁 " : "") + "Queue")
				.setDescription(description.toString())
				.setColor(DJ.getPrimaryColor())
				.setFooter(String.format("Page %d of %d. (Showing %d songs out of %d)", (page + 1), getPageCount(context), shownSongs, queue.size()), null)
				.build();
		
		return embed;

	}

	@Override
	protected int getPageCount(MenuContext context)
	{
		return calculatePageCount(songsPerPage, context.server.queue.size());
		
	}

}
