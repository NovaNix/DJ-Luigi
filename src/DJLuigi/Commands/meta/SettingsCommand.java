package djLuigi.commands.meta;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import djLuigi.server.Server;
import djLuigi.server.ServerSettings;
import djLuigi.commands.Command;
import djLuigi.commands.CommandCategory;
import djLuigi.commands.CommandData;
import djLuigi.io.BotSetting;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

@CommandData
(
	command = "settings", 
	description = "Used to see, read, and write settings",
	// PARAMETERS ARE NOT STORED HERE! THEY ARE GENERATED DUE TO THE EXISTANCE OF SUBCOMMANDS!
	ownerOnly = true,
	category = CommandCategory.Settings
)
public class SettingsCommand extends Command
{

	@Override
	public void executeCommand(Server s, SlashCommandInteractionEvent event) 
	{
		
		event.deferReply().queue();
		
		switch (event.getSubcommandName())
		{
			case "list":
				executeListCommand(s, event);
				break;
			case "set":
				executeSetCommand(s, event);
				break;
			case "get":
				executeGetCommand(s, event);
				break;
			case "reset":
				executeResetCommand(s, event);
				break;
			default:
				event.getHook().sendMessage("Something went wrong! Contact a developer!").queue();
				System.err.println("Failed to find subcommand " + event.getSubcommandName() + " in SettingsCommand!");
				break;
		}
		
	}
	
