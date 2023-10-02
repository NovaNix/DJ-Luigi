package io.github.novanix.djluigi.commands.audio;

import io.github.novanix.djluigi.DJ;
import io.github.novanix.djluigi.audio.Song;
import io.github.novanix.djluigi.commands.Command;
import io.github.novanix.djluigi.commands.CommandCategory;
import io.github.novanix.djluigi.commands.CommandData;
import io.github.novanix.djluigi.server.Server;
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
