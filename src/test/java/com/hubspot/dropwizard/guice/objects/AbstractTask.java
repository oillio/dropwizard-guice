package com.hubspot.dropwizard.guice.objects;

import io.dropwizard.servlets.tasks.Task;

public abstract class AbstractTask extends Task {
    protected AbstractTask(String name) {
        super(name);
    }
}
