package DJLuigi.Interaction;

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
	
	private final String id;
	
	protected Menu(String id)
	{
		this.id = id;
	}
	
	// Responds to the SlashCommandInteractionEvent with a menu
	protected abstract void generate(SlashCommandInteractionEvent event);
	
	public void onButtonInteraction(ButtonInteractionEvent event) {};
	public void onSelectMenuInteraction(SelectMenuInteractionEvent event) {};
	public void onModalInteraction(@Nonnull ModalInteractionEvent event) {};
	
	// Returns the unique id of the Menu
	// This is used to forward the interaction events to the right object
	public String getMenuID()
	{
		return id;
	}
	
}
