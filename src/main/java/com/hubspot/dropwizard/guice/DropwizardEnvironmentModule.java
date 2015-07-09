package com.hubspot.dropwizard.guice;

import com.google.common.base.Optional;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.ProvisionException;
import com.google.inject.name.Names;

import net.sourceforge.argparse4j.inf.Namespace;

import io.dropwizard.Configuration;
import io.dropwizard.jetty.MutableServletContextHandler;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.servlet.ServletContext;

public class DropwizardEnvironmentModule<T extends Configuration> extends AbstractModule {
  private static final String ILLEGAL_DROPWIZARD_MODULE_STATE = "The dropwizard environment has not been set. This is likely caused by trying to access the dropwizard environment during the bootstrap phase or during a non-configured command.";
  private Optional<T> configuration;
  private Optional<Environment> environment;
  private Optional<Namespace> namespace = Optional.absent();
  private Optional<Bootstrap<T>> bootstrap;
  private Class<? super T> configurationClass;

  public DropwizardEnvironmentModule(Class<T> configurationClass) {
    this.configurationClass = configurationClass;
  }

  @Override
  protected void configure() {
    Provider<T> provider = new CustomConfigurationProvider();
    if (configuration.isPresent()){
      bind(configurationClass).toProvider(provider);
      if (configurationClass != Configuration.class) {
        bind(Configuration.class).toProvider(provider);
      }
    }
    if (environment.isPresent()) {
      bindContext("application", environment.get().getApplicationContext());
    }
  }

  /**
   * Bind some of the context objects to be injectable. Annotated with a {@link com.google.inject.name.Names} to
   * prevent collisions for any that the {@link com.google.inject.servlet.ServletModule} may bind later.
   */
  private void bindContext(String name, MutableServletContextHandler context) {
    bind(ServletContext.class)
      .annotatedWith(Names.named(name))
      .toInstance(context.getServletContext());
  }

  @Deprecated
  public void setEnvironmentData(T configuration, Environment environment) {
    setEnvironmentData(null, environment, configuration);
  }

  public void setEnvironmentData(Bootstrap<T> bootstrap,
                  Environment environment,
                  T configuration) {
    this.bootstrap = Optional.fromNullable(bootstrap);
    this.configuration = Optional.fromNullable(configuration);
    this.environment = Optional.fromNullable(environment);
  }

  public void setNamespace(Namespace namespace) {
    this.namespace = Optional.fromNullable(namespace);
  }

  @Provides
  public Environment providesEnvironment() {
    if (environment == null || !environment.isPresent()) {
      throw new ProvisionException(ILLEGAL_DROPWIZARD_MODULE_STATE);
    }
    return environment.get();
  }

  @Provides
  public Namespace providesNamespace() {
    if (namespace == null || !namespace.isPresent()) {
      throw new ProvisionException(ILLEGAL_DROPWIZARD_MODULE_STATE);
    }
    return namespace.get();
  }

  /**
   * Note: This is a raw type. Guice cannot inject the full type due to type erasure
   */
  @Provides
  public Bootstrap providesBootstrap() {
    if (bootstrap == null || !bootstrap.isPresent()) {
      throw new ProvisionException(ILLEGAL_DROPWIZARD_MODULE_STATE);
    }
    return bootstrap.get();
  }

  private class CustomConfigurationProvider implements Provider<T> {
    @Override
    public T get() {
      if (configuration == null || !configuration.isPresent()) {
        throw new ProvisionException(ILLEGAL_DROPWIZARD_MODULE_STATE);
      }
      return configuration.get();
    }
  }
}
