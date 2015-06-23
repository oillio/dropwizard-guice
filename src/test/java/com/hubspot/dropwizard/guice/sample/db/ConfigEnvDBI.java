package com.hubspot.dropwizard.guice.sample.db;

import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;


/**
 * a stub DBI object to mimic the symptoms described in
 * https://github.com/HubSpot/dropwizard-guice/issues/19
 */
public class ConfigEnvDBI {

    private final Configuration configuration;
    private final Environment environment;

    public ConfigEnvDBI(Configuration configuration, Environment environment) {
        this.configuration = configuration;
        this.environment = environment;
    }
}
