package com.hubspot.dropwizard.guice;

import io.dropwizard.Configuration;
import io.dropwizard.cli.Command;
import io.dropwizard.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 * Must be used in conjunction with the GuiceBundle.
 * The method annotated with {@link Run} will be injected and run when this command is called.
 * The {@link Bootstrap}, and {@link Namespace} will be available for injection.
 */
public abstract class InjectedCommand<T extends Configuration> extends Command implements GuiceCommand<T> {
    //I can't figure out how to get the GuiceBundle to work correctly
    //without defining a T, which should not be necessary.
    private GuiceBundle<T> init;

    protected InjectedCommand(String name, String description) {
        super(name, description);
    }

    @Override
    public void setInit(GuiceBundle<T> init) {
        this.init = init;
    }

    @Override
    final public void run(Bootstrap<?> bootstrap, Namespace namespace) throws Exception {
        if(init == null) throw new IllegalStateException("Injected Command run without a GuiceBundle. Was the application initialized correctly?");

        init.run((Bootstrap<T>)bootstrap, null, null);
        init.setNamespace(namespace);
        Utils.runRunnable(this, init.getInjector().get());
    }
}