package com.hubspot.dropwizard.guice.sample;

import com.hubspot.dropwizard.guice.GuiceBundle;
import com.hubspot.dropwizard.guice.sample.config.SubConfig;
import com.hubspot.dropwizard.guice.sample.guice.DependentModule;
import com.hubspot.dropwizard.guice.sample.guice.HelloWorldModule;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class HelloWorldApplication extends Application<HelloWorldConfiguration> {

	public GuiceBundle<HelloWorldConfiguration> guiceBundle;

	public static void main(String[] args) throws Exception {
		new HelloWorldApplication().run(args);
	}

	@Override
	public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {

		guiceBundle = GuiceBundle.<HelloWorldConfiguration>newBuilder()
				.addInitModule(new HelloWorldModule())
			    .addModule(new DependentModule())
				.enableAutoConfig(getClass().getPackage().getName())
				.setConfigClass(HelloWorldConfiguration.class)
				.build();

		bootstrap.addBundle(guiceBundle);
	}

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void run(HelloWorldConfiguration helloWorldConfiguration, Environment environment) throws Exception {
    }
}
