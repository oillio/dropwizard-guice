//This is in this package to test that
//config files in packages outside the named root will not be
//picked up by the auto-binding functionality.
package com.hubspot.dropwizard.guice.sample;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OtherConfig {
    @JsonProperty
    private String otherData;

    public String getOtherData() { return otherData; }
}
