package io.github.novanix.djluigi.interaction.menus.paged;

import io.github.novanix.djluigi.DJ;
import io.github.novanix.djluigi.audio.Song;
import io.github.novanix.djluigi.interaction.MenuContext;
import io.github.novanix.djluigi.playlist.Playlist;
import io.github.novanix.djluigi.playlist.PlaylistManager;
import io.github.novanix.djluigi.utils.CommandUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class PlaylistSongsMenu extends PagedMenu
{

	public static final int songsPerPage = 10;
	
	@Override
	protected MessageEmbed getPage(int page, MenuContext context)
	{
		if (context.stateInfo.length == 0)
		{
			return new EmbedBuilder()
					.setTitle("ERROR")
					.setDescription("Something went wrong!")
					.setColor(CommandUtils.ERROR_COLOR)
					.build();
		}
		
		String playlistName = context.stateInfo[0];
		
		if (!PlaylistManager.hasPlaylist(playlistName))
		{
			return new EmbedBuilder()
					.setTitle("ERROR")
					.setDescription("Failed to find playlist: " + playlistName + ".")
					.setColor(CommandUtils.ERROR_COLOR)
					.build();
		}
		
		Playlist p = PlaylistManager.getPlaylist(playlistName);
		
		StringBuilder description = new StringBuilder();
		
		int shownSongs = 0;
		
		for (int i = 0; i < songsPerPage; i++)
		{
			if (i + (page * songsPerPage) >= p.size())
			{
				break;
			}
			
			Song song = p.getSong(i + (page * songsPerPage));
			
			description.append((i + 1) + (songsPerPage * page) + ". " + song.getQueueEntryString());
			description.append("\n");
			shownSongs++;
		}
		
		MessageEmbed embed = new EmbedBuilder()
				.setTitle(p.displayName)
				.setDescription(description.toString())
				.setColor(DJ.getPrimaryColor())
				.setFooter(String.format("Page %d of %d. (Showing %d songs out of %d)", (page + 1), getPageCount(context), shownSongs, p.size()), null)
				.build();
		
		return embed;
	}

	@Override
	protected int getPageCount(MenuContext context)
	{
		if (context.stateInfo.length == 0)
		{
			return 1; // Something went wrong!
		}
		
		String playlistName = context.stateInfo[0];
		Playlist p = PlaylistManager.getPlaylist(playlistName);
		
		return calculatePageCount(songsPerPage, p.size());
	}

}
