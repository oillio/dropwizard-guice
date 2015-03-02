package com.hubspot.dropwizard.guice;

import com.hubspot.dropwizard.guice.sample.HelloWorldApplication;
import com.hubspot.dropwizard.guice.sample.command.TestConfiguredCommand;
import com.hubspot.dropwizard.guice.util.CommandRunner;
import org.junit.Test;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;
import static org.junit.Assert.*;

public class InjectedConfiguredCommandTest {
    @Test
    public void run_test_command() {
        String configPath = resourceFilePath("hello-world.yml");
        new CommandRunner<>(HelloWorldApplication.class, configPath, "SimpleCommand").run();
        assertEquals("Joe", TestConfiguredCommand.configName);
        assertEquals(HelloWorldApplication.class, TestConfiguredCommand.bootstrap.getApplication().getClass());
        assertEquals(configPath, TestConfiguredCommand.namespace.get("file"));
    }
}
