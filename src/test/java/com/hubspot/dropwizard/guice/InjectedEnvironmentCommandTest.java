package com.hubspot.dropwizard.guice;

import com.hubspot.dropwizard.guice.sample.HelloWorldApplication;
import com.hubspot.dropwizard.guice.sample.command.TestEnvironmentCommand;
import com.hubspot.dropwizard.guice.util.CommandRunner;
import org.junit.Test;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;
import static org.junit.Assert.*;

public class InjectedEnvironmentCommandTest {
    @Test
    public void run_simple_command() {
        new CommandRunner<>(HelloWorldApplication.class, resourceFilePath("hello-world.yml"), "TestEnvironmentCommand").run();
        assertEquals("Joe", TestEnvironmentCommand.configName);
    }
    @Test
    public void run_test_command() {
        String configPath = resourceFilePath("hello-world.yml");
        new CommandRunner<>(HelloWorldApplication.class, configPath, "TestEnvironmentCommand").run();
        assertEquals("Joe", TestEnvironmentCommand.configName);
        assertTrue(TestEnvironmentCommand.environment != null);
        assertEquals(configPath, TestEnvironmentCommand.namespace.get("file"));
    }
}
