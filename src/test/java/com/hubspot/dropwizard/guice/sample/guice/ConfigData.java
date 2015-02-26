package com.hubspot.dropwizard.guice.sample.guice;

import com.hubspot.dropwizard.guice.ConfigData.Config;

import javax.inject.Inject;

//This is broken out in order to test Just In Time binding.
//This class should be available to Resources without an explicit binding statement.
public class ConfigData {
    @Inject
    @Config("template")
    private String template;

    @Inject
    @Config("defaultName")
    private String defaultName;

    public String getTemplate() { return template; }

    public String getDefaultName() { return defaultName; }
}
