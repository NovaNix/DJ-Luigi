package djLuigi;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import djLuigi.io.DirectoryManager;

public class DJSettings 
{
	
	// TODO add command that saves the settings
	
	@JsonProperty("botToken") public String botToken = "";
	
	@JsonProperty("leaveOnQueueFinish") public boolean leaveOnQueueFinish = true;
	
	// The color used in embeds.
	// DJ Luigi Yellow is 15060541
	// DJ Waluigi Purple is 6971865
	@JsonProperty("botColor") public int botColor = 15060541;
	@JsonProperty("botIcon") public String botIcon = "https://github.com/NovaNix/DJ-Luigi/blob/main/DJ%20Luigi.jpg?raw=true";
	
	@JsonProperty("botStatus") public String botStatus = "Epic Tunes!";
	
	@JsonProperty("debugMode") public boolean debugMode = false;
	
	@JsonProperty("sendJoinMessage") public boolean sendJoinMessage = false;
	
	@JsonProperty("playlistsDirectory") public String playlistsDirectory = "";
	
	@JsonProperty("nonVAUsersWebInterfaceEdit") public boolean nonVAUsersWebInterfaceEdit = false;
	
	public void SaveSettings() throws JsonGenerationException, JsonMappingException, IOException
	{	
		DirectoryManager.yamlMapper.writeValue(DirectoryManager.configFile, this);
	}
	
}
