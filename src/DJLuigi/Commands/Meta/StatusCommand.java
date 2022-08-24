package DJLuigi.Commands.Meta;

import DJLuigi.DJ;
import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Playlist.PlaylistManager;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@CommandData
(
	command = "status", 
	description = "Tells information about the bot",
	aliases = {"about", "info"},
	category = CommandCategory.Other
)
public class StatusCommand extends Command
{

	@Override
	public void executeCommand(Server S, SlashCommandInteractionEvent event) 
	{
		MessageEmbed e = new EmbedBuilder()
			    .setTitle("Status")
			    .setColor(DJ.getPrimaryColor())
			    .addField("Joined Servers", Integer.toString(DJ.getJoinedServersCount()), true)
			    .addField("Loaded Servers", Integer.toString(DJ.getLoadedServersCount()), true)
			    .addField("Total Playlists", Integer.toString(PlaylistManager.getTotalPlaylistCount()), false)
			    .build();
		
		event.replyEmbeds(e).queue();
	}

}
