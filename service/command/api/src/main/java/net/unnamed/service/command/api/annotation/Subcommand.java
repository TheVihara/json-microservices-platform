package net.unnamed.service.command.api.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Subcommand {
    String name();
    String[] aliases() default {};
    String description() default "";
    String usage() default "";
    String permission() default "";
    int cooldown() default 0;
    boolean async() default false;
}