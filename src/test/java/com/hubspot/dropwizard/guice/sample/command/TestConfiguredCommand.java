package com.hubspot.dropwizard.guice.sample.command;

import com.hubspot.dropwizard.guice.ConfigData.Config;
import com.hubspot.dropwizard.guice.InjectedConfiguredCommand;
import com.hubspot.dropwizard.guice.Run;
import com.hubspot.dropwizard.guice.sample.HelloWorldConfiguration;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;

public class TestConfiguredCommand extends InjectedConfiguredCommand<HelloWorldConfiguration> {
    public static String configName;
    public static Bootstrap bootstrap;
    public static Namespace namespace;

    public TestConfiguredCommand() {
        super("SimpleCommand", "A command that does not do much.");
    }

    @Run
    public void run(@Config("defaultName") String name,
                    Bootstrap bootstrap,
                    Namespace namespace) {
        TestConfiguredCommand.configName = name;
        TestConfiguredCommand.bootstrap = bootstrap;
        TestConfiguredCommand.namespace = namespace;
    }
}
