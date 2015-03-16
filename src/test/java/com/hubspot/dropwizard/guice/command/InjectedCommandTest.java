package com.hubspot.dropwizard.guice.command;

import com.hubspot.dropwizard.guice.sample.DoubleInjectApplication;
import com.hubspot.dropwizard.guice.sample.command.TestCommand;
import org.junit.Test;

import static org.junit.Assert.*;

public class InjectedCommandTest {

    @Test
    public void run_test_command() {
        new CommandRunner<>(DoubleInjectApplication.class, "TestCommand").run();
        assertEquals(DoubleInjectApplication.class, TestCommand.bootstrap.getApplication().getClass());
        assertTrue(TestCommand.namespace != null);
    }
}
