package DJLuigi.utils;

import java.io.File;

public class directoryUtils 
{
	// Ensures that the folder is there
	// If the folder does not exist, it will create it
	public static void validateFolder(File f)
	{
		if (!f.exists())
		{
			System.out.println("Folder " + f.getName() + " does not exist! Creating it.");
			f.mkdirs();
		}
	}
	
}
