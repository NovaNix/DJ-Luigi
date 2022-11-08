package djLuigi.interaction.menus;

import djLuigi.interaction.Menu;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

// Confirmation menus are on hold until modals are more mature. 
// Currently you cannot change the button text, add a description, or anything like that.
// Once modals have these features the feature will converted to modals
// For now it could be turned into a regular menu message
public abstract class ConfirmationMenu extends Menu
{

	//public static final String MENU_CONFIRMATION = "Confirmation";
	
	public ConfirmationMenu()
	{
		
	}

	@Override
	public void generate(SlashCommandInteractionEvent event, String... state)
	{
		//Modal modal = Modal.create("test", "Confirmation").
		
	}
	
	@Override
	public void onModalInteraction(ModalInteractionEvent event) 
	{
		
	}
	
	protected abstract void onConfirm(ModalInteractionEvent event);

}
