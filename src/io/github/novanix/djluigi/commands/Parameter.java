package io.github.novanix.djluigi.commands;

import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import net.dv8tion.jda.api.interactions.commands.OptionType;

@Retention(RUNTIME)
public @interface Parameter 
{
	public String name();
	// Note: the description cannot be longer than 100 characters due to discord limitations
	public String description() default "";
	public OptionType type();
	public boolean required() default true;
}
