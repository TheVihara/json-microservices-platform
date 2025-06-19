package net.unnamed.service.command.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
    String name();
    String[] aliases() default {};
    String description() default "";
    String usage() default "";
    String permission() default "";
    int cooldown() default 0; // seconds
    boolean async() default false;
}