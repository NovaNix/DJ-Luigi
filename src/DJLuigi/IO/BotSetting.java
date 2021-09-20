package DJLuigi.IO;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface BotSetting 
{

	public String description(); // A basic description of the setting
	public SettingType type(); // The type of variable the setting is
	
	// The possible types for a setting to be
	public static enum SettingType
	{
		String, Int, Float, Boolean
	}
}
