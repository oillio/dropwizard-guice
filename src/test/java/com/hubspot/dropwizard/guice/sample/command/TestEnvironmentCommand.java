package com.hubspot.dropwizard.guice.sample.command;

import com.hubspot.dropwizard.guice.doubleinject.InjectedEnvironmentCommand;
import com.hubspot.dropwizard.guice.doubleinject.Run;
import com.hubspot.dropwizard.guice.sample.DoubleInjectApplication;
import com.hubspot.dropwizard.guice.sample.HelloWorldConfiguration;
import io.dropwizard.setup.Environment;
import net.sourceforge.argparse4j.inf.Namespace;

import javax.inject.Inject;

public class TestEnvironmentCommand extends InjectedEnvironmentCommand<HelloWorldConfiguration> {
    public static String configName;
    public static Environment environment;
    public static Namespace namespace;

    @Inject
    public TestEnvironmentCommand(DoubleInjectApplication app) {
        super(app, "TestEnvironmentCommand", "A command that does not do much.");
    }

    @Run
    public void run(HelloWorldConfiguration config,
                    Environment environment,
                    Namespace namespace) {
        TestEnvironmentCommand.configName = config.getDefaultName();
        TestEnvironmentCommand.environment = environment;
        TestEnvironmentCommand.namespace = namespace;
    }
}
