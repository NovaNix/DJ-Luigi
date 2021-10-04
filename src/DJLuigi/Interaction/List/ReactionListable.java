package DJLuigi.Interaction.List;

public interface ReactionListable 
{

	public String getName();
	public String getValue(int index);
	public int size();
	public int itemsPerPage();
	
	public default int calculatePageCount()
	{
		return (int) Math.ceil(((double) size()) / ((double) itemsPerPage())); 
	}
	
}
