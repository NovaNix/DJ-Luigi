package DJLuigi.Playlist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import DJLuigi.IO.DirectoryManager;
import DJLuigi.Server.Server;
import DJLuigi.utils.commandUtils;

public class PlaylistManager 
{

	public static ArrayList<Playlist> playlists;
	
	public static HashMap<String, Playlist> playlistMap;
	public static HashMap<String, ArrayList<Playlist>> playlistByNameMap = new HashMap<String, ArrayList<Playlist>>();
	
	public static void init()
	{
		System.out.println("Loading playlists...");
		
		DirectoryManager.initPlaylistDirectory();
		
		reloadPlaylists();
		
		System.out.println("Loaded " + playlists.size() + " playlist" + (playlists.size() != 1 ? "s" : ""));
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
						System.out.println("Found deleted playlist: " + p.name + ". Ignoring.");
					}
					
				} catch (IOException e) {
					System.err.println("There was an error loading playlist: \"" + f.getName() + "\" (" + f.getPath() + ")");
					e.printStackTrace();
				}
			}
			
			
		}
	}
	
	// Gets all of the playlists that the server can access
	public static ArrayList<Playlist> getPlaylists(Server s)
	{
		ArrayList<Playlist> serversPlaylists = new ArrayList<Playlist>();
		
		String hostID = s.guildID;
		
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
	public static boolean deletePlaylist(String name)
	{
		if (hasPlaylist(name))
		{
			Playlist deleting = getPlaylist(name);
			try {
				deleting.remove();
				removePlaylist(name);
				return true;
			} catch (IOException e) {
				System.err.println("Error deleting playlist " + name);
				e.printStackTrace();
				return false;
			}
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
			String hexCode = commandUtils.numberToHex(i);
			
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
	
	public static int getTotalPlaylistCount()
	{
		return playlists.size();
	}
}
