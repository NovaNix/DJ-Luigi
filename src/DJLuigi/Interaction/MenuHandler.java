package DJLuigi.Interaction;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nonnull;

import DJLuigi.DJ;
import DJLuigi.Interaction.PagedMenus.EditorListMenu;
import DJLuigi.Interaction.PagedMenus.HelpMenu;
import DJLuigi.Interaction.PagedMenus.PlaylistListMenu;
import DJLuigi.Interaction.PagedMenus.PlaylistSongsMenu;
import DJLuigi.Interaction.PagedMenus.QueueMenu;
import DJLuigi.Interaction.PagedMenus.TestListMenu;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

// Handles creating, distributing events, and accessing menus
public class MenuHandler extends ListenerAdapter 
{

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
		
		System.out.println("Loaded " + menus.size() + " menus");
	}
	
	public static void createMenu(Class<? extends Menu> menuClass, SlashCommandInteractionEvent event, String... state)
	{
		Menu menu = menuMap.get(menuClass.getSimpleName());
		
		if (menu == null)
		{
			event.reply("Something went wrong! Please contact a developer!").queue();
			System.err.println("Failed to find menu \"" + menuClass.getSimpleName() + "\"");
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
	
		System.out.println("User \"" + event.getUser().getName() + "\" (" + event.getUser().getId() + ") has interacted with a menu of type \"" + menu.getMenuID() + "\"");
		
		menu.onButtonInteraction(event);
	}
	
	@Override
	public void onSelectMenuInteraction(SelectMenuInteractionEvent event) 
	{
		Menu menu = findEventMenu(event);
		
		System.out.println("User \"" + event.getUser().getName() + "\" (" + event.getUser().getId() + ") has interacted with a menu of type \"" + menu.getMenuID() + "\"");
		
		menu.onSelectMenuInteraction(event);
	}
	
	@Override
	public void onModalInteraction(@Nonnull ModalInteractionEvent event) 
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
