package DJLuigi.Commands;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;

// Stores all metadata used for commands
// Used for the help command and for determining who can run a command
@Retention(RUNTIME)
public @interface CommandData 
{

	String command();
	String description();
	
	String[] aliases() default {};
	
	boolean debug() default false;
	boolean djOnly() default false;
	
}
