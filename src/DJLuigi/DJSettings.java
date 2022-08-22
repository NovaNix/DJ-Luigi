package DJLuigi;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import DJLuigi.IO.DirectoryManager;

public class DJSettings 
{
	
	@JsonProperty("botToken") public String botToken = "";
	@JsonProperty("defaultPrefix") public String defaultPrefix = "!";
	
	@JsonProperty("leaveOnQueueFinish") public boolean leaveOnQueueFinish = true;
	
	@JsonProperty("debugMode") public boolean debugMode = false;
	
	@JsonProperty("sendJoinMessage") public boolean sendJoinMessage = true;
	
	@JsonProperty("playlistsDirectory") public String playlistsDirectory = "";
	
	@JsonProperty("nonVAUsersWebInterfaceEdit") public boolean nonVAUsersWebInterfaceEdit = false;
	
	public void SaveSettings() throws JsonGenerationException, JsonMappingException, IOException
	{	
		DirectoryManager.yamlMapper.writeValue(DirectoryManager.configFile, this);
	}
	
}
