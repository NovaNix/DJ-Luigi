package DJLuigi.Interaction;

import DJLuigi.DJ;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.interaction.component.GenericComponentInteractionCreateEvent;

// Information about the state of a menu
public class MenuContext
{
	
	public final Server server;
	
	public MenuContext(GenericComponentInteractionCreateEvent event)
	{
		server = DJ.getServer(event.getGuild().getId());
	}
	
}
