package com.hubspot.dropwizard.guice.sample.health;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hubspot.dropwizard.guice.InjectableHealthCheck;
import com.hubspot.dropwizard.guice.sample.HelloWorldConfiguration;

@Singleton
public class TemplateHealthCheck extends InjectableHealthCheck {
    
	private final String template;

    @Inject
    public TemplateHealthCheck(HelloWorldConfiguration config) {
        this.template = config.getTemplate();
    }

    @Override
    protected Result check() throws Exception {
        final String saying = String.format(template, "TEST");
        if (!saying.contains("TEST")) {
            return Result.unhealthy("template doesn't include a name");
        }
        return Result.healthy();
    }

    @Override
    public String getName() {
        return "template";
    }
}