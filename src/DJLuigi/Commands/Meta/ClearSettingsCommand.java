package DJLuigi.Commands.Meta;

import java.io.IOException;
import java.util.ArrayList;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Interaction.ReactionConfirmation;
import DJLuigi.Server.Server;
import DJLuigi.Server.ServerSettings;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "clearsettings", 
	description = "Clears all of the settings",
	djOnly = true,
	ownerOnly = true,
	category = CommandCategory.Settings
)
public class ClearSettingsCommand implements Command
{

	@Override
	public void executeCommand(Server S, ArrayList<String> Parameters, MessageReceivedEvent event) 
	{
		new ReactionConfirmation("Do you really want to clear all of the settings? There's no going back...", event,
			() -> clearSettings(S),
			() -> S.SendMessage("Clear Settings Aborted"));
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
