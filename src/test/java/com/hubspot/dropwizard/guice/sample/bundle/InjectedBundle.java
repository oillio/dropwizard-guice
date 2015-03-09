package com.hubspot.dropwizard.guice.sample.bundle;

import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.inject.Singleton;

@Singleton
public class InjectedBundle implements Bundle {
    @Override
    public void initialize(Bootstrap<?> bootstrap) {

    }

    @Override
    public void run(Environment environment) {

    }
}
