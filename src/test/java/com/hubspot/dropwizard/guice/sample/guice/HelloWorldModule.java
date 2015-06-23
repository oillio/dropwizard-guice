package com.hubspot.dropwizard.guice.sample.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.hubspot.dropwizard.guice.sample.db.ExplicitDAO;

import javax.inject.Named;

public class HelloWorldModule extends AbstractModule {
	
	@Override
	protected void configure() {
        bind(ExplicitDAO.class);
        bindConstant().annotatedWith(Names.named("TestTaskName")).to("test task");
	}

	@Provides
	@Named("sample")
	public String provideTemplate() {
		return "foo";
	}

}
