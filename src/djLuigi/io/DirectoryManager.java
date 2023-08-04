package djLuigi.io;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import djLuigi.DJ;
import djLuigi.DJSettings;
import djLuigi.playlist.PlaylistManager;
import djLuigi.server.ServerSettings;
import djLuigi.utils.DirectoryUtils;

public class DirectoryManager 
{

	private static final Logger logger = LoggerFactory.getLogger(DirectoryManager.class);
	
	public static ObjectMapper jsonMapper = new ObjectMapper(new JsonFactory())
										.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
										.enable(SerializationFeature.INDENT_OUTPUT)
										.disable(
												MapperFeature.AUTO_DETECT_CREATORS,
												MapperFeature.AUTO_DETECT_FIELDS,
												MapperFeature.AUTO_DETECT_GETTERS,
												MapperFeature.AUTO_DETECT_IS_GETTERS);;
	public static ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory())
										.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	
	public static File home;
	
	public static File configFile;
	public static File serversDirectory;
	public static File playlistsDirectory;
	
	public static void init(String directory)
	{
		home = new File(directory);
		
		configFile = new File(home, "config.yml");
		serversDirectory = new File(home, "servers");
		
		try {
			validate();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// As the playlist directory is determined by the bot config, it must be loaded afterwards
	public static void initPlaylistDirectory()
	{
		//Check to see if the playlist directory has been defined
		if (DJ.settings.playlistsDirectory.equals("")) 
		{
			logger.error("WARNING: PLAYLIST DIRECTORY HAS NOT BEEN DEFINED. DEFAULTING TO \"/playlists\" IN THE HOME DIRECTORY");
			logger.error("THIS MEANS PLAYLISTS WILL NOT WORK BETWEEN MULTIPLE BOTS");
			playlistsDirectory = new File(home, "playlists");
		}
		
		else
		{
			playlistsDirectory = new File(DJ.settings.playlistsDirectory);
		}
		
		DirectoryUtils.validateFolder(playlistsDirectory);
	}
	
	public static File getUserPlaylistDirectory(String userID)
	{
		File userDirectory = new File(playlistsDirectory, userID);
		userDirectory.mkdirs();
		
		return userDirectory;
	}
	
	// Checks to see if all of the needed files exist, and if they don't it will create it
	// Doesnt handle individual server files however
	private static void validate() throws IOException
	{
		DirectoryUtils.validateFolder(home);
		DirectoryUtils.validateFolder(serversDirectory);
		
		if (!configFile.exists())
		{
			configFile.createNewFile();
			new DJSettings().SaveSettings();
		}
	}
	
	public static DJSettings loadDJConfig()
	{
		try {
			return yamlMapper.readValue(configFile, DJSettings.class);
		} catch (IOException e) {
			e.printStackTrace();
			return new DJSettings();
		}
	}
	
	public static ServerSettings loadServerSettings(File settings)
	{
		try {
			return yamlMapper.readValue(settings, ServerSettings.class);
		} catch (IOException e) {
			e.printStackTrace();
			return new ServerSettings();
		}
	}
	
}
