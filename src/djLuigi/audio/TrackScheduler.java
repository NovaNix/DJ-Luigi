package djLuigi.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import djLuigi.DJ;
import djLuigi.server.Server;

public class TrackScheduler extends AudioEventAdapter
{
	private Server hostServer;
	private Queue queue;
	
	public TrackScheduler(Server HostServer) 
	{
		this.hostServer = HostServer;
		this.queue = hostServer.queue;
	}
	
	@Override
	public void onPlayerPause(AudioPlayer player) 
	{
		//hostServer.SendMessage("Player was paused");
		// Player was paused
	}

	@Override
	public void onPlayerResume(AudioPlayer player) 
	{
		//hostServer.SendMessage("Player was resumed");
		// Player was resumed
	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) 
	{
		// A track started playing
		if (hostServer.data.settings.outputSongNameOnPlay)
			hostServer.sendMessage("Now playing `" + track.getInfo().title + "`");
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) 
	{
		if (endReason.mayStartNext) 
		{
			// Start next track
			
			if (queue.looped)
			{
				// Add the last song played to the end of the queue
				
				queue.add(track.makeClone());
			}
			
			queue.remove(0); // Remove the first song in the queue as to not play it again
			
			if (queue.size() > 0)
			{
				player.playTrack(queue.getTrack(0));
			}
		}
		
		else if (endReason == AudioTrackEndReason.LOAD_FAILED)
		{
			queue.songs.remove(0);
			
			if (queue.size() > 0)
			{
				player.playTrack(queue.getTrack(0));
			}
		}
		
		else if (queue.size() == 0)
		{
			if (DJ.settings.leaveOnQueueFinish)
			{
				hostServer.leaveVC();
			}
		}
	}

	@Override
	public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) 
	{
		// An already playing track threw an exception (track end event will still be
		// received separately)
		
		hostServer.sendMessage("Something went wrong while playing `" + track.getInfo().title + "`: `" + exception.getMessage() + "`");
		System.err.println("Something broke while playing a track!");
		exception.printStackTrace();
	}

	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) 
	{
		// Audio track has been unable to provide us any audio, might want to just start
		// a new track
		hostServer.sendMessage("I think I'm stuck... I'm going to skip `" + track.getInfo().title + "`");
		queue.skip();
	}
	
}
