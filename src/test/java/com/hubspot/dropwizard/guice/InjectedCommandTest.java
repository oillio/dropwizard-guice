package com.hubspot.dropwizard.guice;

import com.hubspot.dropwizard.guice.sample.HelloWorldApplication;
import com.hubspot.dropwizard.guice.sample.command.TestCommand;
import com.hubspot.dropwizard.guice.util.CommandRunner;
import org.junit.Test;

import static org.junit.Assert.*;

public class InjectedCommandTest {

    @Test
    public void run_test_command() {
        new CommandRunner<>(HelloWorldApplication.class, "TestCommand").run();
        assertEquals(HelloWorldApplication.class, TestCommand.bootstrap.getApplication().getClass());
        assertTrue(TestCommand.namespace != null);
    }
}
