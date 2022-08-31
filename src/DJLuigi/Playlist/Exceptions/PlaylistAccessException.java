package DJLuigi.Playlist.Exceptions;

@SuppressWarnings("serial")
// An error that has occurred that relates to accessing or modifying a playlist
public class PlaylistAccessException extends Exception
{
	protected Type type;
	
	public PlaylistAccessException(String errorMessage, Type type)
	{
		super(errorMessage);
		this.type = type;
	}
	
	public Type getType()
	{
		return type;
	}
	
	public enum Type
	{
		UnknownPlaylist, MissingPermissions, DuplicatePlaylistNames
	}
}
