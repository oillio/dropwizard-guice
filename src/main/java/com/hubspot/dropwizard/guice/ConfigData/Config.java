package com.hubspot.dropwizard.guice.ConfigData;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Guice {@linkplain Qualifier qualifier} that is bound
 * to fields in Dropwizard configuration objects.
 */
@Qualifier
@Documented
@Retention(RUNTIME)
public @interface Config {

    /** The config path. */
    String value();

    /** The root config object to which the path is relative */
    Class root() default void.class;
}
