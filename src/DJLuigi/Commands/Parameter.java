package DJLuigi.Commands;

import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import net.dv8tion.jda.api.interactions.commands.OptionType;

@Retention(RUNTIME)
public @interface Parameter 
{
	public String name();
	public String description() default "";
	public OptionType type();
	public boolean required() default true;
}