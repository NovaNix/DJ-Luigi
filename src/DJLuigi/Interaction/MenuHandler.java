package DJLuigi.Interaction;

import java.util.ArrayList;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class MenuHandler
{

	private ArrayList<Menu> menus = new ArrayList<Menu>();
	
	
	public void onButtonInteraction(ButtonInteractionEvent event) 
	{
		
	}
	
	protected void registerMenu(Menu menu)
	{
		menus.add(menu);
	}
	
	protected void deregisterMenu(Menu menu)
	{
		menus.remove(menu);
	}
	
}
