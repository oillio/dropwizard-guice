//This is in a sub package in order to test that
//config files in packages below the named root will be picked up.
package com.hubspot.dropwizard.guice.sample.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubConfig {
    @JsonProperty
    private String moreData;

    public String getMoreData() { return moreData; }
}
