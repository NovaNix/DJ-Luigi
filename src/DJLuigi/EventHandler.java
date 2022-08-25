package DJLuigi;

import DJLuigi.Commands.CommandHandler;
import DJLuigi.Interaction.ReactionMenuManager;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

// TODO Consider merging into command handler
public class EventHandler extends ListenerAdapter 
{
	
	public EventHandler()
	{
		CommandHandler.init();
	}
	
	@Override
	public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
	{
		Server host = DJ.Servers.get(event.getGuild().getId());
		host.SetActiveTextChannel(event.getChannel());
		
		CommandHandler.processCommand(host, event);
	}
	
	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event)
	{
		AudioChannel left = event.getChannelLeft();
		
		Server host = DJ.Servers.get(left.getGuild().getId());
		
		if (host.isAloneInVC())
		{
			host.LeaveVC();
		}
	}
	
	@Override
    public void onGuildJoin(GuildJoinEvent event)
    {
		if (DJ.settings.sendJoinMessage)
		{
			MessageEmbed sendMessage = new EmbedBuilder()
				    .setTitle("WHATS UP MOTHERFUCKERS")
				    .setDescription("I'm DJ Luigi, and im here to swag. Since Rythm is shutting down, I'm taking over! Here are a few things about myself:")
				    .setColor(DJ.getPrimaryColor())
				    .setThumbnail("https://i.redd.it/b2pilioyu7u21.jpg")
				    .setImage(DJ.settings.botIcon)
				    .addField("I'm better than Mario", "He's a red idiot who should belong in prison. I'm am epic DJ.", false)
				    .addField("I'm the best!", "Prove me wrong, I bet you cant.", false)
				    .addField("I'm honing my skills!", "I'm still improving, because I'm always on that grind. I should be ready to DJ for you by Friday!", false)
				    .build();
					
					event.getGuild().getSystemChannel().sendMessageEmbeds(sendMessage).queue();
					
					DJ.Servers.put(event.getGuild().getId(), new Server(event.getGuild().getId()));
		}
		
    }
	
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent event)
	{
		ReactionMenuManager.onReactionEvent(event);
	}
	
}
