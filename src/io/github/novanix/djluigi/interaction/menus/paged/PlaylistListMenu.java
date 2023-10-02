package io.github.novanix.djluigi.interaction.menus.paged;

import java.util.ArrayList;

import io.github.novanix.djluigi.DJ;
import io.github.novanix.djluigi.interaction.MenuContext;
import io.github.novanix.djluigi.playlist.Playlist;
import io.github.novanix.djluigi.playlist.PlaylistManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class PlaylistListMenu extends PagedMenu
{
	
	public static int playlistsPerPage = 15;
	
	public PlaylistListMenu()
	{

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
			
			Playlist p = playlists.get(i + (page * playlistsPerPage));
			
			description.append((i + 1) + (playlistsPerPage * page) + ". " + "**" + p.displayName + "**#" + p.id + ", " + p.getCreatorName());
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
