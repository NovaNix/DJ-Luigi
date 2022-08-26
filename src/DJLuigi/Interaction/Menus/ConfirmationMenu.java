package DJLuigi.Interaction.Menus;

import javax.annotation.Nonnull;

import DJLuigi.Interaction.Menu;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public abstract class ConfirmationMenu extends Menu
{

	//public static final String MENU_CONFIRMATION = "Confirmation";
	
	public ConfirmationMenu()
	{

	}

	@Override
	protected void generate(SlashCommandInteractionEvent event)
	{
		
		
	}
	
	@Override
	public void onModalInteraction(@Nonnull ModalInteractionEvent event) 
	{
		
	}

}
