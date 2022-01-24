package DJLuigi.Audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import DJLuigi.Server.Server;

public class LoadResultHandler implements AudioLoadResultHandler
{

	private Server HostServer;
	
	public LoadResultHandler(Server HostServer)
	{
		this.HostServer = HostServer;
	}
	
	@Override
	public void trackLoaded(AudioTrack track) 
	{
		HostServer.trackScheduler.queue(track);
		
		HostServer.SendMessage("Added " + track.getInfo().title);
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) 
	{
		if (playlist.isSearchResult())
		{
			HostServer.trackScheduler.queue(playlist.getTracks().get(0));
			HostServer.SendMessage("Added " + playlist.getTracks().get(0).getInfo().title);
		}
		
		else
		{
			for (AudioTrack track : playlist.getTracks()) 
			{
				HostServer.trackScheduler.queue(track);
			}
			
			HostServer.SendMessage("Added " + playlist.getTracks().size() + " Songs");
		}
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
