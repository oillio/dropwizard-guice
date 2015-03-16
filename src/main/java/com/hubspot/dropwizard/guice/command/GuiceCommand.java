package com.hubspot.dropwizard.guice.command;

import com.hubspot.dropwizard.guice.GuiceBundle;
import io.dropwizard.Configuration;

public interface GuiceCommand<T extends Configuration> {
    void setInit(GuiceBundle<T> init);
}
