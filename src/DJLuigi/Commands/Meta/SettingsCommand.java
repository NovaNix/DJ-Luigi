package DJLuigi.Commands.Meta;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.IO.BotSetting;
import DJLuigi.Server.Server;
import DJLuigi.Server.ServerSettings;
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
	public void executeCommand(Server S, SlashCommandInteractionEvent event) 
	{
		//event.reply("Sorry, this command is broken right now. Come back later").queue();
		
		event.deferReply().queue();
		
		switch (event.getSubcommandName())
		{
			case "list":
				executeListCommand(S, event);
				break;
			case "set":
				executeSetCommand(S, event);
				break;
			case "get":
				executeGetCommand(S, event);
				break;
			case "reset":
				executeResetCommand(S, event);
				break;
			default:
				event.getHook().sendMessage("Something went wrong! Contact a developer!").queue();
				System.err.println("Failed to find subcommand " + event.getSubcommandName() + " in SettingsCommand!");
				break;
		}
		
	}
	
	private void executeListCommand(Server S, SlashCommandInteractionEvent event)
	{
		ArrayList<Field> settings = S.data.settings.getSettings();
		
		StringBuilder list = new StringBuilder();
		
		list.append("There are " + settings.size() + " settings:\n```yml\n");
		
		for (int i = 0; i < settings.size(); i++)
		{
			BotSetting botSetting = settings.get(i).getAnnotation(BotSetting.class);
			
			try {
				list.append(settings.get(i).getName() + ": " + botSetting.description() + "\n");
				list.append("\tSetting type: " + settings.get(i).getType().getSimpleName() + "\n");
				list.append("\tCurrent value: " + getSettingValue(S, settings.get(i).getName()));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			
			list.append("\n");
		}
		
		list.append("```");
		
		event.getHook().sendMessage(list.toString()).queue();
	}
	
	private void executeSetCommand(Server S, SlashCommandInteractionEvent event)
	{
		String settingName = event.getOption("setting").getAsString();
		String value = event.getOption("value").getAsString();
		
		ServerSettings settings = S.data.settings;
		
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
		
		event.getHook().sendMessage(String.format("Set setting `%s` to value `%s`.", settingName, getSettingValue(S, settingName))).queue();
		
		try {
			S.data.saveSettings();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Error saving server data!");
			S.SendMessage("WARNING! THERE WAS A PROBLEM SAVING SETTING CHANGES. PLEASE NOTIFY A DEVELOPER IMMEDIETLY!");
		}

	}
	
	private void executeGetCommand(Server S, SlashCommandInteractionEvent event)
	{
		String setting = event.getOption("setting").getAsString();
		
		BotSetting settingData = S.data.settings.getSetting(setting).getAnnotation(BotSetting.class);
		
		event.getHook().sendMessage(setting + ": " + settingData.description() + "\n"
				+ "Current value: `" + getSettingValue(S, setting) + "`").queue();
	}
	
	// TODO ask for confirmation
	private void executeResetCommand(Server S, SlashCommandInteractionEvent event)
	{
		ServerSettings defaultSettings = new ServerSettings();
		
		if (event.getOption("setting") == null)
		{
			// Clear all the settings
			
			S.data.settings = defaultSettings;
			
			try {
				S.data.saveSettings();
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
			
			Field oldSetting = S.data.settings.getSetting(settingName);
			Field newSetting = defaultSettings.getSetting(settingName);
			
			try
			{
				oldSetting.set(S.data.settings, newSetting.get(defaultSettings));
			} catch (IllegalArgumentException | IllegalAccessException e1)
			{
				event.getHook().sendMessage("Something went wrong while changing the setting!").queue();
				e1.printStackTrace();
				return;
			}
			
			event.getHook().sendMessage(String.format("Reset setting `%s` to the default value (`%s`).", settingName, getSettingValue(S, settingName))).queue();
			
			try {
				S.data.saveSettings();
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
	
	private String getSettingValue(Server S, String setting)
	{
		try {
			return S.data.settings.getSetting(setting).get(S.data.settings).toString();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return "ERROR!";
		}
	}
	
	

}
