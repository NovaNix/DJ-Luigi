package djLuigi.playlist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import djLuigi.server.Server;
import djLuigi.io.DirectoryManager;

public class PlaylistManager 
{
	private static final Logger logger = LoggerFactory.getLogger(PlaylistManager.class);
	
	public static ArrayList<Playlist> playlists;
	
	public static HashMap<String, Playlist> playlistMap;
	public static HashMap<String, ArrayList<Playlist>> playlistByNameMap = new HashMap<String, ArrayList<Playlist>>();
	
	public static void init()
	{
		logger.info("Loading playlists...");
		
		DirectoryManager.initPlaylistDirectory();
		
		reloadPlaylists();
		
		logger.info("Loaded " + playlists.size() + " playlist" + (playlists.size() != 1 ? "s" : ""));
	}
	
	public static void reloadPlaylists()
	{
		playlists = new ArrayList<Playlist>();
		playlistMap = new HashMap<String, Playlist>();
		
		File playlistDirectory = DirectoryManager.playlistsDirectory;
		
		for (File userDirectory : playlistDirectory.listFiles())
		{
			for (File f : userDirectory.listFiles())
			{
				try {
					Playlist p = Playlist.LoadPlaylist(f);
					
					if (!p.deleted)
					{
						addPlaylist(p);
					}
					
					else
					{
						logger.info("Found deleted playlist: " + p.name + ". Ignoring.");
					}
					
				} catch (IOException e) {
					logger.error("There was an error loading playlist: \"" + f.getName() + "\" (" + f.getPath() + ")");
					e.printStackTrace();
				}
			}
			
			
		}
	}
	
	// Gets all of the playlists that the server can access
	public static ArrayList<Playlist> getPlaylists(Server s)
	{
		ArrayList<Playlist> serversPlaylists = new ArrayList<Playlist>();
		
		String hostID = s.getId();
		
		for (int i = 0; i < playlists.size(); i++)
		{
			if (playlists.get(i).isAllowedServer(hostID))
			{
				serversPlaylists.add(playlists.get(i));
			}
		}
		
		return serversPlaylists;
	}
	
	public static void addPlaylist(Playlist p)
	{
		playlistMap.put(p.getUniqueName(), p);
		
		playlists.add(p);
		
		if (playlistByNameMap.get(p.name) == null)
		{
			playlistByNameMap.put(p.name, new ArrayList<Playlist>());
		}
		
		playlistByNameMap.get(p.name).add(p);
	}
	
	// Removes the specified playlist from the playlist list
	// Do not confuse with deletePlaylist()
	public static void removePlaylist(String name)
	{	
		Playlist removing = getPlaylist(name.toLowerCase());
		
		playlistByNameMap.get(removing.name).remove(removing);
		
		playlistMap.remove(removing.getUniqueName());
		playlists.remove(removing);
	}
	
	public static Playlist getPlaylist(String name)
	{
		Playlist p = playlistMap.get(name.toLowerCase());
		
		if (p == null)
		{
			if (playlistByNameMap.get(name.toLowerCase()).size() == 1)
			{
				p = playlistByNameMap.get(name.toLowerCase()).get(0);
			}
		}
		
		return p;
	}
	
	// Pseudo deletes the playlist
	// Returns if the playlist was successfully deleted
	public static boolean deletePlaylist(String name) throws IOException
	{
		if (hasPlaylist(name))
		{
			Playlist deleting = getPlaylist(name);
			
			deleting.remove();
			removePlaylist(name);
			return true;
		}
		
		else
		{
			return false;
		}
	}
	
	// Returns a unique 4 digit hexidecimal playlist id
	// TODO optimize for when there are a lot of playlists
	public static String getUniquePlaylistId(String name)
	{
		for (int i = 0; ; i++)
		{
			String hexCode = Integer.toHexString(i);
			
			if (!hasPlaylist(name + "#" + hexCode))
			{
				return hexCode;
			}
		}
	}
	
	public static boolean hasPlaylist(String name)
	{
		return playlistMap.containsKey(name.toLowerCase()) || playlistByNameMap.get(name.toLowerCase()) != null;
	}
	
	public static boolean hasDuplicatePlaylistName(String name)
	{
		ArrayList<Playlist> playlists = playlistByNameMap.get(name.toLowerCase());
		
		if (playlists == null)
		{
			return false;
		}
		
		return playlists.size() > 1;
	}
	
	public static int getTotalPlaylistCount()
	{
		return playlists.size();
	}
}
