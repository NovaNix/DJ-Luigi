package DJLuigi.Playlist;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class PlaylistEntry 
{

	@JsonProperty("name") public String name;
	@JsonProperty("uri") public String uri;
	@JsonProperty("author") public String author;
	
	public PlaylistEntry()
	{
		
	}
	
	public PlaylistEntry(AudioTrack track)
	{
		name = track.getInfo().title;
		uri = track.getInfo().uri;
		author = track.getInfo().author;
	}

	// Autogenerated
	@Override
	public int hashCode() 
	{
		return Objects.hash(author, name, uri);
	}

	// The majority is autogenerated. Will also return true if it is a string matching the name or uri of the entry
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof PlaylistEntry) && !(obj instanceof String))
			return false;
		
		if (obj instanceof String)
		{
			String value = (String) obj;
			
			return value.equalsIgnoreCase(name) || value.equalsIgnoreCase(uri);
		}
		
		PlaylistEntry other = (PlaylistEntry) obj;
		return Objects.equals(author, other.author) && Objects.equals(name, other.name)
				&& Objects.equals(uri, other.uri);
	}
	
}
