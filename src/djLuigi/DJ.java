package djLuigi;

import java.awt.Color;
import javax.security.auth.login.LoginException;

//import com.sedmelluq.discord.lavaplayer.jdaudp.NativeAudioSendFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import djLuigi.interaction.MenuHandler;
import djLuigi.server.ServerHandler;
import djLuigi.commands.CommandHandler;
import djLuigi.io.DirectoryManager;
import djLuigi.playlist.PlaylistManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class DJ 
{

	public static AudioPlayerManager playerManager;
	
	public static JDA jda;
		
	// TODO Consider moving servers to their own manager
	
	
	public static DJSettings settings;
	
	private static Color primaryColor;
	
	public static void main(String[] args) throws LoginException, InterruptedException
    {
		
		System.out.println("Starting...");
		
		if (args.length == 0)
		{
			System.err.println("You must specify the home directory in the program arguments!");
			System.exit(1);
		}
		
		System.out.println("Loading home directory \"" + args[0] + "\"");
		
		DirectoryManager.init(args[0]);
		
		settings = DirectoryManager.loadDJConfig();
		
		System.out.println("Config file loaded.");
		
		primaryColor = new Color(settings.botColor);
		
		if (settings.botToken.equals(""))
		{
			System.err.println("You must specify the bot token in the config file!");
			System.exit(1);
		}
		
		System.out.println("Setting up JDA instance...");
		
        jda = JDABuilder.create(settings.botToken, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.MESSAGE_CONTENT)
            .addEventListeners(
            		new ServerHandler(),
            		new MenuHandler())
            .setActivity(Activity.playing(settings.botStatus))
            .build();

        playerManager = new DefaultAudioPlayerManager();
		AudioSourceManagers.registerRemoteSources(playerManager);
        
        // optionally block until JDA is ready
        jda.awaitReady();
        
        System.out.println("JDA loaded");

        PlaylistManager.init();
        int loadedServers = ServerHandler.loadServers();
        System.out.println("Loaded " + loadedServers + " server" + (loadedServers != 1 ? "s" : ""));
        
        CommandHandler.init();
        CommandHandler.initSlashCommands();
                
        System.out.println("Ready to accept user input!");
        
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
