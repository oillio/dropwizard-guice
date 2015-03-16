package com.hubspot.dropwizard.guice.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Apply to method in injectable commands.
 * Specifies the method to run to kick off the command.
 * When the command is run from Dropwizard, this method
 * will be called with Guice injected parameters.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Run { }
