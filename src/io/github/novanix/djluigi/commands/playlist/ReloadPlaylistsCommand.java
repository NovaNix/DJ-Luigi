package io.github.novanix.djluigi.commands.playlist;

import io.github.novanix.djluigi.commands.Command;
import io.github.novanix.djluigi.commands.CommandCategory;
import io.github.novanix.djluigi.commands.CommandData;
import io.github.novanix.djluigi.playlist.PlaylistManager;
import io.github.novanix.djluigi.server.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@CommandData
(
	command = "reload", 
	description = "Reloads all of the playlists",
	djOnly = true,
	category = CommandCategory.Playlist
)
public class ReloadPlaylistsCommand extends Command
{

	@Override
	public void executeCommand(Server s, SlashCommandInteractionEvent event) 
	{
		event.reply("Reloading playlists...").queue();
		
		PlaylistManager.reloadPlaylists();
		
		event.getHook().editOriginal("Reloaded " + PlaylistManager.playlists.size() + " playlist(s)").queue();
	}

}
