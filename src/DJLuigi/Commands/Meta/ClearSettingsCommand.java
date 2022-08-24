package DJLuigi.Commands.Meta;

import java.io.IOException;
import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Server.Server;
import DJLuigi.Server.ServerSettings;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@CommandData
(
	command = "clearsettings", 
	description = "Clears all of the settings",
	djOnly = true,
	ownerOnly = true,
	category = CommandCategory.Settings
)
public class ClearSettingsCommand extends Command
{

	@Override
	public void executeCommand(Server S, SlashCommandInteractionEvent event) 
	{
		event.reply("This command is broken right now, come back later").queue();
		
//		new ReactionConfirmation("Do you really want to clear all of the settings? There's no going back...", event,
//			() -> clearSettings(S),
//			() -> S.SendMessage("Clear Settings Aborted"));
	}
	
	public void clearSettings(Server S)
	{
		S.data.settings = new ServerSettings();
		
		try {
			S.data.saveSettings();
			S.SendMessage("Settings have been reset! (Remember that the prefix has also been reset!)");
		} catch (IOException e) {
			S.SendMessage("Something went wrong!");
			e.printStackTrace();
		}
	}

}
