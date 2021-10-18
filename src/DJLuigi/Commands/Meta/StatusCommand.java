package DJLuigi.Commands.Meta;

import java.awt.Color;
import java.util.ArrayList;

import DJLuigi.DJ;
import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Playlist.PlaylistManager;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

@CommandData
(
	command = "status", 
	description = "Tells information about the bot",
	category = CommandCategory.Other
)
public class StatusCommand implements Command
{

	@SuppressWarnings("deprecation")
	@Override
	public void executeCommand(Server S, ArrayList<String> Parameters, MessageReceivedEvent event) 
	{
		MessageEmbed e = new EmbedBuilder()
			    .setTitle("Status")
			    .setColor(new Color(12390624))
			    .addField("Joined Servers", Integer.toString(DJ.getJoinedServersCount()), true)
			    .addField("Loaded Servers", Integer.toString(DJ.getLoadedServersCount()), true)
			    .addField("Total Playlists", Integer.toString(PlaylistManager.getTotalPlaylistCount()), false)
			    .build();
		
		event.getChannel().sendMessage(e);
	}

}
