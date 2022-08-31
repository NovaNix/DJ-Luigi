package DJLuigi.Commands.Meta;

import DJLuigi.Commands.Command;
import DJLuigi.Commands.CommandData;
import DJLuigi.Server.Server;
import net.dv8tion.jda.api.entities.AudioChannel;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import DJLuigi.Commands.CommandCategory;

@CommandData
(
	command = "join", 
	description = "Makes the bot join VC",
	aliases = {"connect", "summon"},
	category = CommandCategory.Control
)
public class JoinCommand extends Command
{

	@Override
	public void executeCommand(Server S, SlashCommandInteractionEvent event) 
	{
		GuildVoiceState state = event.getMember().getVoiceState();
		
		if (!state.inAudioChannel())
		{
			event.reply("You're not in a channel!").setEphemeral(true).queue();
			
			return;
		}
		
		AudioChannel channel = state.getChannel();
		
		S.JoinChannel(channel);
		
		event.reply("Joined `" + channel.getName() + "`").queue();
	}
	
}
