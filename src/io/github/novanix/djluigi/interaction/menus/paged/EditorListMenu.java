package io.github.novanix.djluigi.interaction.menus.paged;

import io.github.novanix.djluigi.DJ;
import io.github.novanix.djluigi.interaction.MenuContext;
import io.github.novanix.djluigi.playlist.Playlist;
import io.github.novanix.djluigi.playlist.PlaylistManager;
import io.github.novanix.djluigi.utils.CommandUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

public class EditorListMenu extends PagedMenu
{

	public static int editorsPerPage = 15;

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
		
		int shownEditors = 0;
		
		for (int i = 0; i < editorsPerPage; i++)
		{
			if (i + (page * editorsPerPage) >= p.editors.size())
			{
				break;
			}
			
			String editorId = p.editors.get(i + (page * editorsPerPage));
			
			User editor = DJ.jda.retrieveUserById(editorId).complete();
			
			description.append((i + 1) + (editorsPerPage * page) + ". " + editor.getAsMention());
			description.append("\n");
			shownEditors++;
		}
		
		MessageEmbed embed = new EmbedBuilder()
				.setTitle(p.displayName + " Editors")
				.setDescription(description.toString())
				.setColor(DJ.getPrimaryColor())
				.setFooter(String.format("Page %d of %d. (Showing %d editors out of %d)", (page + 1), getPageCount(context), shownEditors, p.editors.size()), null)
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
		
		return calculatePageCount(editorsPerPage, p.editors.size());
	}
	
}
