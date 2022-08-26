package DJLuigi.Commands.Playlist;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@CommandData
(
	command = "listsongs", 
	description = "Lists the songs in the playlist",
	aliases = {"songs"},
	category = CommandCategory.Playlist
)
public class ListPlaylistSongsCommand extends Command
{

	@Override
	public void executeCommand(Server S, SlashCommandInteractionEvent event) 
	{
		event.reply("Sorry, this command is broken right now. Come back later").queue();
		
		
//		if (Parameters.size() == 0)
//		{
//			S.SendMessage("You need to specify the playlist!");
//			return;
//		}
//		
//		if (!PlaylistManager.hasPlaylist(Parameters.get(0)))
//		{
//			S.SendMessage("Unknown playlist: \"" + Parameters.get(0) + "\"");
//			return;
//		}
//		
//		Playlist p = PlaylistManager.getPlaylist(Parameters.get(0));
//		
//		if (p.size() > 0)
//		{
//			S.SendMessage("Playlist Size: " + p.size() + " Songs");
//			new ReactionList(p, 0, event);
//		}
//		
//		else
//		{
//			S.SendMessage("There are no songs in the playlist!");
//		}
		
	}

}