	private void executeListCommand(Server s, SlashCommandInteractionEvent event)
	{
		ArrayList<Field> settings = s.data.settings.getSettings();
		
		StringBuilder list = new StringBuilder();
		
		list.append("There are " + settings.size() + " settings:\n```yml\n");
		
		for (int i = 0; i < settings.size(); i++)
		{
			BotSetting botSetting = settings.get(i).getAnnotation(BotSetting.class);
			
			try {
				list.append(settings.get(i).getName() + ": " + botSetting.description() + "\n");
				list.append("\tSetting type: " + settings.get(i).getType().getSimpleName() + "\n");
				list.append("\tCurrent value: " + getSettingValue(s, settings.get(i).getName()));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			
			list.append("\n");
		}
		
		list.append("```");
		
		event.getHook().sendMessage(list.toString()).queue();
	}
	
	private void executeSetCommand(Server s, SlashCommandInteractionEvent event)
	{
		String settingName = event.getOption("setting").getAsString();
		String value = event.getOption("value").getAsString();
		
		ServerSettings settings = s.data.settings;
		
		try {
			switch (settings.getSetting(settingName).getType().getSimpleName().toLowerCase())
			{
				case "string":
					settings.setValue(settingName, value);
					break;
				case "int":
					settings.setValue(settingName, Integer.parseInt(value));
					break;
				case "float":
					settings.setValue(settingName, Float.parseFloat(value));
					break;
				case "boolean":
					if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false"))
						throw new IllegalArgumentException();
					
					settings.setValue(settingName, Boolean.parseBoolean(value));
					break;
				default:
					break;
			}
		} 
		
		catch (IllegalArgumentException e) 
		{
			//e.printStackTrace();
			event.getHook().sendMessage(String.format("Invalid value for setting `%s` (Expected type %s, got value \"%s\")", settingName, settings.getSetting(settingName).getType().getSimpleName(), value)).queue();
			return;
		}
		
		event.getHook().sendMessage(String.format("Set setting `%s` to value `%s`.", settingName, getSettingValue(s, settingName))).queue();
		
		try {
			s.data.saveSettings();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error saving server data!");
			s.sendMessage("WARNING! THERE WAS A PROBLEM SAVING SETTING CHANGES. PLEASE NOTIFY A DEVELOPER IMMEDIETLY!");
		}

	}
	
	private void executeGetCommand(Server s, SlashCommandInteractionEvent event)
	{
		String setting = event.getOption("setting").getAsString();
		
		BotSetting settingData = s.data.settings.getSetting(setting).getAnnotation(BotSetting.class);
		
		event.getHook().sendMessage(setting + ": " + settingData.description() + "\n"
				+ "Current value: `" + getSettingValue(s, setting) + "`").queue();
	}
	
	// TODO ask for confirmation
	private void executeResetCommand(Server s, SlashCommandInteractionEvent event)
	{
		ServerSettings defaultSettings = new ServerSettings();
		
		if (event.getOption("setting") == null)
		{
			// Clear all the settings
			
			s.data.settings = defaultSettings;
			
			try {
				s.data.saveSettings();
				event.getHook().sendMessage("Settings have been reset!").queue();
			} catch (IOException e) {
				System.err.println("Error saving server data!");
				event.getHook().sendMessage("WARNING! THERE WAS A PROBLEM SAVING SETTING CHANGES. PLEASE NOTIFY A DEVELOPER IMMEDIETLY!").queue();
				e.printStackTrace();
			}
		}
		
		else
		{
			String settingName = event.getOption("setting").getAsString();
			
			Field oldSetting = s.data.settings.getSetting(settingName);
			Field newSetting = defaultSettings.getSetting(settingName);
			
			try
			{
				oldSetting.set(s.data.settings, newSetting.get(defaultSettings));
			} catch (IllegalArgumentException | IllegalAccessException e1)
			{
				event.getHook().sendMessage("Something went wrong while changing the setting!").queue();
				e1.printStackTrace();
				return;
			}
			
			event.getHook().sendMessage(String.format("Reset setting `%s` to the default value (`%s`).", settingName, getSettingValue(s, settingName))).queue();
			
			try {
				s.data.saveSettings();
			} catch (IOException e) {
				System.err.println("Error saving server data!");
				event.getHook().sendMessage("WARNING! THERE WAS A PROBLEM SAVING SETTING CHANGES. PLEASE NOTIFY A DEVELOPER IMMEDIETLY!").queue();
				e.printStackTrace();
			}
		}

	}
	
	// setSlashCommandParameters is overridden because the CommandData and Parameter annotations do not allow for subcommands (yet?)
	@Override
	protected void setSlashCommandParameters(SlashCommandData data)
	{
		SubcommandData listCommand = new SubcommandData("list", "Lists the values of all settings");
		SubcommandData setCommand = new SubcommandData("set", "Sets the value of a setting");
		SubcommandData getCommand = new SubcommandData("get", "Gets the value of a setting");
		SubcommandData resetCommand = new SubcommandData("reset", "Resets the value of one or all settings");
		
		// Create a blank ServerSetting class to generate the possible values for the set and get commands
		ServerSettings blank = new ServerSettings();
		
		ArrayList<Field> settings = blank.getSettings();
		
		// Add options for the set, get, and reset commands
		
		OptionData setCommandData = new OptionData(OptionType.STRING, "setting", "The setting that should be changed", true);
		OptionData getCommandData = new OptionData(OptionType.STRING, "setting", "The setting that should be viewed", true);
		OptionData resetCommandData = new OptionData(OptionType.STRING, "setting", "The setting that should be reset");
		
		for (Field setting : settings)
		{	
			setCommandData.addChoice(setting.getName(), setting.getName());
			getCommandData.addChoice(setting.getName(), setting.getName());
			resetCommandData.addChoice(setting.getName(), setting.getName());
		}
		
		setCommand.addOptions(setCommandData);
		getCommand.addOptions(getCommandData);
		resetCommand.addOptions(resetCommandData);
		
		// Add the last option to the set commands
		
		setCommand.addOption(OptionType.STRING, "value", "The value to set the setting to", true);
		
		// Add the subcommands
		data.addSubcommands(listCommand, setCommand, getCommand, resetCommand);
	}
	
	private String getSettingValue(Server s, String setting)
	{
		try {
			return s.data.settings.getSetting(setting).get(s.data.settings).toString();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return "ERROR!";
		}
	}
	
	

}
