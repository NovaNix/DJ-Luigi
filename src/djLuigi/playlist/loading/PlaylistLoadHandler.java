package djLuigi.playlist.loading;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import djLuigi.server.Server;

public class PlaylistLoadHandler implements AudioLoadResultHandler
{

	private Server hostServer;
	
	public PlaylistLoadHandler(Server host)
	{
		this.hostServer = host;
	}
	
	@Override
	public void trackLoaded(AudioTrack track) 
	{
		hostServer.queue.add(track);
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) 
	{
		for (AudioTrack track : playlist.getTracks()) 
		{
			hostServer.queue.add(track);
		}
	}

	@Override
	public void noMatches() 
	{
		hostServer.sendMessage("We had trouble finding a song!");
	}

	@Override
	public void loadFailed(FriendlyException exception) 
	{
		hostServer.sendMessage("Failed to load song: " + exception.getMessage());
	}

}
