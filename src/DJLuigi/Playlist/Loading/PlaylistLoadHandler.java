package DJLuigi.Playlist.Loading;

import java.io.IOException;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import DJLuigi.Server.Server;

public class PlaylistLoadHandler implements AudioLoadResultHandler
{

	private Server HostServer;
	
	public PlaylistLoadHandler(Server HostServer)
	{
		this.HostServer = HostServer;
	}
	
	@Override
	public void trackLoaded(AudioTrack track) 
	{
		HostServer.trackScheduler.queue(track);
		//HostServer.SendMessage("Added " + track.getInfo().title + " to playlist " + p.name);

	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) 
	{
	
		for (AudioTrack track : playlist.getTracks()) 
		{
			HostServer.trackScheduler.queue(track);
		}
			
		//HostServer.SendMessage("Added " + playlist.getTracks().size() + " Songs to playlist " + p.name);
		
	}

	@Override
	public void noMatches() 
	{
		//HostServer.SendMessage("Hmm, I couldnt find that...");
		
	}

	@Override
	public void loadFailed(FriendlyException exception) 
	{
		// TODO Auto-generated method stub
		HostServer.SendMessage("Something went wrong!");
		
	}

}
