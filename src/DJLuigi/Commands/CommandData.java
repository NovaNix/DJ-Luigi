package DJLuigi.Commands;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

// Stores all metadata used for commands
// Used for the help command, determining who can run a command, and other useful command stuff
@Retention(RUNTIME)
public @interface CommandData 
{

	String command();
	String description();
	
	String[] aliases() default {};
	
	CommandCategory category() default CommandCategory.Other;
	
	boolean debug() default false;
	boolean djOnly() default false;
	boolean ownerOnly() default false;
	
}
