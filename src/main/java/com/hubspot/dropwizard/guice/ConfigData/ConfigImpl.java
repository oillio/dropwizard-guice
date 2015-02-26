package com.hubspot.dropwizard.guice.ConfigData;

import com.hubspot.dropwizard.guice.ConfigData.Config;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.lang.annotation.Annotation;

public class ConfigImpl implements Config, Serializable {

    private final String value;
    private final Class root;

    public ConfigImpl(String value) {
        this.value = checkNotNull(value, "name");
        this.root = void.class;
    }

    public ConfigImpl(Class root, String value) {
        this.value = checkNotNull(value, "name");
        this.root = checkNotNull(root);
    }

    public String value() {
        return this.value;
    }

    public Class root() {
        return this.root;
    }

    public int hashCode() {
        // This is specified in java.lang.Annotation.
        return ((127 * "value".hashCode()) ^ value.hashCode()) +
               ((127 * "root".hashCode()) ^ root.hashCode());
    }

    public boolean equals(Object o) {
        if (!(o instanceof Config)) {
            return false;
        }

        Config other = (Config) o;
        return value.equals(other.value()) &&
               root.equals(other.root());
    }

    public String toString() {
        return "@" + Config.class.getName() + "(root=" + root + ", " + "value=" + value + ")";
    }

    public Class<? extends Annotation> annotationType() {
        return Config.class;
    }

    private static final long serialVersionUID = 0;
}
