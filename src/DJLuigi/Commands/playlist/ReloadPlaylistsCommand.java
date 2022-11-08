package djLuigi.commands.playlist;

import djLuigi.server.Server;
import djLuigi.commands.Command;
import djLuigi.commands.CommandCategory;
import djLuigi.commands.CommandData;
import djLuigi.playlist.PlaylistManager;
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
