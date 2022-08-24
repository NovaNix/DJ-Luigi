package DJLuigi.Commands.Meta;

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
	parameters = {
		//@Parameter(name = "command", description = "The command the help menu should be shown for", type = OptionType.STRING, required = false)	
	},
	ownerOnly = true,
	category = CommandCategory.Settings
)
// TODO Combine clear settings and settings into a single command
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
				listSettings(S, event);
				break;
			case "set":
				break;
			case "get":
				executeGetCommand(S, event);
				break;
			case "reset":
				break;
		}
		
//		System.out.println("Settings command activated");
//		System.out.println("Parameters: " + Parameters.size());
//		switch (Parameters.size())
//		{
//		case 0:
//			listSettings(S);
//			break;
//		case 1:
//			
//			String setting = Parameters.get(0);
//			
//			if (S.data.settings.hasSetting(setting))
//			{
//				BotSetting settingData = S.data.settings.getSetting(setting).getAnnotation(BotSetting.class);
//				
//				S.SendMessage(setting + ": " + settingData.description() + "\n"
//						+ "Current value: `" + getSettingValue(S, setting) + "`");
//			}
//			
//			else 
//			{
//				S.SendMessage("Invalid setting: " + setting);
//			}
//			
//			break;
//		case 2:
//			
//			String changedSetting = Parameters.get(0);
//			String newValue = Parameters.get(1);
//			
//			if (!S.data.settings.hasSetting(changedSetting))
//			{
//				S.SendMessage("Invalid setting: " + changedSetting);
//				return;
//			}
//			
//			Field f = S.data.settings.getSetting(changedSetting);
//			BotSetting settingData = f.getAnnotation(BotSetting.class);
//			
//			try {
//			
//				switch (settingData.type()) 
//				{
//					case Boolean:
//						f.setBoolean(S.data.settings, Boolean.parseBoolean(newValue));
//						break;
//					case Float:
//						f.setFloat(S.data.settings, Float.parseFloat(newValue));
//						break;
//					case Int:
//						f.setInt(S.data.settings, Integer.parseInt(newValue));
//						break;
//					case String:
//						f.set(S.data.settings, newValue);
//						break;
//				}
//				
//				try {
//					S.data.saveSettings();
//					S.SendMessage("Set setting " + changedSetting + " to value `" + newValue + "`");
//				} catch (IOException e) {
//					e.printStackTrace();
//					System.err.println("Error saving data!");
//				}
//			
//			} catch (IllegalArgumentException e) {
//				e.printStackTrace();
//				S.SendMessage("You cant set this setting to " + newValue);
//				
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//				S.SendMessage("There was an error setting the value!");
//				S.SendMessage("Make sure you use the right type of value!");
//			} 
//			
//			break;
//		default:
//			S.SendMessage("Thats too many parameters!");
//			break;
//		}
		
	}
	
	private void executeSetCommand(Server S, SlashCommandInteractionEvent event)
	{
		
	}
	
	private void executeGetCommand(Server S, SlashCommandInteractionEvent event)
	{
		String setting = event.getOption("setting").getAsString();
		
		BotSetting settingData = S.data.settings.getSetting(setting).getAnnotation(BotSetting.class);
		
		event.getHook().sendMessage(setting + ": " + settingData.description() + "\n"
				+ "Current value: `" + getSettingValue(S, setting) + "`").queue();
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
	
	private void listSettings(Server S, SlashCommandInteractionEvent event)
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
		
		event.getHook().sendMessage(list.toString()).queue();
	}

}
