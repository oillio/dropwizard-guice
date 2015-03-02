package com.hubspot.dropwizard.guice.sample.guice;

import com.hubspot.dropwizard.guice.sample.HelloWorldConfiguration;

import javax.inject.Inject;

//This is broken out in order to test Just In Time binding.
//This class should be available to Resources without an explicit binding statement.
public class ConfigData {

    @Inject
    private HelloWorldConfiguration config;

    public String getTemplate() { return config.getTemplate(); }

    public String getDefaultName() { return config.getDefaultName(); }
}
