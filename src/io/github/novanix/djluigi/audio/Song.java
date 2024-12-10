package io.github.novanix.djluigi.audio;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import io.github.novanix.djluigi.io.DirectoryManager;
import io.github.novanix.djluigi.utils.DiscordUtils;

public class Song
{
	@JsonProperty("name") public final String name;
	@JsonProperty("uri") public final String uri;
	@JsonProperty("author") public final String author;
	@JsonProperty("length") public final long length;
	
	public AudioTrack track;
	
	@JsonCreator
	public Song(
			@JsonProperty("name") String name, 
			@JsonProperty("uri") String uri, 
			@JsonProperty("author") String author, 
			@JsonProperty("length") long length)
	{
		this.name = name;
		this.uri = uri;
		this.author = author;
		this.length = length;
	}
	
	public Song(AudioTrack track)
	{
		this.track = track;
		
		this.name = track.getInfo().title;
		this.uri = track.getInfo().uri;
		this.author = track.getInfo().author;
		this.length = track.getDuration();
	}
	
	public boolean isPlayable()
	{
		return track != null;
	}
	
	public String toJSON() throws JsonProcessingException
	{
		return DirectoryManager.jsonMapper.writeValueAsString(this);
	}
	
	public String getLengthString()
	{
		return DiscordUtils.getLengthString(length);
	}
	
	// Returns the song as a string formatted to be placed into a song list
	public String getQueueEntryString()
	{
		return String.format("[**%s**](%s)", name, uri);
	}
	
	// Autogenerated
	@Override
	public int hashCode() 
	{
		return Objects.hash(author, name, uri, length);
	}

	// The majority is autogenerated. Will also return true if it is a string matching the name or uri of the entry
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Song) && !(obj instanceof String))
			return false;
		
		if (obj instanceof String)
		{
			String value = (String) obj;
			
			return value.equalsIgnoreCase(name) || value.equalsIgnoreCase(uri);
		}
		
		Song other = (Song) obj;
		return Objects.equals(author, other.author) && Objects.equals(name, other.name)
				&& Objects.equals(uri, other.uri) && Objects.equals(length, other.length);
		}
}