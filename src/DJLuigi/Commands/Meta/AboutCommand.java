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
import net.dv8tion.jda.api.interactions.components.buttons.Button;

@CommandData
(
	command = "about", 
	description = "Tells information about the bot",
	global = true,
	aliases = {"status", "info"},
	category = CommandCategory.Other
)
public class AboutCommand extends Command
{

	@Override
	public void executeCommand(Server s, SlashCommandInteractionEvent event) 
	{
		MessageEmbed e = new EmbedBuilder()
			    .setTitle("About")
			    .setColor(DJ.getPrimaryColor())
			    .setImage(DJ.settings.botIcon)
			    .addField("Joined Servers", Integer.toString(DJ.getJoinedServersCount()), true)
			    .addField("Loaded Servers", Integer.toString(DJ.getLoadedServersCount()), true)
			    .addField("Total Playlists", Integer.toString(PlaylistManager.getTotalPlaylistCount()), false)
			    .build();
		
		event.replyEmbeds(e).addActionRow(
				Button.link("https://github.com/NovaNix/DJ-Luigi", "GitHub")
				).queue();
	}

}
