package io.github.novanix.djluigi.commands.playlist;

import java.util.ArrayList;

import io.github.novanix.djluigi.DJ;
import io.github.novanix.djluigi.audio.Song;
import io.github.novanix.djluigi.commands.Command;
import io.github.novanix.djluigi.commands.CommandCategory;
import io.github.novanix.djluigi.commands.CommandData;
import io.github.novanix.djluigi.commands.Parameter;
import io.github.novanix.djluigi.playlist.Playlist;
import io.github.novanix.djluigi.playlist.PlaylistManager;
import io.github.novanix.djluigi.playlist.loading.PlaylistLoadHandler;
import io.github.novanix.djluigi.server.Server;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

@CommandData
(
	command = "playplaylist", 
	description = "Adds a playlist to the queue",
	parameters = {
		@Parameter(name = "playlist", description = "The playlist that should be played", type = OptionType.STRING, required = true)
	},
	aliases = {"pp", "pplaylist"},
	category = CommandCategory.Playlist
)
public class PlayPlaylistCommand extends Command
{

	@Override
	public void executeCommand(Server s, SlashCommandInteractionEvent event) 
	{
		// TODO consider add dropdown if multiple playlists share the same name
		
		Member self = event.getGuild().getMember(DJ.jda.getSelfUser());
		
		AudioChannelUnion currentChannel = self.getVoiceState().getChannel();
		AudioChannelUnion userChannel = event.getMember().getVoiceState().getChannel();
		
		String playlistName = event.getOption("playlist").getAsString();
		
		if (!PlaylistManager.hasPlaylist(playlistName))
		{
			event.reply("Unknown playlist: \"" + playlistName + "\"").queue();
			return;
		}
		
		if (userChannel == null)
		{
			event.reply("You must be in a voice channel for me to join!").setEphemeral(true).queue();
			return;
		}
		
		if (currentChannel == null)
		{
			s.joinChannel(userChannel);
			currentChannel = userChannel;
		}
		
		else if (!currentChannel.equals(userChannel))
		{
			s.joinChannel(userChannel);
			currentChannel = userChannel;
		}
		
		Playlist p = PlaylistManager.getPlaylist(playlistName);
		
		ArrayList<Song> songs = p.songs;
		
		for (int i = 0; i < songs.size(); i++)
		{
			DJ.playerManager.loadItem(songs.get(i).uri, new PlaylistLoadHandler(s));
		}
		
		event.reply("Loaded `" + songs.size() + "` songs from playlist `" + p.displayName + "`").queue();
		
	}

}
