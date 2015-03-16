package com.hubspot.dropwizard.guice.command;

import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.Configuration;
import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * Must be used in conjunction with the GuiceBundle.
 * Will load the configuration based Guice modules.
 * The method annotated with {@link Run} will be injected and run when this command is called.
 * The {link Bootstrap}, {@link Namespace}, and {@link Configuration} will be available for
 * injection.
 */
public abstract class InjectedConfiguredCommand<T extends Configuration> extends ConfiguredCommand<T> implements GuiceCommand<T> {
    private GuiceBundle<T> init;


    protected InjectedConfiguredCommand(String name, String description) {
        super(name, description);
    }

    @Override
    public void setInit(GuiceBundle<T> init) {
        this.init = init;
    }

    @Override
    final protected void run(Bootstrap<T> bootstrap, Namespace namespace, T configuration) throws Exception {
        if(init == null) throw new IllegalStateException("Injected Command run without a GuiceBundle. Was the application initialized correctly?");

        init.run(bootstrap, null, configuration);
        init.setNamespace(namespace);
        Utils.runRunnable(this, init.getInjector().get());
    }
}

