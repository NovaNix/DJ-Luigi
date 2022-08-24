package DJLuigi.Audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import DJLuigi.DJ;
import DJLuigi.Interaction.List.ReactionListable;
import DJLuigi.Server.Server;

public class TrackScheduler extends AudioEventAdapter implements ReactionListable
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
		hostServer.SendMessage("Now playing `" + track.getInfo().title + "`");
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
				hostServer.LeaveVC();
			}
		}
	}

	@Override
	public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) 
	{
		// An already playing track threw an exception (track end event will still be
		// received separately)
		
		hostServer.SendMessage("Something went wrong while playing `" + track.getInfo().title + "`: `" + exception.getMessage() + "`");
		System.err.println("Something broke while playing a track!");
		exception.printStackTrace();
	}

	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) 
	{
		// Audio track has been unable to provide us any audio, might want to just start
		// a new track
		hostServer.SendMessage("I think I'm stuck... I'm going to skip `" + track.getInfo().title + "`");
		queue.skip();
	}

	@Override
	public String getValue(int index) 
	{	
		Song song = queue.songs.get(index);
		
		return (index + 1) + ". [**" + song.name + "**](" + song.uri + ")";
	}

	@Override
	public int size() 
	{
		return queue.size();
	}

	@Override
	public int itemsPerPage() 
	{
		return 10;
	}

	@Override
	public String getName() 
	{	
		return "Queue" + (queue.looped ? " (Looped)" : "");
	}
	
}
