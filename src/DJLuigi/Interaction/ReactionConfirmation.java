package DJLuigi.Interaction;

import DJLuigi.Server.Server;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

public class ReactionConfirmation extends ReactionMenu
{

	public static String ConfirmEmote = "✅";
	public static String DeclineEmote = "🚫";
	
	public Runnable onConfirm;
	public Runnable onDecline;
	public String personConfirming;
	
	public ReactionConfirmation(String message, MessageReceivedEvent event, Runnable onConfirm, Runnable onDecline)
	{
		super(event.getChannel().sendMessage(message).complete());
	
		event.getChannel().addReactionById(messageID, ConfirmEmote).queue();
		event.getChannel().addReactionById(messageID, DeclineEmote).queue();
		
		this.personConfirming = event.getAuthor().getId();
		this.onConfirm = onConfirm;
		this.onDecline = onDecline;
	}
	
	@Override
	public void OnReactionUpdate(GuildMessageReactionAddEvent event) 
	{	
		System.out.println("Found a reaction on the confirmation.");
		// RUN CHECK ON IF THE PERSON ADDING THE REACTION IS THE PERSON ASKED
		if (event.getMember().getId().equals(personConfirming))
		{
			System.out.println("The user who sent it was the person we asked to confirm");
			// Check if the reactions are the right ones
			if (EventIsEmoji(event, ConfirmEmote))
			{
				onConfirm.run();
				Delete();
			}
			
			else if (EventIsEmoji(event, DeclineEmote))
			{
				onDecline.run();
				Delete();
			}
		}
		
		else
		{
			event.getReaction().removeReaction(event.getUser()).queue();
		}
	}
	
}
