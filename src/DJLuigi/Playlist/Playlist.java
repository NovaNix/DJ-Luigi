package DJLuigi.Playlist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import DJLuigi.DJ;
import DJLuigi.Audio.Song;
import DJLuigi.IO.DirectoryManager;
import DJLuigi.utils.commandUtils;
import net.dv8tion.jda.api.entities.Member;

public class Playlist
{
	
	@JsonProperty("name") public String name;
	@JsonProperty("creatorID") public String creatorID;
	
	@JsonProperty("editPermissions") public int editPermissions = PlaylistEditPermissions.EDIT_EVERYONE;
	
	@JsonProperty("isPublic") public boolean isPublic = false;
	@JsonProperty("allowedServers") public ArrayList<String> allowedServers = new ArrayList<String>();

	@JsonProperty("editors") public ArrayList<String> editors = new ArrayList<String>();
	
	@JsonProperty("songs") public ArrayList<Song> songs = new ArrayList<Song>();
	
	@JsonProperty("deleted") public boolean deleted = false;
	
	
	public Playlist(String name, String creatorID, String createdServer) throws JsonGenerationException, JsonMappingException, IOException
	{
		this.name = name;
		this.creatorID = creatorID;
		this.allowedServers.add(createdServer);
		
		SavePlaylist();
	}
	
	// DO NOT USE, FOR JACKSON ONLY 
	@Deprecated 
	public Playlist()
	{
		
	}
	
	public void addSong(Song song) throws JsonGenerationException, JsonMappingException, IOException
	{
		songs.add(song);
		
		SavePlaylist();
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public boolean removeSong(String song)
	{
		return songs.remove(song);
	}
	
	public Song removeSong(int songIndex)
	{
		return songs.remove(songIndex);
	}
	
	public void addAllowedServer(String serverID)
	{
		allowedServers.add(serverID);
	}
	
	public void removeAllowedServer(String serverID)
	{
		allowedServers.remove(serverID);
	}
	
	public boolean isAllowedServer(String serverID)
	{
		if (isPublic)
		{
			return true;
		}
		
		else
		{
			return allowedServers.contains(serverID);
		}
		
	}
	
	public String getCreatorName()
	{
		return DJ.jda.getUserById(creatorID).getName();
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
	
	public int size()
	{
		return songs.size();
	}
	
	private void SavePlaylist() throws JsonGenerationException, JsonMappingException, IOException
	{
		DirectoryManager.jsonMapper.writeValue(new File(DirectoryManager.getUserPlaylistDirectory(creatorID), name + ".json"), this);
	}
	
	public static Playlist LoadPlaylist(File f) throws JsonParseException, JsonMappingException, IOException
	{
		return DirectoryManager.jsonMapper.readValue(f, Playlist.class);
	}
	
	public String toJSON() throws JsonProcessingException
	{
		return DirectoryManager.jsonMapper.writeValueAsString(this);
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
		File playlistFile = new File(DirectoryManager.getUserPlaylistDirectory(creatorID), name + ".json");
		
		return playlistFile.delete();
	}
	
}
