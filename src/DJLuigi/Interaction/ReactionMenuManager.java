package DJLuigi.Interaction;

import java.util.ArrayList;

import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public class ReactionMenuManager 
{
	public static ArrayList<ReactionMenu> Menus = new ArrayList<ReactionMenu>();
	
	public static void onReactionEvent(GuildMessageReactionAddEvent event)
	{		
		if (event.getUser().isBot())
			return;
		
		String channel = event.getChannel().getId();
		String message = event.getMessageId();
		
		for (int i = 0; i < Menus.size(); i++)
		{
			ReactionMenu menu = Menus.get(i);
			
			if (menu.channelID.equals(channel) && menu.messageID.equals(message))
			{
				menu.OnReactionUpdate(event);
				return;
			}
		}
	}
}
