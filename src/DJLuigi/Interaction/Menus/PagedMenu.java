package DJLuigi.Interaction.Menus;

import DJLuigi.Interaction.Menu;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

// A menu with multiple pages
public abstract class PagedMenu extends Menu
{
	
	public static Emoji forwardEmoji = Emoji.fromUnicode("➡️");
	public static Emoji refreshEmoji = Emoji.fromUnicode("🔄");
	public static Emoji backEmoji = Emoji.fromUnicode("⬅️");
	
	protected int page = 0;
	
	protected PagedMenu(String id)
	{
		super(id);
	}

	@Override
	protected void generate(SlashCommandInteractionEvent event)
	{
		
	}
	
	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) 
	{
		
	}
	
	//protected abstract Embed 
	
	protected abstract int getPageCount();
}
