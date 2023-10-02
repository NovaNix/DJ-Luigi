package io.github.novanix.djluigi.commands.meta;

import io.github.novanix.djluigi.DJ;
import io.github.novanix.djluigi.commands.Command;
import io.github.novanix.djluigi.commands.CommandCategory;
import io.github.novanix.djluigi.commands.CommandData;
import io.github.novanix.djluigi.commands.CommandHandler;
import io.github.novanix.djluigi.commands.Parameter;
import io.github.novanix.djluigi.interaction.MenuHandler;
import io.github.novanix.djluigi.interaction.menus.paged.HelpMenu;
import io.github.novanix.djluigi.server.Server;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;

@CommandData
(
	command = "help", 
	description = "Lists the available commands",
	parameters = {
		@Parameter(name = "command", description = "The command the help menu should be shown for", type = OptionType.STRING, required = false)	
	},
	aliases = {"?", "h", "commands", "hep"},
	category = CommandCategory.Other
)
public class HelpCommand extends Command
{
	
	@Override
	public void executeCommand(Server S, SlashCommandInteractionEvent event) 
	{
		// Tell the user that it might take a second
		//event.deferReply().queue();
		
		if (event.getOption("command") == null)
		{
			MenuHandler.createMenu(HelpMenu.class, event);
		}
		
		else
		{
			sendCommandMenu(S, event, event.getOption("command").getAsString());
		}
			
	}

	private static void sendCommandMenu(Server s, SlashCommandInteractionEvent event, String commandName)
	{
		// Send a help menu for a specific command

		Command c = CommandHandler.getCommand(commandName);

		if (c == null)
		{
			event.getHook().sendMessage("Invalid command: `" + commandName + "`").queue();
			return;
		}

		StringBuilder title = new StringBuilder();

		title.append("/" + c.getCommandMessage() + " ");

		// Start writing the bottom part of the help menu
		// We start it here so we can take advantage of the fact that we are already
		// looping over the parameter list

		StringBuilder commandParameters = new StringBuilder();

		Parameter[] parameters = c.getParameters();

		if (parameters.length == 0)
		{
			commandParameters.append("None");
		}

		else
		{
			for (int i = 0; i < parameters.length; i++)
			{
				Parameter p = parameters[i];

				title.append(parameterToString(p) + " ");

				commandParameters.append(p.name());
				commandParameters.append(", ");
				commandParameters.append(p.type().name());

				if (!p.required())
				{
					commandParameters.append(" (Optional)");
				}

				commandParameters.append(": ");
				commandParameters.append(p.description());
			}
		}

		StringBuilder aliases = new StringBuilder();

		for (int i = 0; i < c.getAliases().length; i++)
		{
			aliases.append(c.getAliases()[i]);

			if (i < c.getAliases().length - 1)
			{
				aliases.append(", ");
			}
		}

		// Create the embed
		EmbedBuilder embed = new EmbedBuilder();

		embed.setTitle(title.toString());
		embed.setDescription(c.getDescription());

		if (c.getAliases().length != 0)
		{
			embed.addField("Aliases", aliases.toString(), false);
		}

		embed.addField("Parameters", commandParameters.toString(), false);
		embed.setColor(DJ.getPrimaryColor());

		event.replyEmbeds(embed.build()).queue();
		
		//event.getHook().sendMessageEmbeds(embed.build()).queue();

	}

	// Returns the parameter represented in a way that shows it's name, type, and if it is optional or not
	private static String parameterToString(Parameter parameter)
	{
		String innerText = parameter.name() + ", " + parameter.type().name();
		
		if (parameter.required())
		{
			return String.format("<%s>", innerText);
		}
		
		else
		{
			return String.format("[%s]", innerText);
		}
	}

}
