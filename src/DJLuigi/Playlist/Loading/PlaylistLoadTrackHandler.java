package DJLuigi.Playlist.Loading;

import java.io.IOException;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import DJLuigi.Audio.Song;
import DJLuigi.Playlist.Playlist;
import DJLuigi.Server.Server;

public class PlaylistLoadTrackHandler implements AudioLoadResultHandler
{

	private Server HostServer;
	private Playlist p;
	
	public PlaylistLoadTrackHandler(Server HostServer, Playlist p)
	{
		this.HostServer = HostServer;
		this.p = p;
	}
	
	@Override
	public void trackLoaded(AudioTrack track) 
	{
		try {
			p.addSong(new Song(track));
			HostServer.SendMessage("Added " + track.getInfo().title + " to playlist " + p.name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			HostServer.SendMessage("Something went wrong!");
			e.printStackTrace();
		}
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) 
	{
	
		for (AudioTrack track : playlist.getTracks()) 
		{
			try {
				p.addSong(new Song(track));
			} catch (IOException e) {
				HostServer.SendMessage("Something went wrong adding song " + track.getInfo().title);
				e.printStackTrace();
			}
		}
			
		HostServer.SendMessage("Added " + playlist.getTracks().size() + " Songs to playlist " + p.name);
		
	}

	@Override
	public void noMatches() 
	{
		HostServer.SendMessage("Hmm, I couldnt find that...");
		
	}

	@Override
	public void loadFailed(FriendlyException exception) 
	{
		HostServer.SendMessage("Failed to load song: " + exception.getMessage());
	}

	
	
}
