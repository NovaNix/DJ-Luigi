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

import DJLuigi.IO.BotSetting;
import DJLuigi.IO.DirectoryManager;

// A container of all of the server settings that can be changed by server admins. 
public class ServerSettings 
{
	// A lit of all of the setting annotations
	@JsonIgnore
	private HashMap<String, Field> settings = new HashMap<String, Field>();
	
	@JsonProperty("djOnlyMode") 
	@BotSetting(description = "Should only DJs be able to use commands")
	public boolean djOnlyMode = false;
	
	public ServerSettings()
	{		
		for(Field field : this.getClass().getDeclaredFields())
		{
		    if (field.isAnnotationPresent(BotSetting.class))
		    {
		    	settings.put(field.getName(), field);
		    }
		}
	}
	
	public <H> void setValue(String settingName, H value)
	{
		Field setting = getSetting(settingName);
		
		// Verify that settingName is a valid setting
		if (setting == null)
			throw new IllegalArgumentException("There is no setting with the name " + setting);
		
		// Check to see if they are the same type. If not, the value is rejected
		//value.getClass().isInstance(setting.getType())
		if (!checkTypeMatch(value, setting))
			throw new IllegalArgumentException("Cannot set variable \"" + settingName + "\" of type " + setting.getType().getName() + " to type " + value.getClass().getName());

		try
		{
			setting.set(this, value);
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	// Checks to see if the value can be stored in the field
	private <H> boolean checkTypeMatch(H value, Field setting)
	{
		System.out.println("Comparing types " + value.getClass().getName() + " to " + setting.getType());
		
		if (!setting.getType().isPrimitive())
		{
			return setting.getType().isAssignableFrom(value.getClass());
		}
		
		else
		{
			// Handle primitive types
			switch (setting.getType().getSimpleName())
			{
				case "int":
					return value instanceof Integer;
				case "byte":
					return value instanceof Byte;
				case "short":
					return value instanceof Short;
				case "long":
					return value instanceof Long;
				case "float":
					return value instanceof Float;
				case "double":
					return value instanceof Double;
				case "boolean":
					return value instanceof Boolean;
				case "char":
					return value instanceof Character;
				default:
					System.err.println("I missed a primitive type? " + setting.getType().getName());
					return false;
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
		DirectoryManager.yamlMapper.writeValue(location, this);
		
		System.out.println("Saved Server Settings");
	}
	
}
