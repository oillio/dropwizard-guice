package com.hubspot.dropwizard.guice.command;

import com.hubspot.dropwizard.guice.GuiceBundle;

import net.sourceforge.argparse4j.inf.Namespace;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.cli.EnvironmentCommand;
import io.dropwizard.setup.Environment;

/**
 * Must be used in conjunction with the GuiceBundle.
 * Will load the configuration based Guice modules.
 * The method annotated with {@link Run} will be injected and run when this command is called.
 * The {@link Environment}, {@link Namespace}, and {@link Configuration} will be available for
 * injection.
 */
public abstract class InjectedEnvironmentCommand<T extends Configuration> extends EnvironmentCommand<T> implements GuiceCommand<T> {
    private GuiceBundle<T> init;

    protected InjectedEnvironmentCommand(Application<T> application, String name, String description) {
        super(application, name, description);
    }

    @Override
    public void setInit(GuiceBundle<T> init) {
        this.init = init;
    }

    @Override
    protected void run(Environment environment, Namespace namespace, T configuration) throws Exception {
        if (init == null) {
            throw new IllegalStateException("Injected Command run without a GuiceBundle. Was the application initialized correctly?");
        }

        //We do not need to run init here, as it was already run by the Dropwizard environment initializer.
        init.setNamespace(namespace);
        Utils.runRunnable(this, init.getInjector().get());
    }
}
