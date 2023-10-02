package io.github.novanix.djluigi.commands.meta;

import io.github.novanix.djluigi.DJ;
import io.github.novanix.djluigi.commands.Command;
import io.github.novanix.djluigi.commands.CommandCategory;
import io.github.novanix.djluigi.commands.CommandData;
import io.github.novanix.djluigi.playlist.PlaylistManager;
import io.github.novanix.djluigi.server.Server;
import io.github.novanix.djluigi.server.ServerHandler;
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
			    .addField("Loaded Servers", Integer.toString(ServerHandler.getLoadedServerCount()), true)
			    .addField("Total Playlists", Integer.toString(PlaylistManager.getTotalPlaylistCount()), false)
			    .build();
		
		event.replyEmbeds(e).addActionRow(
				Button.link("https://github.com/NovaNix/DJ-Luigi", "GitHub")
				).queue();
	}

}
