package com.hubspot.dropwizard.guice.sample.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Named;

public class HelloWorldModule extends AbstractModule {
	
	@Override
	protected void configure() {

	}
	
	@Provides
	@Named("sample")
	public String provideTemplate() {
		return "foo";
	}

}
