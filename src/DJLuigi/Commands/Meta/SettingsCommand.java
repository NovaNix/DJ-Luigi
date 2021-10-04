package DJLuigi.Commands.Meta;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.IO.BotSetting;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "settings", 
	description = "Used to see, read, and write settings",
	djOnly = true,
	category = CommandCategory.Settings
)
public class SettingsCommand implements Command
{

	@Override
	public void executeCommand(Server S, ArrayList<String> Parameters, MessageReceivedEvent event) 
	{
		System.out.println("Settings command activated");
		System.out.println("Parameters: " + Parameters.size());
		switch (Parameters.size())
		{
		case 0:
			listSettings(S);
			break;
		case 1:
			
			String setting = Parameters.get(0);
			
			if (S.data.settings.hasSetting(setting))
			{
				BotSetting settingData = S.data.settings.getSetting(setting).getAnnotation(BotSetting.class);
				
				S.SendMessage(setting + ": " + settingData.description() + "\n"
						+ "Current value: `" + getSettingValue(S, setting) + "`");
			}
			
			else 
			{
				S.SendMessage("Invalid setting: " + setting);
			}
			
			break;
		case 2:
			
			String changedSetting = Parameters.get(0);
			String newValue = Parameters.get(1);
			
			if (!S.data.settings.hasSetting(changedSetting))
			{
				S.SendMessage("Invalid setting: " + changedSetting);
				return;
			}
			
			Field f = S.data.settings.getSetting(changedSetting);
			BotSetting settingData = f.getAnnotation(BotSetting.class);
			
			try {
			
				switch (settingData.type()) 
				{
					case Boolean:
						f.setBoolean(S.data.settings, Boolean.parseBoolean(newValue));
						break;
					case Float:
						f.setFloat(S.data.settings, Float.parseFloat(newValue));
						break;
					case Int:
						f.setInt(S.data.settings, Integer.parseInt(newValue));
						break;
					case String:
						f.set(S.data.settings, newValue);
						break;
				}
				
				try {
					S.data.saveSettings();
					S.SendMessage("Set setting " + changedSetting + " to value `" + newValue + "`");
				} catch (IOException e) {
					e.printStackTrace();
					System.err.println("Error saving data!");
				}
			
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				S.SendMessage("You cant set this setting to " + newValue);
				
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				S.SendMessage("There was an error setting the value!");
				S.SendMessage("Make sure you use the right type of value!");
			} 
			
			break;
		default:
			S.SendMessage("Thats too many parameters!");
			break;
		}
		
	}
	
	
	
	private String getSettingValue(Server S, String setting)
	{
		try {
			return S.data.settings.getSetting(setting).get(S.data.settings).toString();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "ERROR!";
		}
	}
	
	private void listSettings(Server S)
	{
		ArrayList<Field> settings = S.data.settings.getSettings();
		
		StringBuilder list = new StringBuilder();
		
		list.append("There are " + settings.size() + " settings:\n```yml\n");
		
		for (int i = 0; i < settings.size(); i++)
		{
			BotSetting botSetting = settings.get(i).getAnnotation(BotSetting.class);
			
			try {
				list.append(settings.get(i).getName() + ": " + botSetting.description() + "\n");
				list.append("	Current value: " + getSettingValue(S, settings.get(i).getName()));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			
			list.append("\n");
		}
		
		list.append("```");
		
		S.SendMessage(list.toString());
	}

	
	
}
