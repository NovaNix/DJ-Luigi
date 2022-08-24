package DJLuigi.Commands.Playlist;

import java.util.ArrayList;

import DJLuigi.DJ;
import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Playlist.Playlist;
import DJLuigi.Playlist.PlaylistManager;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "playlistinfo", 
	description = "Lists information about the playlist",
	aliases = {"info", "pinfo"},
	category = CommandCategory.Playlist
)
public class PlaylistInfoCommand extends Command 
{

	@SuppressWarnings("deprecation")
	@Override
	public void executeCommand(Server S, SlashCommandInteractionEvent event) 
	{			
		
		if (!PlaylistManager.hasPlaylist(Parameters.get(0)))
		{
			S.SendMessage("Unknown playlist: \"" + Parameters.get(0) + "\"");
			return;
		}
		
		Playlist p = PlaylistManager.getPlaylist(Parameters.get(0));
		
		User user = DJ.jda.getUserById(p.creatorID);
		
		MessageEmbed embed = new EmbedBuilder()
				.setTitle(p.name)
				.setColor(DJ.getPrimaryColor())
				.setFooter("Owner: " + user.getName() + ", " + p.editors.size() + " editors", user.getAvatarUrl())
				.setThumbnail("https://i.redd.it/b2pilioyu7u21.jpg")
				.addField("Songs", p.songs.size() + " Songs", false)
				.addField("Is Public", "" + p.isPublic, false)
				.addField("Edit Status", p.editPermissions + " (will be replaced with more readable value later)", false)
				.build();
		
		  event.getChannel().sendMessageEmbeds(embed).queue();
	}

}
