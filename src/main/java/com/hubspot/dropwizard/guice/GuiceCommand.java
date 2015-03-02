package com.hubspot.dropwizard.guice;

import io.dropwizard.Configuration;

interface GuiceCommand<T extends Configuration> {
    void setInit(GuiceBundle<T> init);
}
