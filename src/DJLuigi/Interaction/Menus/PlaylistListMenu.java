package DJLuigi.Interaction.Menus;

import DJLuigi.Interaction.MenuContext;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class PlaylistListMenu extends PagedMenu
{

	public static final String MENU_PLAYLIST_LIST = "PlaylistList";
	
	public PlaylistListMenu()
	{
		super(MENU_PLAYLIST_LIST);
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
