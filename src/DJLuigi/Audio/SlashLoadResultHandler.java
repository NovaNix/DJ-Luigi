package DJLuigi.Audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import DJLuigi.Server.Server;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

// A Result Handler that also replies to slash commands
public class SlashLoadResultHandler implements AudioLoadResultHandler
{

	private Server HostServer;
	private SlashCommandInteractionEvent event;
	
	public SlashLoadResultHandler(Server HostServer, SlashCommandInteractionEvent event)
	{
		this.HostServer = HostServer;
		this.event = event;
	}
	
	@Override
	public void trackLoaded(AudioTrack track) 
	{
		
		event.getHook().sendMessage("Added `" + track.getInfo().title + "`").queue();
		HostServer.queue.add(track);
	}

	@Override
	public void playlistLoaded(AudioPlaylist playlist) 
	{
		if (playlist.isSearchResult())
		{
			event.getHook().sendMessage("Added `" + playlist.getTracks().get(0).getInfo().title + "`").queue();
			HostServer.queue.add(playlist.getTracks().get(0));
		}
		
		else
		{
			event.getHook().sendMessage("Added " + playlist.getTracks().size() + " Songs").queue();
			for (AudioTrack track : playlist.getTracks()) 
			{
				HostServer.queue.add(track);
			}
		}
	}

	@Override
	public void noMatches() 
	{
		event.getHook().sendMessage("Hmm, I couldnt find that...").queue();
	}

	@Override
	public void loadFailed(FriendlyException exception) 
	{
		event.getHook().sendMessage("Failed to load song: `" + exception.getMessage() + "`").queue();
	}

	
	
}
