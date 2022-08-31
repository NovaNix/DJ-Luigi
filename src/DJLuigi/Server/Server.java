package DJLuigi.Server;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import DJLuigi.DJ;
import DJLuigi.Audio.AudioPlayerSendHandler;
import DJLuigi.Audio.Queue;
import DJLuigi.Audio.TrackScheduler;
import DJLuigi.IO.ServerData;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class Server 
{
	public String guildID;
	
	public ServerData data;
	
	public AudioPlayer player;
	public TrackScheduler trackScheduler;
	
	public Queue queue;
	
	// The voice channel that the bot is in
	public String ActiveVoiceChannel = "";
	// The text channel that the bot should send messages to
	// This changes whenever a command is sent
	private String ActiveTextChannel = "";
	
	public Server(String GuildID)
	{
		this.guildID = GuildID;
		
		this.data = new ServerData(guildID); 
		
		player = DJ.playerManager.createPlayer();
		
		queue = new Queue(this);
		
		trackScheduler = new TrackScheduler(this);
		player.addListener(trackScheduler);
	}
	
	// Sends a message to the current active text channel. 
	// Note: should never be used by a command, because slash commands require a reply through the event
	public void SendMessage(String message)
	{
		getActiveChannel().sendMessage(message).queue();
	}
	
	public void SetActiveTextChannel(MessageChannel channel)
	{
		ActiveTextChannel = channel.getId();
	}
	
	public void JoinChannel(AudioChannel channel)
	{
		AudioManager audioManager = getGuild().getAudioManager();
		
	 	audioManager.setSendingHandler(new AudioPlayerSendHandler(player));
		audioManager.openAudioConnection(channel);
		
		ActiveVoiceChannel = channel.getId();
	}
	
	public void LeaveVC() 
	{
		AudioManager audioManager = getGuild().getAudioManager();
		
		audioManager.closeAudioConnection();
		
		queue.clear();
		
		ActiveVoiceChannel = "";
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
		
		VoiceChannel channel = DJ.jda.getVoiceChannelById(ActiveVoiceChannel);
		
		return (channel.getMembers().size() <= 1);
		
	}
	
	public Guild getGuild()
	{
		return DJ.jda.getGuildById(guildID);
	}
	
	public MessageChannel getActiveChannel()
	{
		return DJ.jda.getTextChannelById(ActiveTextChannel);
	}
}
