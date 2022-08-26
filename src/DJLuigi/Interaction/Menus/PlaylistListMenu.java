package DJLuigi.Interaction.Menus;

import java.util.ArrayList;

import DJLuigi.DJ;
import DJLuigi.Interaction.MenuContext;
import DJLuigi.Playlist.Playlist;
import DJLuigi.Playlist.PlaylistManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class PlaylistListMenu extends PagedMenu
{

	public static final String MENU_PLAYLIST_LIST = "PlaylistList";
	
	public static int playlistsPerPage = 15;
	
	public PlaylistListMenu()
	{
		super(MENU_PLAYLIST_LIST);
	}

	@Override
	protected MessageEmbed getPage(int page, MenuContext context)
	{
		StringBuilder description = new StringBuilder();
		
		ArrayList<Playlist> playlists = PlaylistManager.getPlaylists(context.server);
		
		int shownPlaylists = 0;
		
		for (int i = 0; i < playlistsPerPage; i++)
		{
			if (i + (page * playlistsPerPage) >= playlists.size())
			{
				break;
			}
			
			description.append((i + 1) + (playlistsPerPage * page) + ". " + "**" + playlists.get(i * playlistsPerPage).name + ",** " + playlists.get(i).getCreatorName());
			description.append("\n");
			shownPlaylists++;
		}
		
		MessageEmbed embed = new EmbedBuilder()
				.setTitle("Playlists")
				.setDescription(description.toString())
				.setColor(DJ.getPrimaryColor())
				.setFooter(String.format("Page %d of %d. (Showing %d playlists out of %d)", (page + 1), getPageCount(context), shownPlaylists, playlists.size()), null)
				.build();
		
		return embed;
	}

	@Override
	protected int getPageCount(MenuContext context)
	{
		return calculatePageCount(playlistsPerPage, PlaylistManager.getPlaylists(context.server).size());
	}

}
