package DJLuigi.Interaction;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public class ReactionConfirmation extends ReactionMenu
{

	public static Emoji ConfirmEmoji = Emoji.fromUnicode("✅");
	public static Emoji DeclineEmoji = Emoji.fromUnicode("🚫");
	
	public Runnable onConfirm;
	public Runnable onDecline;
	public String personConfirming;
	
	public ReactionConfirmation(String message, MessageReceivedEvent event, Runnable onConfirm, Runnable onDecline)
	{
		super(event.getChannel().sendMessage(message).complete());
	
		event.getChannel().addReactionById(messageID, ConfirmEmoji).queue();
		event.getChannel().addReactionById(messageID, DeclineEmoji).queue();
		
		this.personConfirming = event.getAuthor().getId();
		this.onConfirm = onConfirm;
		this.onDecline = onDecline;
	}
	
	@Override
	public void OnReactionUpdate(MessageReactionAddEvent event) 
	{	
		System.out.println("Found a reaction on the confirmation.");
		// RUN CHECK ON IF THE PERSON ADDING THE REACTION IS THE PERSON ASKED
		if (event.getMember().getId().equals(personConfirming))
		{
			System.out.println("The user who sent it was the person we asked to confirm");
			// Check if the reactions are the right ones
			if (EventIsEmoji(event, ConfirmEmoji))
			{
				onConfirm.run();
				Delete();
			}
			
			else if (EventIsEmoji(event, DeclineEmoji))
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
