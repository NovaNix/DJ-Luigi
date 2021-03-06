package DJLuigi.Interaction.List;

import java.awt.Color;

import DJLuigi.Interaction.ReactionMenu;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public class ReactionList extends ReactionMenu
{

	public static boolean NewMessageOnUpdate = true;
	
	public static String ForwardEmote = "➡️";
	public static String BackEmote = "⬅️";
		
	public int Page = 0;
	
	public MessageReceivedEvent event; // The message used to create the menu
	
	public ReactionListable listed;
	
	public ReactionList(ReactionListable listed, int page, MessageReceivedEvent event) 
	{
		super(generateList(listed, page, event));
	
		this.Page = page;
		this.listed = listed;
		
		this.event = event;
		
		if (Page != 0)
		{
			event.getChannel().addReactionById(messageID, BackEmote).queue();
		}
		
		if (!((Page + 1) >= listed.calculatePageCount()))
		{
			event.getChannel().addReactionById(messageID, ForwardEmote).queue();
		}
	}
	
	@SuppressWarnings("deprecation")
	public static Message generateList(ReactionListable listed, int page, MessageReceivedEvent event)
	{
		return event.getChannel().sendMessage(generateEmbed(listed, page)).complete();
	}
	
	public static MessageEmbed generateEmbed(ReactionListable listed, int page)
	{
		StringBuilder description = new StringBuilder();
		
		for (int i = 0; i < listed.itemsPerPage(); i++)
		{
			if (i + (page * listed.itemsPerPage()) >= listed.size())
			{
				break;
			}
			
			description.append(listed.getValue(i + (page * listed.itemsPerPage())));
			description.append("\n");
		}
		
		MessageEmbed embed = new EmbedBuilder()
				.setDescription(description.toString())
				.setColor(new Color(13012625))
				.setFooter("Page " + (page + 1) + " of " + listed.calculatePageCount(), "https://i.redd.it/b2pilioyu7u21.jpg")
				.setAuthor(listed.getName(), null, "https://i.redd.it/b2pilioyu7u21.jpg")
				.build();
		
		return embed;
	}
	
	public void UpdateList()
	{
		if (NewMessageOnUpdate)
		{
			new ReactionList(listed, Page, event);
			Delete();
		}
	}
	
	@Override
	public void OnReactionUpdate(GuildMessageReactionAddEvent event) 
	{
		if (EventIsEmoji(event, ForwardEmote))
		{
			if ((Page + 1) < listed.calculatePageCount())
			{
				Page++;
				UpdateList();
			}
		}
		
		else if (EventIsEmoji(event, BackEmote))
		{
			if ((Page - 1) >= 0)
			{
				Page--;
				UpdateList();
			}
		}
		
		
	}
	
}
