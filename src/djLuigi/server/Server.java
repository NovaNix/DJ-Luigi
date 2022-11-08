package djLuigi.server;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import djLuigi.DJ;
import djLuigi.audio.AudioPlayerSendHandler;
import djLuigi.audio.Queue;
import djLuigi.audio.TrackScheduler;
import djLuigi.io.ServerData;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.managers.AudioManager;

public class Server 
{
	private String id;
	
	public ServerData data;
	
	public AudioPlayer player;
	public TrackScheduler trackScheduler;
	
	public Queue queue;
	
	// The voice channel that the bot is in
	public String activeVoiceChannel = "";
	// The text channel that the bot should send messages to
	// This changes whenever a command is sent
	private String activeTextChannel = "";
	
	public Server(String guildID)
	{
		this.id = guildID;
		
		this.data = new ServerData(id); 
		
		player = DJ.playerManager.createPlayer();
		
		queue = new Queue(this);
		
		trackScheduler = new TrackScheduler(this);
		player.addListener(trackScheduler);
	}
	
	// Sends a message to the current active text channel. 
	// Note: should never be used by a command, because slash commands require a reply through the event
	public void sendMessage(String message)
	{
		getActiveChannel().sendMessage(message).queue();
	}
	
	public void setActiveTextChannel(MessageChannelUnion channel)
	{
		activeTextChannel = channel.getId();
	}
	
	public void joinChannel(AudioChannelUnion channel)
	{
		AudioManager audioManager = getGuild().getAudioManager();
		
	 	audioManager.setSendingHandler(new AudioPlayerSendHandler(player));
		audioManager.openAudioConnection(channel);
		
		activeVoiceChannel = channel.getId();
	}
	
	public void leaveVC() 
	{
		AudioManager audioManager = getGuild().getAudioManager();
		
		audioManager.closeAudioConnection();
		
		queue.clear();
		
		activeVoiceChannel = "";
	}
	
	public boolean isInVC()
	{
		AudioManager audioManager = getGuild().getAudioManager();
		
		return audioManager.isConnected();
	}
	
	public boolean isAloneInVC()
	{
		if (!isInVC())
		{
			return true;
		}
		
		VoiceChannel channel = DJ.jda.getVoiceChannelById(activeVoiceChannel);
		
		return (channel.getMembers().size() <= 1);
		
	}
	
	public Guild getGuild()
	{
		return DJ.jda.getGuildById(id);
	}
	
	public TextChannel getActiveChannel()
	{
		return DJ.jda.getTextChannelById(activeTextChannel);
	}
	
	public String getId()
	{
		return id;
	}
}
