package DJLuigi.Server;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import DJLuigi.DJ;
import DJLuigi.IO.BotSetting;
import DJLuigi.IO.BotSetting.SettingType;
import DJLuigi.IO.DirectoryManager;

public class ServerSettings 
{
	// A lit of all of the setting annotations
	@JsonIgnore
	private HashMap<String, Field> settings = new HashMap<String, Field>();
	
	@JsonProperty("commandPrefix") 
	@BotSetting(description = "The prefix used before any command", type = SettingType.String)
	public String commandPrefix = "!";
	
	@JsonProperty("djOnlyMode") 
	@BotSetting(description = "Should only DJs be able to use commands", type = SettingType.Boolean)
	public boolean djOnlyMode = false;
	
	public ServerSettings()
	{
		commandPrefix = DJ.settings.defaultPrefix;
		
		for(Field field : this.getClass().getDeclaredFields())
		{
		    if (field.isAnnotationPresent(BotSetting.class))
		    {
		    	settings.put(field.getName(), field);
		    }
		}
	}
	
	public Field getSetting(String s)
	{
		return settings.get(s);
	}

	public boolean hasSetting(String s)
	{
		return settings.containsKey(s);
	}
	
	public ArrayList<Field> getSettings()
	{
		return new ArrayList<Field>(settings.values());
	}
	
	public void Save(File location) throws JsonGenerationException, JsonMappingException, IOException
	{
		DirectoryManager.mapper.writeValue(location, this);
		
		System.out.println("Saved!");
	}
	
}
