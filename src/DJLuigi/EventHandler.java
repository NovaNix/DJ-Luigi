package DJLuigi;

import java.awt.Color;

import DJLuigi.Commands.CommandHandler;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventHandler extends ListenerAdapter 
{
	
	public EventHandler()
	{
		CommandHandler.init();
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) 
	{
		if (event.getAuthor().isBot()) { return; }
		
		Server host = DJ.Servers.get(event.getGuild().getId());
		
		String serverCommandPrefix = host.data.settings.commandPrefix;
		
		if (event.getMessage().getContentRaw().startsWith(serverCommandPrefix))
		{
			host.SetActiveTextChannel(event.getChannel());
			CommandHandler.processCommand(host, event);
		}
		
	}
	
//	@Override
//	public void onGuildVoiceJoin(GuildVoiceJoinEvent event)
//	{
//		
//	}
	
	@Override
	public void onGuildVoiceLeave(GuildVoiceLeaveEvent event)
	{
		VoiceChannel left = event.getChannelLeft();
		
		Server host = DJ.Servers.get(left.getGuild().getId());
		
		if (host.isAloneInVC())
		{
			host.LeaveVC();
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
    public void onGuildJoin(GuildJoinEvent event)
    {
		if (DJ.settings.sendJoinMessage)
		{
			MessageEmbed sendMessage = new EmbedBuilder()
				    .setTitle("WHATS UP MOTHERFUCKERS")
				    .setDescription("I'm DJ Luigi, and im here to swag. Since Rythm is shutting down, I'm taking over! Here are a few things about myself:")
				    .setColor(new Color(15060541))
				    .setThumbnail("https://i.redd.it/b2pilioyu7u21.jpg")
				    .setImage("https://i.pinimg.com/originals/61/a8/88/61a8880068b6275c91ba2408cd1718a7.png")
				    .addField("I'm better than Mario", "He's a red idiot who should belong in prison. I'm am epic DJ.", false)
				    .addField("I'm the best!", "Prove me wrong, I bet you cant.", false)
				    .addField("I'm honing my skills!", "I'm still improving, because I'm always on that grind. I should be ready to DJ for you by Friday!", false)
				    .build();
					
					event.getGuild().getSystemChannel().sendMessage(sendMessage).queue();
					
					DJ.Servers.put(event.getGuild().getId(), new Server(event.getGuild().getId()));
		}
		
    }
	
}
