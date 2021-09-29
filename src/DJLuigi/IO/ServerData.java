package DJLuigi.IO;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import DJLuigi.Server.ServerSettings;
import DJLuigi.utils.directoryUtils;

public class ServerData 
{

	private String ID;
	
	public ServerSettings settings;
	
	public File directory;
	public File settingsFile;
	
	public ServerData(String ID)
	{
		this.ID = ID;
		
		directory = new File(DirectoryManager.serversDirectory, ID); 
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
		directoryUtils.validateFolder(directory);
		
		if (!settingsFile.exists())
		{
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
