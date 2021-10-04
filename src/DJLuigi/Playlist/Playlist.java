package DJLuigi.Playlist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import DJLuigi.DJ;
import DJLuigi.IO.DirectoryManager;
import DJLuigi.Interaction.List.ReactionListable;
import DJLuigi.Server.Server;
import DJLuigi.Server.ServerSettings;
import DJLuigi.utils.commandUtils;
import net.dv8tion.jda.api.entities.Member;

public class Playlist implements ReactionListable
{
	
	@JsonProperty("name") public String name;
	@JsonProperty("creatorID") public String creatorID;
	
	@JsonProperty("editPermissions") public int editPermissions = PlaylistEditPermissions.EDIT_EVERYONE;
	
	@JsonProperty("serverDependent") public boolean serverDependent = true;
	@JsonProperty("homeServerID") public String homeServerID;

	@JsonProperty("editors") public ArrayList<String> editors = new ArrayList<String>();
	
	@JsonProperty("songs") public ArrayList<PlaylistEntry> songs = new ArrayList<PlaylistEntry>();
	
	@JsonProperty("deleted") public boolean deleted = false;
	
	
	public Playlist(String name, String creatorID, String homeServerID) throws JsonGenerationException, JsonMappingException, IOException
	{
		this.name = name;
		this.creatorID = creatorID;
		this.homeServerID = homeServerID;
		
		SavePlaylist();
	}
	
	// DO NOT USE, FOR JACKSON ONLY 
	@Deprecated 
	public Playlist()
	{
		
	}
	
	public void addSong(PlaylistEntry song) throws JsonGenerationException, JsonMappingException, IOException
	{
		songs.add(song);
		
		SavePlaylist();
	}
	
	public boolean removeSong(String song)
	{
		return songs.remove(song);
	}
	
	public PlaylistEntry removeSong(int songIndex)
	{
		return songs.remove(songIndex);
	}
	
	// Returns if the specified member can edit the playlist
	public boolean memberCanEdit(Member m)
	{
		if ((editPermissions & PlaylistEditPermissions.EDIT_EVERYONE) > 0)
		{
			return true;
		}
		
		if ((editPermissions & PlaylistEditPermissions.EDIT_EDITORS) > 0)
		{
			if (editors.contains(m.getUser().getId()))
			{
				return true;
			}
		}
		
		if ((editPermissions & PlaylistEditPermissions.EDIT_OWNER) > 0)
		{
			if (m.getUser().getId() == creatorID)
			{
				return true;
			}
		}
		
		if ((editPermissions & PlaylistEditPermissions.EDIT_DJ) > 0)
		{
			if (commandUtils.isMemberDJ(m))
			{
				return true;
			}
		}
		
		return false;
	}
	
	private void SavePlaylist() throws JsonGenerationException, JsonMappingException, IOException
	{
		DirectoryManager.jsonMapper.writeValue(new File(DirectoryManager.playlistsDirectory, name + ".json"), this);
	}
	
	public static Playlist LoadPlaylist(File f) throws JsonParseException, JsonMappingException, IOException
	{
		return DirectoryManager.jsonMapper.readValue(f, Playlist.class);
	}
	
	// Pseudo delete. Sets the playlist to an inactive state where it cannot be played
	public void remove() throws JsonGenerationException, JsonMappingException, IOException
	{
		deleted = true;
		SavePlaylist();
	}
	
	// THIS WILL DELETE THE PLAYLIST FROM THE HARDDRIVE
	// Returns if it was successfully deleted
	public boolean permaDelete()
	{
		File playlistFile = new File(DirectoryManager.playlistsDirectory, name + ".json");
		
		return playlistFile.delete();
	}

	@Override
	public String getName() 
	{
		return name;
	}

	@Override
	public String getValue(int index) 
	{
		PlaylistEntry entry = songs.get(index);
		
		return (index + 1) + ". [**" + entry.name + "**](" + entry.uri + ")";
	}

	@Override
	public int size() 
	{
		return songs.size();
	}

	@Override
	public int itemsPerPage() 
	{
		return 10;
	}
	
}
