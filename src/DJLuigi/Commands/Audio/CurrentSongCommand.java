package DJLuigi.Commands.Audio;

import DJLuigi.DJ;
import DJLuigi.Audio.Song;
import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@CommandData
(
	command = "currentsong", 
	description = "Tells information about the current song",
	aliases = {"current", "nowplaying", "now"},
	category = CommandCategory.Audio
)
public class CurrentSongCommand extends Command
{

	@Override
	public void executeCommand(Server s, SlashCommandInteractionEvent event)
	{
		if (s.queue.size() == 0)
		{
			event.reply("Nothing is playing right now!").queue();
			return;
		}
		
		Song currentSong = s.queue.songs.get(0);
		
		MessageEmbed e = new EmbedBuilder()
				.setTitle(currentSong.name, currentSong.uri)
				.setColor(DJ.getPrimaryColor())
				.setDescription("*" + currentSong.author + ", " + currentSong.getLengthString() + "*")
				.build();

		event.replyEmbeds(e).queue();
		
	}

}
