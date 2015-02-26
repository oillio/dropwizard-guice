package com.hubspot.dropwizard.guice.sample.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.name.Names;
import com.hubspot.dropwizard.guice.ConfigData.Config;

public class DependentModule extends AbstractModule {

    @Inject
    @Config("subConfig.moreData")
    private String injectedData;

    @Override
    protected void configure() {
        bind(String.class).annotatedWith(Names.named("dependent")).toInstance("More data is: " + injectedData);
    }
}
