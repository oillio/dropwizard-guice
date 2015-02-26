package com.hubspot.dropwizard.guice.sample.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.hubspot.dropwizard.guice.ConfigData.Config;

public class HelloWorldModule extends AbstractModule {
	
	@Override
	protected void configure() {

	}
	
	@Provides
	@Config("sample")
	public String provideTemplate() {
		return "foo";
	}

}
