package djLuigi.audio;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import djLuigi.server.Server;
import djLuigi.io.DirectoryManager;

// A representation of the song queue in a specific server
// The queue is separated from the TrackScheduler to make it easier to implement a web interface in the future by having an easily serializable queue 
// TODO consider adding an event that is triggered when the queue changes
public class Queue
{
	@JsonIgnore
	Server hostServer;
	
	@JsonProperty("songs") public ArrayList<Song> songs = new ArrayList<Song>();
	
	// Whether the current queue should be looped
	@JsonProperty("looped") public boolean looped = false; 
	
	public Queue(Server host)
	{
		this.hostServer = host;
	}
	
	public void add(Song song)
	{
		if (song.isPlayable())
		{
			songs.add(song);
			
			hostServer.player.startTrack(song.track, true); 
		}
		
		else
		{
			System.err.println("Tried to add an unplayable song to the queue!");
		}
	}
	
	public void add(AudioTrack track)
	{
		add(new Song(track));
	}
	
	public Song get(int index)
	{
		return songs.get(index);
	}
	
	public AudioTrack getTrack(int index)
	{
		return songs.get(index).track;
	}
	
	// Skips the current song. 
	// Returns the skipped song
	public Song skip() 
	{
		Song removed = songs.remove(0);
		
		if (size() > 0)
		{
			hostServer.player.playTrack(songs.get(0).track);
		}
		
		else
		{
			hostServer.player.stopTrack();
		}
		
		return removed;
	}
	
	// Sets if the queue should be looped
	public void setLoop(boolean loop)
	{
		looped = loop;
	}
	
	// Toggles if the queue is currently looped. 
	// Returns the new value of looped
	public boolean toggleLoop()
	{
		looped = !looped;
		return looped;
	}
	
	// Removes the song at the specified index
	// Returns the removed song
	public Song remove(int index)
	{
		return songs.remove(index);
	}
	
	// Shuffles the queue. Note: the first item in the queue will not be suffled because it is currently playing
	public void shuffle()
	{
		@SuppressWarnings("unchecked")
		ArrayList<Song> queue = (ArrayList<Song>) songs.clone();
		ArrayList<Song> shuffled = new ArrayList<Song>();
			
		shuffled.add(queue.remove(0));
			
		while (queue.size() > 0)
		{
			shuffled.add(queue.remove(ThreadLocalRandom.current().nextInt(queue.size())));
		}
			
		songs = shuffled;
	}
	
	public void clear()
	{
		songs.clear();
		hostServer.player.stopTrack();
	}
	
	public int size()
	{
		return songs.size();
	}
	
	public String toJSON() throws JsonProcessingException
	{
		return DirectoryManager.jsonMapper.writeValueAsString(this);
	}
}
