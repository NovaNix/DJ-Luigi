package io.github.novanix.djluigi.io;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.github.novanix.djluigi.server.ServerSettings;
import io.github.novanix.djluigi.utils.DirectoryUtils;

public class ServerData 
{

	public final String id;
	
	public ServerSettings settings;
	
	public File directory;
	public File settingsFile;
	
	public ServerData(String id)
	{
		this.id = id;
		
		directory = new File(DirectoryManager.serversDirectory, id); 
		settingsFile = new File(directory, "config.yml");
		
		try {
			validate();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		settings = DirectoryManager.loadServerSettings(settingsFile);
	}
	
	// Verifies all of the files exist. If they don't exist then it will create them
	public void validate() throws JsonGenerationException, JsonMappingException, IOException
	{
		DirectoryUtils.validateFolder(directory);
		
		if (!settingsFile.exists())
		{
			System.out.println("Failed to find config file. Creating a new one.");
			settingsFile.createNewFile();
			new ServerSettings().Save(settingsFile);
		}
		
	}
	
	public void saveSettings() throws JsonGenerationException, JsonMappingException, IOException
	{
		settings.Save(settingsFile);
	}
	
	public void resetSettings()
	{
		settings = new ServerSettings();
		
		try {
			saveSettings();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
