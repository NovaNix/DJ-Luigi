package DJLuigi.Commands.Audio;

import java.awt.Color;
import java.util.ArrayList;

import DJLuigi.DJ;
import DJLuigi.Audio.Song;
import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Playlist.PlaylistManager;
import DJLuigi.Server.Server;
import DJLuigi.utils.DiscordUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "currentsong", 
	description = "Tells information about the current song",
	aliases = {"current", "nowplaying", "now"},
	category = CommandCategory.Audio
)
public class CurrentSongCommand implements Command
{

	@SuppressWarnings("deprecation")
	@Override
	public void executeCommand(Server S, ArrayList<String> Parameters, MessageReceivedEvent event)
	{
		if (S.queue.size() == 0)
		{
			S.SendMessage("Nothing is playing right now!");
		}
		
		Song currentSong = S.queue.songs.get(0);
		
		MessageEmbed e = new EmbedBuilder()
											.setTitle(currentSong.name, currentSong.uri)
											.setColor(DJ.getPrimaryColor())
											.setDescription("*" + currentSong.author + ", " + currentSong.getLengthString() + "*")
											.build();

		event.getChannel().sendMessageEmbeds(e).queue();
		
	}

}
