package DJLuigi.IO;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import DJLuigi.DJSettings;
import DJLuigi.Server.Server;
import DJLuigi.Server.ServerSettings;
import DJLuigi.utils.directoryUtils;

public class DirectoryManager 
{

	public static ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
	
	public static File home;
	
	public static File configFile;
	public static File serversDirectory;
	
	public static void Init(String directory)
	{
		home = new File(directory);
		
		configFile = new File(home, "config.yml");
		serversDirectory = new File(home, "servers");
		
		try {
			validate();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Checks to see if all of the needed files exist, and if they don't it will create it
	// Doesnt handle individual server files however
	private static void validate() throws IOException
	{
		directoryUtils.validateFolder(home);
		directoryUtils.validateFolder(serversDirectory);
		
		if (!configFile.exists())
		{
			configFile.createNewFile();
			new DJSettings().SaveSettings();
		}
	}
	
	public static DJSettings loadDJConfig()
	{
		try {
			return mapper.readValue(configFile, DJSettings.class);
		} catch (IOException e) {
			e.printStackTrace();
			return new DJSettings();
		}
	}
	
	public static ServerSettings loadServerSettings(File settings)
	{
		try {
			return mapper.readValue(settings, ServerSettings.class);
		} catch (IOException e) {
			e.printStackTrace();
			return new ServerSettings();
		}
	}
	
}
