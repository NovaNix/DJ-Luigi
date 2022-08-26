package DJLuigi.Interaction.Menus;

import javax.annotation.Nonnull;

import DJLuigi.Interaction.Menu;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class ConfirmationMenu extends Menu
{

	public static final String MENU_CONFIRMATION = "Confirmation";
	
	public ConfirmationMenu()
	{
		super(MENU_CONFIRMATION);
	}

	@Override
	protected void generate(SlashCommandInteractionEvent event)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onModalInteraction(@Nonnull ModalInteractionEvent event) 
	{
		
	}

}
