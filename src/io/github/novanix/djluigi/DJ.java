package io.github.novanix.djluigi;

import java.awt.Color;
import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import dev.lavalink.youtube.YoutubeAudioSourceManager;
import dev.lavalink.youtube.clients.AndroidTestsuiteWithThumbnail;
import dev.lavalink.youtube.clients.AndroidWithThumbnail;
import dev.lavalink.youtube.clients.MusicWithThumbnail;
import dev.lavalink.youtube.clients.WebEmbeddedWithThumbnail;
import dev.lavalink.youtube.clients.WebWithThumbnail;
import dev.lavalink.youtube.clients.skeleton.Client;
import io.github.novanix.djluigi.commands.CommandHandler;
import io.github.novanix.djluigi.interaction.MenuHandler;
import io.github.novanix.djluigi.io.DirectoryManager;
import io.github.novanix.djluigi.playlist.PlaylistManager;
import io.github.novanix.djluigi.server.ServerHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class DJ 
{

	private static final Logger logger = LoggerFactory.getLogger(DJ.class);
	
	public static AudioPlayerManager playerManager;
	
	public static JDA jda;
		
	public static DJSettings settings;
	
	private static Color primaryColor;
	
	public static void main(String[] args) throws LoginException, InterruptedException
    {
		
		logger.info("Starting...");
		
		if (args.length == 0)
		{
			logger.error("You must specify the home directory in the program arguments!");
			System.exit(1);
		}
		
		logger.info("Loading home directory \"" + args[0] + "\"");
		
		DirectoryManager.init(args[0]);
		
		settings = DirectoryManager.loadDJConfig();
		
		logger.info("Config file loaded.");
		
		primaryColor = new Color(settings.botColor);
		
		if (settings.botToken.equals(""))
		{
			logger.error("You must specify the bot token in the config file!");
			System.exit(1);
		}
		
		logger.info("Setting up JDA instance...");
		
        jda = JDABuilder.create(settings.botToken, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.MESSAGE_CONTENT)
            .addEventListeners(
            		new ServerHandler(),
            		new MenuHandler())
            .setActivity(Activity.playing(settings.botStatus))
            .build();

        playerManager = new DefaultAudioPlayerManager();
        
        // Add youtube support
        YoutubeAudioSourceManager youtube = new YoutubeAudioSourceManager(/*allowSearch:*/ true, new Client[] { new MusicWithThumbnail(), new WebWithThumbnail(), new AndroidTestsuiteWithThumbnail(), new WebEmbeddedWithThumbnail() });
        playerManager.registerSourceManager(youtube);

		AudioSourceManagers.registerRemoteSources(playerManager);
        
        // optionally block until JDA is ready
        jda.awaitReady();
        
        logger.info("JDA loaded");

        PlaylistManager.init();
        int loadedServers = ServerHandler.loadServers();
        logger.info("Loaded " + loadedServers + " server" + (loadedServers != 1 ? "s" : ""));
        
        CommandHandler.init();
        CommandHandler.initSlashCommands();
                
        logger.info("Ready to accept user input!");
        
    }
	
	// Gets the primary color of the bot
	public static Color getPrimaryColor()
	{
		return primaryColor;
	}
	
	public static int getJoinedServersCount()
	{
		return jda.getGuilds().size();
	}
	
}
