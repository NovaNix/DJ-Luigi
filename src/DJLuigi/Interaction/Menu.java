package DJLuigi.Interaction;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;

public abstract class Menu
{

	protected SlashCommandInteractionEvent event;
	
	public Menu(SlashCommandInteractionEvent event)
	{
		this.event = event;
	}
	
	public void onButtonInteraction(ButtonInteractionEvent event) {};
	public void onSelectMenuInteraction(SelectMenuInteractionEvent event) {};
	public void onModalInteraction(@Nonnull ModalInteractionEvent event) {};
	
}
