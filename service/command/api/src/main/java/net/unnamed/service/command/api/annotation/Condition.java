package net.unnamed.service.command.api.annotation;

import net.unnamed.service.command.api.condition.CommandCondition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Condition {
    Class<? extends CommandCondition>[] value();
}