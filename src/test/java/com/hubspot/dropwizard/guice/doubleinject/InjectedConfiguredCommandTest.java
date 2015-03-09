package com.hubspot.dropwizard.guice.doubleinject;

import com.hubspot.dropwizard.guice.sample.DoubleInjectApplication;
import com.hubspot.dropwizard.guice.sample.command.TestConfiguredCommand;
import org.junit.Test;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;
import static org.junit.Assert.assertEquals;

public class InjectedConfiguredCommandTest {

    @Test
    public void run_test_command() {
        String configPath = resourceFilePath("hello-world.yml");
        new CommandRunner<>(DoubleInjectApplication.class, configPath, "TestConfiguredCommand").run();
        assertEquals("Joe", TestConfiguredCommand.configName);
        assertEquals(DoubleInjectApplication.class, TestConfiguredCommand.bootstrap.getApplication().getClass());
        assertEquals(configPath, TestConfiguredCommand.namespace.get("file"));
    }
}
