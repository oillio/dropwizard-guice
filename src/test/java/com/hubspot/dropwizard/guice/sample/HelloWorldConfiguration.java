package com.hubspot.dropwizard.guice.sample;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubspot.dropwizard.guice.sample.config.SubConfig;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

public class HelloWorldConfiguration extends Configuration {
    @NotEmpty
    @JsonProperty
    private String template;

    @NotEmpty
    @JsonProperty
    private String defaultName = "Stranger";

    @JsonProperty
    private SubConfig subConfig;

    @JsonProperty
    private OtherConfig otherConfig;

    public String getTemplate() {
        return template;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public SubConfig getSubConfig() { return subConfig; }

    public OtherConfig getOtherConfig() { return otherConfig; }
}
