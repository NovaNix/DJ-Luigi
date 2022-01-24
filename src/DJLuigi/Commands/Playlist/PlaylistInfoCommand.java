package DJLuigi.Commands.Playlist;

import java.awt.Color;
import java.util.ArrayList;

import DJLuigi.DJ;
import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Playlist.Playlist;
import DJLuigi.Playlist.PlaylistManager;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "playlistinfo", 
	description = "Lists information about the playlist",
	aliases = {"info"},
	category = CommandCategory.Playlist
)
public class PlaylistInfoCommand implements Command 
{

	@SuppressWarnings("deprecation")
	@Override
	public void executeCommand(Server S, ArrayList<String> Parameters, MessageReceivedEvent event) 
	{			
		
		if (!PlaylistManager.hasPlaylist(Parameters.get(0)))
		{
			S.SendMessage("Unknown playlist: \"" + Parameters.get(0) + "\"");
			return;
		}
		
		Playlist p = PlaylistManager.getPlaylist(Parameters.get(0));
		
		Guild g = DJ.jda.getGuildById(p.homeServerID);
		
		MessageEmbed embed = new EmbedBuilder()
				.setTitle(p.name)
				.setColor(new Color(6971865))
				.setFooter("Owner: <@" + p.creatorID + ">, 0 editors", "https://i.redd.it/b2pilioyu7u21.jpg")
				.setThumbnail("https://i.redd.it/b2pilioyu7u21.jpg")
				.addField("Songs", p.songs.size() + " Songs", false)
				.addField("Home Server", g.getName(), false)
				.addField("Home Server Only?", "" + p.serverDependent, false)
				.addField("Edit Status", p.editPermissions + " (will be replaced with more readable value later)", false)
				.build();
		
		  event.getChannel().sendMessage(embed).queue();
	}

}
