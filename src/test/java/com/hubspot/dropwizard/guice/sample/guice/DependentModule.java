package com.hubspot.dropwizard.guice.sample.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.hubspot.dropwizard.guice.sample.HelloWorldConfiguration;
import com.hubspot.dropwizard.guice.sample.db.ConfigEnvDBI;
import io.dropwizard.setup.Environment;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.google.inject.name.Names.named;

public class DependentModule extends AbstractModule {
    @Inject
    private HelloWorldConfiguration config;

    @Inject
    private Environment environment;

    @Override
    protected void configure() {
        bind(String.class).annotatedWith(named("dependent")).toInstance("More data is: " + config.getSubConfig().getMoreData());
        bind(String.class).annotatedWith(named("environmentName")).toInstance(environment.getName());
    }

    @Provides
    @Singleton
    public ConfigEnvDBI provideDBI(HelloWorldConfiguration helloWorldConfiguration, Environment environment) {
        return new ConfigEnvDBI(helloWorldConfiguration, environment);
    }
}
