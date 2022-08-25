package DJLuigi.Interaction;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nonnull;

import DJLuigi.Interaction.Menus.QueueMenu;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MenuHandler extends ListenerAdapter 
{

	private ArrayList<Menu> menus = new ArrayList<Menu>();
	private HashMap<String, Menu> menuMap = new HashMap<String, Menu>();
	
	public MenuHandler()
	{
		System.out.println("Loading menus");
		
		loadMenu(new QueueMenu());
		
		System.out.println("Loaded " + menus.size() + " menus");
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
		
		
	}
	
	@Override
	public void onSelectMenuInteraction(SelectMenuInteractionEvent event) 
	{
		Menu menu = findEventMenu(event);
	}
	
	@Override
	public void onModalInteraction(@Nonnull ModalInteractionEvent event) 
	{
		
	}
	
	// Returns the 
	private Menu findEventMenu(GenericComponentInteractionCreateEvent event)
	{
		String[] path = event.getComponentId().split("/");
		
		return menuMap.get(path[0]);
	}
	
}
