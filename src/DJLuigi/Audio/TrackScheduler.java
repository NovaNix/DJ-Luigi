package DJLuigi.Audio;

import java.util.ArrayList;

import javax.sound.midi.Track;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import DJLuigi.Server.Server;

public class TrackScheduler extends AudioEventAdapter
{
	private Server HostServer;
	
	public ArrayList<AudioTrack> Tracks = new ArrayList<AudioTrack>();
	
	public boolean Looped = false; // Whether the current song should be looped
	
	public TrackScheduler(Server HostServer) 
	{
		this.HostServer = HostServer;
	}

	public void queue(AudioTrack t)
	{
		Tracks.add(t);
		
		HostServer.player.startTrack(t, true); 
	}
	
	public void skip() 
	{
		Tracks.remove(0);
		
		if (Tracks.size() > 0)
		{
			HostServer.player.playTrack(Tracks.get(0));
		}
		
		else
		{
			HostServer.player.stopTrack();
		}
	}
	
	public void remove(int song)
	{	
		if (song != 0)
		{
			Tracks.remove(song);
		}
	}
	
	public void clearQueue()
	{
		Tracks.clear();
		HostServer.player.stopTrack();
	}
	
	@Override
	public void onPlayerPause(AudioPlayer player) 
	{
		HostServer.SendMessage("Player was paused");
		// Player was paused
	}

	@Override
	public void onPlayerResume(AudioPlayer player) 
	{
		HostServer.SendMessage("Player was resumed");
		// Player was resumed
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) 
	{
		// A track started playing
		HostServer.SendMessage("Now playing " + track.getInfo().title);
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) 
	{
		if (endReason.mayStartNext) 
		{
			// Start next track
			
			if (Looped)
			{
				// Dont remove the song because its going to be played again
				Tracks.get(0).setPosition(0);
				//Tracks.set(0, Tracks.get(0).makeClone());
				player.playTrack(Tracks.get(0));
			}
			
			else
			{
				Tracks.remove(0);
				
				if (Tracks.size() > 0)
				{
					player.playTrack(Tracks.get(0));
				}
			}
			
			
			
		}
		
		else
		{
			HostServer.LeaveVC();
		}
	}

	@Override
	public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) 
	{
		// An already playing track threw an exception (track end event will still be
		// received separately)
	}

	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) 
	{
		// Audio track has been unable to provide us any audio, might want to just start
		// a new track
	}
	
}
