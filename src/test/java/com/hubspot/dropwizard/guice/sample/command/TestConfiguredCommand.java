package com.hubspot.dropwizard.guice.sample.command;

import com.hubspot.dropwizard.guice.doubleinject.InjectedConfiguredCommand;
import com.hubspot.dropwizard.guice.doubleinject.Run;
import com.hubspot.dropwizard.guice.sample.HelloWorldConfiguration;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;

public class TestConfiguredCommand extends InjectedConfiguredCommand<HelloWorldConfiguration> {
    public static String configName;
    public static Bootstrap bootstrap;
    public static Namespace namespace;

    public TestConfiguredCommand() {
        super("TestConfiguredCommand", "A command that does not do much.");
    }

    @Run
    public void run(HelloWorldConfiguration config,
                    Bootstrap bootstrap,
                    Namespace namespace) {
        TestConfiguredCommand.configName = config.getDefaultName();
        TestConfiguredCommand.bootstrap = bootstrap;
        TestConfiguredCommand.namespace = namespace;
    }
}
