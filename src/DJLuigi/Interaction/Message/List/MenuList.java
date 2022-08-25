package DJLuigi.Interaction.Message.List;

import DJLuigi.Interaction.Menu;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public abstract class MenuList extends Menu
{
	
	public static Emoji ForwardEmote = Emoji.fromUnicode("➡️");
	public static Emoji BackEmote = Emoji.fromUnicode("⬅️");
	
	public MenuList(SlashCommandInteractionEvent event)
	{
		super(event);
		
		// Add buttons
	}

	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) 
	{
		
	}
}
