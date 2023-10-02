package io.github.novanix.djluigi.playlist.loading;

import java.io.IOException;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import io.github.novanix.djluigi.audio.Song;
import io.github.novanix.djluigi.playlist.Playlist;
import io.github.novanix.djluigi.server.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PlaylistLoadTrackHandler implements AudioLoadResultHandler
{

	private Server hostServer;
	private Playlist p;
	private SlashCommandInteractionEvent event;
	
	public PlaylistLoadTrackHandler(Server host, Playlist p, SlashCommandInteractionEvent event)
	{
		this.hostServer = host;
		this.p = p;
		this.event = event;
	}
	
	@Override
	public void trackLoaded(AudioTrack track) 
	{
		try {
			p.addSong(new Song(track));
			event.getHook().sendMessage("Added `" + track.getInfo().title + "` to playlist `" + p.displayName + "`").queue();
		} catch (IOException e) {
			event.getHook().sendMessage("Something went wrong! Please notify a developer!").queue();
			e.printStackTrace();
		}
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) 
	{
		int failedSongCount = 0;
		
		for (AudioTrack track : playlist.getTracks()) 
		{
			try {
				p.addSong(new Song(track));
			} catch (IOException e) {
				hostServer.sendMessage("Something went wrong adding song `" + track.getInfo().title + "` to the playlist `" + p.displayName + "`");
				failedSongCount++;
				e.printStackTrace();
			}
		}
			
		event.getHook().sendMessage("Added " + (playlist.getTracks().size() - failedSongCount) + " songs to playlist `" + p.displayName + "`").queue();
		
	}

	@Override
	public void noMatches() 
	{
		event.getHook().sendMessage("Hmm, I couldnt find that...").queue();
	}

	@Override
	public void loadFailed(FriendlyException exception) 
	{
		event.getHook().sendMessage("Failed to load song: `" + exception.getMessage() + "`").queue();
	}

	
	
}
