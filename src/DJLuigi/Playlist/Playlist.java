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
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public class Playlist
{
	
	public static final int NAME_MAX_CHARS = 50;
	public static final int MAX_DESCRIPTION_CHARS = 100;
	
	@JsonProperty("name") public String name;
	@JsonProperty("displayName") public String displayName;
	@JsonProperty("description") public String description;
	@JsonProperty("id") public String id; // A 4 digit hex code that is appended to playlists with the same name
	@JsonProperty("creatorID") public String creatorID;
	
	@JsonProperty("editPermissions") public int editPermissions = PlaylistEditPermissions.EDIT_OWNER | PlaylistEditPermissions.EDIT_EDITORS | PlaylistEditPermissions.EDIT_DJ;
	
	@JsonProperty("isPublic") public boolean isPublic = false;
	@JsonProperty("allowedServers") public ArrayList<String> allowedServers = new ArrayList<String>();

	@JsonProperty("editors") public ArrayList<String> editors = new ArrayList<String>();
	
	@JsonProperty("songs") public ArrayList<Song> songs = new ArrayList<Song>();
	
	@JsonProperty("deleted") public boolean deleted = false;
	
	public Playlist(String name, String description, String creatorID, String createdServer) throws JsonGenerationException, JsonMappingException, IOException
	{
		this.name = name.toLowerCase();
		this.displayName = name;
		this.description = description;
		this.id = PlaylistManager.getUniquePlaylistId(name);
		this.creatorID = creatorID;
		this.allowedServers.add(createdServer);
		
		SavePlaylist();
	}
	
	// DO NOT USE, FOR JACKSON ONLY 
	@Deprecated 
	public Playlist()
	{
		
	}

	// Returns if a string is a valid playlist name. 
	// A playlist name is invalid if it contains any of the following characters
	// /
	public static boolean isValidName(String name)
	{
		return !name.contains("/");
	}
	
	public void addSong(Song song) throws JsonGenerationException, JsonMappingException, IOException
	{
		songs.add(song);
		
		SavePlaylist();
	}
	
	public Song getSong(int index)
	{
		return songs.get(index);
	}
	
	@SuppressWarnings("unlikely-arg-type")
	public boolean removeSong(String song) throws JsonGenerationException, JsonMappingException, IOException
	{
		boolean isRemoved = songs.remove(song);
		
		if (isRemoved)
		{
			SavePlaylist();
		}
		
		return isRemoved;
	}
	
	public Song removeSong(int songIndex) throws JsonGenerationException, JsonMappingException, IOException
	{
		Song removedSong = songs.remove(songIndex);
		
		if (removedSong != null)
		{
			SavePlaylist();
		}
		
		return removedSong;
	}
	
	public void addAllowedServer(String serverID) throws JsonGenerationException, JsonMappingException, IOException
	{
		allowedServers.add(serverID);
		
		SavePlaylist();
	}
	
	public void removeAllowedServer(String serverID) throws JsonGenerationException, JsonMappingException, IOException
	{
		allowedServers.remove(serverID);
		
		SavePlaylist();
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
	
	// Returns the name of the playlist combined with it's name unique ID
	public String getUniqueName()
	{
		return name + "#" + id;
	}
	
	public String getCreatorName()
	{
		return DJ.jda.retrieveUserById(creatorID).complete().getName();
	}
	
	public void addEditor(User u) throws JsonGenerationException, JsonMappingException, IOException
	{
		editors.add(u.getId());
		
		SavePlaylist();
	}
	
	public boolean isEditor(User u)
	{
		return editors.contains(u.getId());
	}
	
	public void removeEditor(User u) throws JsonGenerationException, JsonMappingException, IOException
	{
		editors.remove(u.getId());
		
		SavePlaylist();
	}
	
	// Returns if the specified member can edit the playlist
	public boolean memberCanEdit(Member m)
	{
		int userPermissionsBitmask = PlaylistEditPermissions.getUserPermissions(m, this);
		
		return (userPermissionsBitmask & editPermissions) != 0;
	}
	
	public int size()
	{
		return songs.size();
	}
	
	private void SavePlaylist() throws JsonGenerationException, JsonMappingException, IOException
	{
		DirectoryManager.jsonMapper.writeValue(new File(DirectoryManager.getUserPlaylistDirectory(creatorID), getUniqueName() + ".json"), this);
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
