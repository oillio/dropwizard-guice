package com.hubspot.dropwizard.guice.sample.guice;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.hubspot.dropwizard.guice.sample.HelloWorldConfiguration;

import javax.inject.Inject;

public class DependentModule extends AbstractModule {
    @Inject
    private HelloWorldConfiguration config;

    @Override
    protected void configure() {
        bind(String.class).annotatedWith(Names.named("dependent")).toInstance("More data is: " + config.getSubConfig().getMoreData());
    }
}
