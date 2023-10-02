package io.github.novanix.djluigi.commands;

public enum CommandCategory 
{

	Audio(0), Control(1), Settings(2), Playlist(3), Other(4);
	
	public int order;
	
	CommandCategory(int order)
	{
		this.order = order;
	}
	
}
