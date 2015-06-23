package com.hubspot.dropwizard.guice.sample;

import com.hubspot.dropwizard.guice.GuiceBundle;
import com.hubspot.dropwizard.guice.sample.guice.HelloWorldModule;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import static com.hubspot.dropwizard.guice.GuiceBundle.newBuilder;

public class SingleInjectApplication extends Application<HelloWorldConfiguration> {

    @Override
    public void initialize(final Bootstrap<HelloWorldConfiguration> bootstrap) {
        final GuiceBundle<Configuration> jersey2GuiceBundle = newBuilder()
                .addModule(new HelloWorldModule())
                .enableAutoConfig(this.getClass().getPackage().getName())
                .build();
        bootstrap.addBundle(jersey2GuiceBundle);
    }

    @Override
    public void run(HelloWorldConfiguration configuration, Environment environment) throws Exception {

    }
}
