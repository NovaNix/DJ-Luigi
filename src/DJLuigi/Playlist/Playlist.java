package DJLuigi.Playlist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import DJLuigi.Server.Server;

public class Playlist 
{

	public String name;
	public Server host;
	
	public ArrayList<String> songs = new ArrayList<String>();
	public File playlistFile;
	
	// Creates the playlist object
	// If the playlist already exists, it will load the songs
	// If the playlist doesnt exist, it will create everything
	//	Throws IOException if it is unable to create the playlist file
	public Playlist(String name, Server host) throws IOException
	{
		this.name = name;
		this.host = host;
		
		playlistFile = new File(host.data.playlistsFile, name + ".txt");
		
		if (playlistFile.exists())
		{
			LoadPlaylist();
		}
		
		else
		{
			playlistFile.createNewFile();
		}
	}
	
	private void SavePlaylist()
	{
		
	}
	
	private void LoadPlaylist()
	{
		
	}
	
}
