package DJLuigi.Audio;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.sound.midi.Track;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import DJLuigi.Interaction.List.ReactionListable;
import DJLuigi.Server.Server;

public class TrackScheduler extends AudioEventAdapter implements ReactionListable
{
	private Server HostServer;
	
	public ArrayList<AudioTrack> Tracks = new ArrayList<AudioTrack>();
	
	public boolean Looped = false; // Whether the current queue should be looped
	
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
		HostServer.SendMessage("Skipping song `" + Tracks.get(0).getInfo().title + "`...");
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
	
	// Shuffles the queue. Note: the first item in the queue will not be suffled because it is currently playing
	public void shuffle()
	{
		@SuppressWarnings("unchecked")
		ArrayList<AudioTrack> queue = (ArrayList<AudioTrack>) Tracks.clone();
		ArrayList<AudioTrack> shuffled = new ArrayList<AudioTrack>();
		
		shuffled.add(queue.remove(0));
		
		while (queue.size() > 0)
		{
			shuffled.add(queue.remove(ThreadLocalRandom.current().nextInt(queue.size())));
		}
		
		Tracks = shuffled;
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
		HostServer.SendMessage("Now playing `" + track.getInfo().title + "`");
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) 
	{
		if (endReason.mayStartNext) 
		{
			// Start next track
			
			if (Looped)
			{
				// Add the last song played to the end of the queue
				
				Tracks.add(track.makeClone());
			}
			
			Tracks.remove(0); // Remove the first song in the queue as to not play it again
			
			if (Tracks.size() > 0)
			{
				player.playTrack(Tracks.get(0));
			}
		}
		
		else if (endReason == AudioTrackEndReason.LOAD_FAILED)
		{
			Tracks.remove(0);
			
			if (Tracks.size() > 0)
			{
				player.playTrack(Tracks.get(0));
			}
		}
		
		else if (Tracks.size() == 0)
		{
			HostServer.LeaveVC();
		}
		
		
	}

	@Override
	public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) 
	{
		// An already playing track threw an exception (track end event will still be
		// received separately)
		
		HostServer.SendMessage("Something went wrong while playing `" + track.getInfo().title + "`: `" + exception.getMessage() + "`");
	}

	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) 
	{
		// Audio track has been unable to provide us any audio, might want to just start
		// a new track
		HostServer.SendMessage("I think I'm stuck... I'm going to skip `" + track.getInfo().title + "`");
		skip();
	}

	@Override
	public String getValue(int index) 
	{	
		AudioTrack song = Tracks.get(index);
		
		return (index + 1) + ". [**" + song.getInfo().title + "**](" + song.getInfo().uri + ")";
	}

	@Override
	public int size() 
	{
		return Tracks.size();
	}

	@Override
	public int itemsPerPage() 
	{
		return 10;
	}

	@Override
	public String getName() 
	{	
		return "Queue" + (Looped ? " (Looped)" : "");
	}
	
}
