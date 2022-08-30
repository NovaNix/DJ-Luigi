package DJLuigi.Interaction.PagedMenus;

import java.util.Random;

import DJLuigi.DJ;
import DJLuigi.Interaction.MenuContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class TestListMenu extends PagedMenu
{
	
	private static final int itemsPerPage = 10;
	
	private static final int itemsTotal = 26;
	
	public TestListMenu()
	{
		
	}

	@Override
	protected MessageEmbed getPage(int page, MenuContext context)
	{
		StringBuilder description = new StringBuilder();
		
		Random r = new Random(page);
		
		String allowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!?@#$%^&*";
		
		for (int i = 0; i < itemsPerPage; i++)
		{
			if (i + (page * itemsPerPage) >= itemsTotal)
			{
				break;
			}
			
			String randomChars = "";
			
			for (int c = 0; c < 8 + r.nextInt(8); c++)
			{
				randomChars = randomChars + allowedCharacters.charAt(r.nextInt(allowedCharacters.length()));
			}
			
			description.append(i + (itemsPerPage * page) + ". " + randomChars);
			description.append("\n");
		}
		
		MessageEmbed embed = new EmbedBuilder()
				.setTitle("List Test")
				.setDescription(description.toString())
				.setColor(DJ.getPrimaryColor())
				.setFooter("Page " + (page + 1) + " of " + getPageCount(context), null)
//				.setAuthor(listed.getName(), null, null)
				.build();
		
		return embed;
	}

	@Override
	protected int getPageCount(MenuContext context)
	{
		return calculatePageCount(itemsPerPage, itemsTotal);
	}
}
