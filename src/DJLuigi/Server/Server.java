package DJLuigi.Server;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import DJLuigi.DJ;
import DJLuigi.Audio.AudioPlayerSendHandler;
import DJLuigi.Audio.LoadResultHandler;
import DJLuigi.Audio.TrackScheduler;
import DJLuigi.IO.ServerData;
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
	public LoadResultHandler resultHandler;
	
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
		
		trackScheduler = new TrackScheduler(this);
		player.addListener(trackScheduler);
		
		resultHandler = new LoadResultHandler(this);
	}
	
	public void SendMessage(String message)
	{
		getActiveChannel().sendMessage(message).queue();
	}
	
	public void SetActiveTextChannel(MessageChannel channel)
	{
		ActiveTextChannel = channel.getId();
	}
	
	public void JoinChannel(VoiceChannel channel)
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
		
		trackScheduler.clearQueue();
		
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
