package com.hubspot.dropwizard.guice;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.inject.*;
import com.hubspot.dropwizard.guice.command.GuiceCommand;
import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.cli.Command;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import net.sourceforge.argparse4j.inf.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GuiceBundle<T extends Configuration> implements ConfiguredBundle<T> {

    final Logger logger = LoggerFactory.getLogger(GuiceBundle.class);

    private final AutoConfig autoConfig;
    private final List<Module> modules;
    private final List<Module> initModules;
    private final List<Function<Injector, ServletContextListener>> contextListenerGenerators;
    private final InjectorFactory injectorFactory;

    private Injector initInjector;
    private Injector finalInjector;
    private DropwizardEnvironmentModule dropwizardEnvironmentModule;
    private Optional<Class<T>> configurationClass;
    private Stage stage;

    public static class Builder<T extends Configuration> {
        private AutoConfig autoConfig;
        private List<Module> initModules = Lists.newArrayList();
        private List<Module> modules = Lists.newArrayList();
        private List<Function<Injector, ServletContextListener>> contextListenerGenerators = Lists.newArrayList();
        private Optional<Class<T>> configurationClass = Optional.absent();
        private InjectorFactory injectorFactory = new InjectorFactoryImpl();

        /**
         * Add a module to the bundle.
         * Module may be injected with configuration and environment data.
         * This module will NOT be available for other Bundles and Commands initialized with AutoConfig.
         * Modules will also NOT be available when running classic Command and ConfiguredCommands.
         * They will be available when using InjectedConfiguredCommand, however.
         */
        public Builder<T> addModule(Module module) {
            Preconditions.checkNotNull(module);
            modules.add(module);
            return this;
        }

        /**
         * Add a module to the bundle.
         * Module will not be injected itself.
         * This module will be available for other Bundles and Commands
         * initialized with AutoConfig.
         */
        public Builder<T> addInitModule(Module module) {
            Preconditions.checkNotNull(module);
            initModules.add(module);
            return this;
        }

        public Builder<T> addServletContextListener(Function<Injector, ServletContextListener> contextListenerGenerator) {
            Preconditions.checkNotNull(contextListenerGenerator);
            contextListenerGenerators.add(contextListenerGenerator);
            return this;
        }

        public Builder<T> setConfigClass(Class<T> clazz) {
            configurationClass = Optional.of(clazz);
            return this;
        }
        
        public Builder<T> setInjectorFactory(InjectorFactory factory) {
            Preconditions.checkNotNull(factory);
            injectorFactory = factory;
            return this;
        }

        public Builder<T> enableAutoConfig(String... basePackages) {
            Preconditions.checkNotNull(basePackages.length > 0);
            Preconditions.checkArgument(autoConfig == null, "autoConfig already enabled!");
            autoConfig = new AutoConfig(basePackages);
            return this;
        }

        public GuiceBundle<T> build() {
            return build(Stage.PRODUCTION);
        }

        public GuiceBundle<T> build(Stage s) {
            return new GuiceBundle<>(s, autoConfig, modules, initModules, contextListenerGenerators, injectorFactory,
                                      configurationClass);
        }

    }
    
    public static <T extends Configuration> Builder<T> newBuilder() {
        return new Builder<>();
    }

    private GuiceBundle(Stage stage,
                        AutoConfig autoConfig,
                        List<Module> modules,
                        List<Module> initModules,
                        List<Function<Injector, ServletContextListener>> contextListenerGenerators,
						InjectorFactory injectorFactory,
                        Optional<Class<T>> configurationClass) {
        Preconditions.checkNotNull(modules);
        Preconditions.checkArgument(!modules.isEmpty());
        Preconditions.checkNotNull(contextListenerGenerators);
        Preconditions.checkNotNull(stage);
        this.modules = modules;
        this.initModules = initModules;
        this.contextListenerGenerators = contextListenerGenerators;
        this.autoConfig = autoConfig;
        this.configurationClass = configurationClass;
        this.injectorFactory = injectorFactory;
        this.stage = stage;
    }

    @Override
    public void initialize(Bootstrap<?> bootstrap) {
        initInjector();
        if (autoConfig != null) {
            autoConfig.initialize(bootstrap, initInjector);
        }

        setupCommands(bootstrap.getCommands());
    }

    @SuppressWarnings("unchecked")
    private void setupCommands(Collection<Command> commands) {
        for(Command c : commands) {
            if(c instanceof GuiceCommand) {
                ((GuiceCommand) c).setInit(this);
            }
        }
    }

    private void initInjector() {
        try {
            initInjector = injectorFactory.create(this.stage, ImmutableList.copyOf(this.initModules));
        } catch(Exception ie) {
		    logger.error("Exception occurred when creating Guice Injector - exiting", ie);
		    System.exit(1);
	    }
    }

    @Override
    public void run(final T configuration, final Environment environment) {
        run(null, environment, configuration);
    }
    public void run(Bootstrap<T> bootstrap, Environment environment, final T configuration) {
        initEnvironmentModule();
        setEnvironment(bootstrap, environment, configuration);
        //The secondary injected modules generally use config data.  If we are starting up a command
        //that doesn't have a configuration, loading these modules is useless at best.
        boolean addModules = configuration != null;
        initGuice(environment, addModules);
        Injector injector = getInjector().get();

        if(environment != null) {
            JerseyUtil.registerGuiceBound(injector, environment.jersey());
            JerseyUtil.registerGuiceFilter(environment);

            for (Function<Injector, ServletContextListener> generator : contextListenerGenerators) {
                environment.servlets().addServletListeners(generator.apply(injector));
            }

            if (autoConfig != null) {
                autoConfig.run(environment, injector);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void setEnvironment(Bootstrap<T> bootstrap, final Environment environment, final T configuration) {
        dropwizardEnvironmentModule.setEnvironmentData(bootstrap, environment, configuration);
    }


    public void setNamespace(Namespace namespace) {
        dropwizardEnvironmentModule.setNamespace(namespace);
    }

    private void initEnvironmentModule() {
        if (configurationClass.isPresent()) {
            dropwizardEnvironmentModule = new DropwizardEnvironmentModule<>(configurationClass.get());
        } else {
            dropwizardEnvironmentModule = new DropwizardEnvironmentModule<>(Configuration.class);
        }
    }

    private void initGuice(final Environment environment, boolean addModules) {
        Injector environmentInjector = initInjector.createChildInjector(dropwizardEnvironmentModule);

        if(addModules) {
            List<Module> validModules = new ArrayList<>();
            for (Module module : modules)
                try {
                    environmentInjector.injectMembers(module);
                    validModules.add(module);
                } catch(ProvisionException e) {
                    logger.warn("Module {} could not be initialized.  It has been ignored.", module.getClass().getName());
                }

            if (environment != null) validModules.add(new JerseyModule());
            finalInjector = environmentInjector.createChildInjector(ImmutableList.copyOf(validModules));
        }
        else finalInjector = environmentInjector;
    }

    public Provider<Injector> getInjector() {
        //With double injection, it is not safe to simply provide the finalInjector as the correct
        //instance will change over time.
        return new Provider<Injector>() {
            @Override
            public Injector get() {
                return (finalInjector != null) ? finalInjector : initInjector;
            }
        };
    }
}
