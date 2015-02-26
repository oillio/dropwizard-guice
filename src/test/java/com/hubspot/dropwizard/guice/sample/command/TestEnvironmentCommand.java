package com.hubspot.dropwizard.guice.sample.command;

import com.hubspot.dropwizard.guice.ConfigData.Config;
import com.hubspot.dropwizard.guice.InjectedEnvironmentCommand;
import com.hubspot.dropwizard.guice.Run;
import com.hubspot.dropwizard.guice.sample.HelloWorldApplication;
import com.hubspot.dropwizard.guice.sample.HelloWorldConfiguration;
import io.dropwizard.setup.Environment;
import net.sourceforge.argparse4j.inf.Namespace;

import javax.inject.Inject;

public class TestEnvironmentCommand extends InjectedEnvironmentCommand<HelloWorldConfiguration> {
    public static String configName;
    public static Environment environment;
    public static Namespace namespace;

    @Inject
    public TestEnvironmentCommand(HelloWorldApplication app) {
        super(app, "TestEnvironmentCommand", "A command that does not do much.");
    }

    @Run
    public void run(@Config("defaultName") String name,
                    Environment environment,
                    Namespace namespace) {
        TestEnvironmentCommand.configName = name;
        TestEnvironmentCommand.environment = environment;
        TestEnvironmentCommand.namespace = namespace;
    }
}
