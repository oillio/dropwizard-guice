package com.hubspot.dropwizard.guice.command;

import com.hubspot.dropwizard.guice.sample.DoubleInjectApplication;
import com.hubspot.dropwizard.guice.sample.command.TestEnvironmentCommand;
import org.junit.Test;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;
import static org.junit.Assert.*;

public class InjectedEnvironmentCommandTest {

    @Test
    public void run_test_command() {
        String configPath = resourceFilePath("hello-world.yml");
        new CommandRunner<>(DoubleInjectApplication.class, configPath, "TestEnvironmentCommand").run();
        assertEquals("Joe", TestEnvironmentCommand.configName);
        assertTrue(TestEnvironmentCommand.environment != null);
        assertEquals(configPath, TestEnvironmentCommand.namespace.get("file"));
    }
}
