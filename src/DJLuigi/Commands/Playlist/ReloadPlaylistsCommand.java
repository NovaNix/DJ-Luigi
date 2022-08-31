package DJLuigi.Commands.Playlist;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Playlist.PlaylistManager;
import DJLuigi.Server.Server;
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
	public void executeCommand(Server S, SlashCommandInteractionEvent event) 
	{
		event.reply("Reloading playlists...").queue();
		
		PlaylistManager.reloadPlaylists();
		
		event.getHook().editOriginal("Reloaded " + PlaylistManager.playlists.size() + " playlist(s)").queue();
	}

}
