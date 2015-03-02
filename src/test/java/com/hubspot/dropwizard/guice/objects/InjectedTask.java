package com.hubspot.dropwizard.guice.objects;

import com.google.common.collect.ImmutableMultimap;
import com.google.inject.Singleton;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.PrintWriter;

@Singleton
public class InjectedTask extends AbstractTask {

    @Inject
    protected InjectedTask(@Named("TestTaskName") String name) {
        super(name);
    }

    @Override
    public void execute(ImmutableMultimap<String, String> immutableMultimap, PrintWriter printWriter) throws Exception {

    }
}
