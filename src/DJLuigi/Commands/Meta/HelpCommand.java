package DJLuigi.Commands.Meta;

import java.util.ArrayList;
import java.util.Comparator;

import DJLuigi.DJ;
import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandCategory;
import DJLuigi.Commands.CommandData;
import DJLuigi.Commands.CommandHandler;
import DJLuigi.Commands.Parameter;
import DJLuigi.Interaction.MenuHandler;
import DJLuigi.Interaction.Menus.HelpMenu;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
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
			sendHelpMenu(S, event);
		}
		
		else
		{
			sendCommandMenu(S, event, event.getOption("command").getAsString());
		}
			
	}
	
	// Sends a list of all of the commands the user can see
	private static void sendHelpMenu(Server s, SlashCommandInteractionEvent event)
	{
		
		MenuHandler.createMenu(HelpMenu.class, event);
		
//		Member user = event.getMember();
//		
//		StringBuilder commandList = new StringBuilder();
//	
//		commandList.append("Commands:\n");
//		commandList.append("```YML\n");
//					
//		// Create a list of the commands the user can use
//		
//		ArrayList<Command> commands = new ArrayList<Command>();
//					
//		for (Command c : CommandHandler.commands.values())
//		{
//			// TODO check if the user can use the command
//			commands.add(c);
//		}
//					
//		// Sort the commands so they are in a more readable order
//		commands.sort(sortCommands);
//
//		// Generate the help menu
//		
//		String currentCategory = "";
//
//		for (Command c : commands)
//		{
//			if (currentCategory != c.getCategory().name())
//			{
//				currentCategory = c.getCategory().name();
//				commandList.append("\n\t" + currentCategory + "\n");
//			}
//
//			commandList.append(c.getCommandMessage() + ": " + c.getDescription());
//			commandList.append("\n");
//		}
//
//		commandList.append("```");
//
//		event.getHook().sendMessage(commandList.toString()).queue();
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
