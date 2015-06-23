package com.hubspot.dropwizard.guice.sample.command;

import com.hubspot.dropwizard.guice.command.InjectedCommand;
import com.hubspot.dropwizard.guice.command.Run;
import com.hubspot.dropwizard.guice.sample.HelloWorldConfiguration;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

public class TestCommand extends InjectedCommand<HelloWorldConfiguration> {
    public static Bootstrap bootstrap;
    public static Namespace namespace;

    public TestCommand() {
        super("TestCommand", "A command that does not do much.");
    }

    @Run
    public void runner(Bootstrap bootstrap,
                       Namespace namespace) {
        TestCommand.bootstrap = bootstrap;
        TestCommand.namespace = namespace;
    }

    @Override
    public void configure(Subparser subparser) { }
}
