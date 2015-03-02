package com.hubspot.dropwizard.guice.util;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.cli.Command;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.testing.ConfigOverride;
import net.sourceforge.argparse4j.inf.Namespace;

import java.util.Enumeration;
import java.util.Objects;

public class CommandRunner<C extends Configuration> {
    private final Class<? extends Application<C>> applicationClass;
    private final Optional<String> configPath;
    private final String commandName;
    private final Command command;
    private final ConfigOverride[] configOverrides;

    private Application<C> application;
    private Bootstrap<C> bootstrap;
    private Namespace namespace;

    public CommandRunner(Class<? extends Application<C>> applicationClass,
                             String configPath,
                             String commandName,
                             ConfigOverride... configOverrides) {
        this.applicationClass = applicationClass;
        this.configPath = Optional.fromNullable(configPath);
        this.commandName = commandName;
        this.command = null;
        this.configOverrides = configOverrides;
    }

    public CommandRunner(Class<? extends Application<C>> applicationClass,
                         String commandName,
                         ConfigOverride... configOverrides) {
        this.applicationClass = applicationClass;
        this.configPath = Optional.absent();
        this.commandName = commandName;
        this.command = null;
        this.configOverrides = configOverrides;
    }

    public CommandRunner(Class<? extends Application<C>> applicationClass,
                         String configPath,
                         Command command,
                         ConfigOverride... configOverrides) {
        this.applicationClass = applicationClass;
        this.configPath = Optional.fromNullable(configPath);
        this.commandName = null;
        this.command = command;
        this.configOverrides = configOverrides;
    }

    public CommandRunner(Class<? extends Application<C>> applicationClass,
                         Command command,
                         ConfigOverride... configOverrides) {
        this.applicationClass = applicationClass;
        this.configPath = Optional.absent();
        this.commandName = null;
        this.command = command;
        this.configOverrides = configOverrides;
    }

    private void setConfigOverrides() {
        for (ConfigOverride configOverride: configOverrides) {
            configOverride.addToSystemProperties();
        }
    }

    private void resetConfigOverrides() {
        for (Enumeration<?> props = System.getProperties().propertyNames(); props.hasMoreElements();) {
            String keyString = (String) props.nextElement();
            if (keyString.startsWith("dw.")) {
                System.clearProperty(keyString);
            }
        }
    }

    public Application<C> newApplication() {
        try {
            return application = applicationClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Bootstrap<C> newBootStrap() {
        if(application == null) throw new RuntimeException("Application must be initialized before newBootStrap is called.");
        return bootstrap = new Bootstrap<>(application);
    }

    private void initialize() {
        newApplication();
        newBootStrap();
        if(configPath.isPresent())
            namespace = new Namespace(ImmutableMap.<String, Object>of("file", configPath.get()));
        else namespace = new Namespace(ImmutableMap.<String, Object>of());

        application.initialize(bootstrap);
    }

    private Command getCommand(String name) {
        if(bootstrap == null) throw new RuntimeException("Must be initialized before getCommand is called.");
        for(Command command : bootstrap.getCommands()) {
            if(Objects.equals(command.getName(), name)) return command;
        }
        return null;
    }

    public void run() {
        setConfigOverrides();
        initialize();

        try {
            if (command != null) command.run(bootstrap, namespace);
            else getCommand(commandName).run(bootstrap, namespace);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        resetConfigOverrides();
    }
}
