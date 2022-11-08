package djLuigi.interaction;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

// A type of interaction menu that can be interacted with by the user
// It is responsible for creating and updating interaction menus 
// Note: a menu object does not represent a singular interaction menu, it represents all interaction menus of this specific format
// To create an interaction menu of this type, use the generate() function
public abstract class Menu
{
	
	// TODO make it so theres a setting that only the creator of the menu can interact with it (basically prevent other people from using buttons and stuff)
	
	protected Menu()
	{

	}
	
	// Responds to the SlashCommandInteractionEvent with a menu
	public abstract void generate(SlashCommandInteractionEvent event, String... state);
	
	public void onButtonInteraction(ButtonInteractionEvent event) {};
	public void onSelectMenuInteraction(SelectMenuInteractionEvent event) {};
	public void onModalInteraction(@Nonnull ModalInteractionEvent event) {};
	
	// Returns the unique id of the Menu (Just the class name)
	// This is used to forward the interaction events to the right object
	public String getMenuID()
	{
		return this.getClass().getSimpleName();
	}
	
	// Generates a parsable string that stores the parent menu, the interacted component, and additional state info for use in a component's custom id
	// It is made parsable by splitting each element up using forward slashes, therefore you should never use forward slashes in the component name or in the toString functions of any of the stateInfo objects
	// While it can parse individual elements from the returned id, it cannot automatically parse the objects inserted into the stateinfo part. Only objects that do not violate the forward slash rule and that can be easily parsed back into their original types should be used
	protected String generateComponentID(String componentName, Object... stateInfo)
	{
		StringBuilder builder = new StringBuilder(getMenuID());
		
		builder.append("/");
		builder.append(componentName);
		
		for (int i = 0; i < stateInfo.length; i++)
		{	
			if (stateInfo[i] == null)
			{
				continue;
			}
			
			builder.append("/");
			builder.append(stateInfo[i].toString());
		}
		
		return builder.toString();
											
	}
	
	// Encodes state for the menu as a string
	protected String generateStateString(String... state)
	{
		if (state.length == 0)
		{
			return null;
		}
		
		StringBuilder stateString = new StringBuilder();
		
		for (int i = 0; i < state.length; i++)
		{
			if (i != 0)
			{
				stateString.append("/");
			}
			
			stateString.append(state[i]);
		}
		
		return stateString.toString();
	}
	
}
