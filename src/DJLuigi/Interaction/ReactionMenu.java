package DJLuigi.Interaction;

import java.util.Objects;

import DJLuigi.DJ;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public abstract class ReactionMenu 
{

	public String channelID;
	public String messageID;
	
	public ReactionMenu(Message m)
	{
		this.messageID = m.getId();
		this.channelID = m.getChannel().getId();
		ReactionMenuManager.Menus.add(this);
	}
	
	public abstract void OnReactionUpdate(GuildMessageReactionAddEvent event);
	
	public Message GetMessage()
	{
		return DJ.jda.getTextChannelById(channelID).retrieveMessageById(messageID).complete();
	}
	
	public boolean EventIsEmoji(GuildMessageReactionAddEvent event, String emoji)
	{
		return event.getReaction().getReactionEmote().getEmoji().equals(emoji);
	}
	
	// Prevents the menu from updating more, removes it from the manager
	public void Delete()
	{
		ReactionMenuManager.Menus.remove(this);
	}

//	@Override
//	public int hashCode() {
//		return Objects.hash(channelID, messageID);
//	}

//	@Override
//	public boolean equals(Object obj) 
//	{
//		if (this == obj)
//			return true;
//		
//		if (obj instanceof String)
//		{
//			String sobj = (String) obj;
//			return sobj.equals(messageID) || sobj.equals(channelID);
//		}
//		
//		if (!(obj instanceof ReactionMenu))
//			return false;
//		ReactionMenu other = (ReactionMenu) obj;
//		return Objects.equals(channelID, other.channelID) && Objects.equals(messageID, other.messageID);
//	}
	
}
