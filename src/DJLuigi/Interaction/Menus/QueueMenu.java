package DJLuigi.Interaction.Menus;

import DJLuigi.Interaction.MenuContext;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int getPageCount(MenuContext context)
	{
		// TODO Auto-generated method stub
		return 0;
	}

}
