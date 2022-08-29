package DJLuigi.Interaction.Menus;

import DJLuigi.Interaction.Menu;
import DJLuigi.Interaction.MenuContext;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

// A menu with multiple pages
// The state of the menu (mainly the page number) is stored in the component ids of the buttons
public abstract class PagedMenu extends Menu
{
	
	public static Emoji forwardEmoji = Emoji.fromUnicode("➡️");
	public static Emoji refreshEmoji = Emoji.fromUnicode("🔄");
	public static Emoji backEmoji = Emoji.fromUnicode("⬅️"); 

	@Override
	protected void generate(SlashCommandInteractionEvent event)
	{
		generate(event, 0);
	}
	
	// Generates the element at a specific page
	protected void generate(SlashCommandInteractionEvent event, int page)
	{
		MenuContext context = new MenuContext(event);
		
		MessageEmbed pageMessage = getPage(page, context);
		
		event.replyEmbeds(pageMessage).addActionRow(generateButtons(context, page)).queue();
	}
	
	// Generates the buttons for a page
	protected ItemComponent[] generateButtons(MenuContext context, int page)
	{
		Button backButton = Button.primary(generateComponentID("back", page - 1), backEmoji);
		Button refreshButton = Button.primary(generateComponentID("refresh", page), refreshEmoji);
		Button forwardButton = Button.primary(generateComponentID("forward", page + 1), forwardEmoji);
		
		// Handle the disabled state of buttons
		
		if (page == 0)
		{
			backButton = backButton.asDisabled();
		}
		
		if (page + 1 >= getPageCount(context) || getPageCount(context) == 1)
		{
			forwardButton = forwardButton.asDisabled();
		}
		
		return new ItemComponent[] {
			backButton, refreshButton, forwardButton					
		};
	}
	
	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) 
	{
		String[] path = event.getComponentId().split("/");
		
		@SuppressWarnings("unused")
		String component = path[1]; // The component that was clicked
		int page = Integer.parseInt(path[2]); // The page to show
		
		MenuContext context = new MenuContext(event);
		
		// Prevent getting a page that no longer exists (the maximum pages might have changed since the last time the element was updated, leaving the new page out of bounds)
		
		int maxPages = getPageCount(context);
		
		if (page >= maxPages)
		{
			page = maxPages - 1;
		}
		
		MessageEmbed pageMessage = getPage(page, context);
		
		event.editMessageEmbeds(pageMessage).setActionRow(generateButtons(context, page)).queue();
	}
	
	// Returns an embed representing the contents of the page
	protected abstract MessageEmbed getPage(int page, MenuContext context);
	
	// Gets the number of pages that can be shown through the menu
	// Note: the minimum the page count can go is 1
	protected abstract int getPageCount(MenuContext context);
	
	// A helper function for calculating the page count based on the number of items per page
	public static int calculatePageCount(int itemsPerPage, int itemCount)
	{
		return (int) Math.max(Math.ceil(((float) itemCount) / ((float) itemsPerPage)), 1);
	}
}
