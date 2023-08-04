package djLuigi.interaction;

import java.util.ArrayList;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import djLuigi.DJ;
import djLuigi.interaction.menus.paged.EditorListMenu;
import djLuigi.interaction.menus.paged.HelpMenu;
import djLuigi.interaction.menus.paged.PlaylistListMenu;
import djLuigi.interaction.menus.paged.PlaylistSongsMenu;
import djLuigi.interaction.menus.paged.QueueMenu;
import djLuigi.interaction.menus.paged.TestListMenu;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

// Handles creating, distributing events, and accessing menus
public class MenuHandler extends ListenerAdapter 
{
	
	private static final Logger logger = LoggerFactory.getLogger(MenuHandler.class);

	private static ArrayList<Menu> menus = new ArrayList<Menu>();
	private static HashMap<String, Menu> menuMap = new HashMap<String, Menu>();
	
	public MenuHandler()
	{
		System.out.println("Loading menus");
		
		loadMenu(new QueueMenu());
		loadMenu(new HelpMenu());
		
		loadMenu(new PlaylistListMenu());
		loadMenu(new PlaylistSongsMenu());
		loadMenu(new EditorListMenu());
		
		// Load debug mode exclusive menus
		if (DJ.settings.debugMode)
		{
			loadMenu(new TestListMenu());
		}
		
		logger.info("Loaded " + menus.size() + " menus");
	}
	
	public static void createMenu(Class<? extends Menu> menuClass, SlashCommandInteractionEvent event, String... state)
	{
		Menu menu = menuMap.get(menuClass.getSimpleName());
		
		if (menu == null)
		{
			event.reply("Something went wrong! Please contact a developer!").queue();
			logger.error("Failed to find menu \"" + menuClass.getSimpleName() + "\"");
			return;
		}
		
		menu.generate(event, state);
	}
	
	private void loadMenu(Menu menu)
	{
		menus.add(menu);
		
		// Prevent bugs where a menu with the same id can be added to the map, overriding another menu
		if (menuMap.containsKey(menu.getMenuID()))
			throw new IllegalArgumentException("A menu with the id " + menu.getMenuID() + " already exists in the MenuHandler!");
		
		menuMap.put(menu.getMenuID(), menu);
	}
	
	public boolean hasMenuType(String id)
	{
		return menuMap.containsKey(id);
	}
	
	@Override
	public void onButtonInteraction(ButtonInteractionEvent event) 
	{
		Menu menu = findEventMenu(event);
	
		logger.info("User \"" + event.getUser().getName() + "\" (" + event.getUser().getId() + ") has interacted with a menu of type \"" + menu.getMenuID() + "\"");
		
		menu.onButtonInteraction(event);
	}
	
	@Override
	public void onEntitySelectInteraction(EntitySelectInteractionEvent event) 
	{
		Menu menu = findEventMenu(event);
		
		logger.info("User \"" + event.getUser().getName() + "\" (" + event.getUser().getId() + ") has interacted with a menu of type \"" + menu.getMenuID() + "\"");
		
		menu.onSelectMenuInteraction(event);
	}
	
	@Override
	public void onModalInteraction(ModalInteractionEvent event) 
	{
		// TODO implement modal menus
		//System.out.println("User \"" + event.getUser().getName() + "\" (" + event.getUser().getId() + ") has interacted with a menu of type \"" + menu.getMenuID() + "\"");
		
	}
	
	// Returns the 
	private Menu findEventMenu(GenericComponentInteractionCreateEvent event)
	{
		String[] path = event.getComponentId().split("/");
		
		return menuMap.get(path[0]);
	}
	
}
