package com.hubspot.dropwizard.guice;

import com.google.common.collect.ImmutableList;
import com.google.inject.Injector;
import com.hubspot.dropwizard.guice.sample.guice.HelloWorldModule;
import com.squarespace.jersey2.guice.BootstrapUtils;
import io.dropwizard.Configuration;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.hk2.api.ServiceLocator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.ServletException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GuiceBundleTest {

    @Mock
    Environment environment;

    private GuiceBundle<Configuration> guiceBundle;

    @After
    public void tearDown() {
        BootstrapUtils.reset();
    }

    @Before
    public void setUp() {
        //given
        environment = new Environment("test env", Jackson.newObjectMapper(), null, null, null);
        guiceBundle = GuiceBundle.newBuilder()
                .addModule(new HelloWorldModule())
                .build();
        Bootstrap bootstrap = mock(Bootstrap.class);
        when(bootstrap.getCommands()).thenReturn(ImmutableList.of());
        guiceBundle.initialize(bootstrap);
        guiceBundle.run(new Configuration(), environment);
    }

    @Test
    public void createsInjectorWhenInit() throws ServletException {
        //then
        Injector injector = guiceBundle.getInjector().get();
        assertThat(injector).isNotNull();
    }

    @Test
    public void serviceLocatorIsAvaliable () throws ServletException {
        ServiceLocator serviceLocator = guiceBundle.getInjector().get().getInstance(ServiceLocator.class);
        assertThat(serviceLocator).isNotNull();
    }
}
