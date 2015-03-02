package com.hubspot.dropwizard.guice.sample.jersey;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class SimpleParamConverterProvider implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if(genericType.equals(ParamInput.class)) {
            return new ParamConverter<T>() {

                @Override
                public T fromString(String value) {
                    ParamInput ret = new ParamInput();
                    ret.data = value;
                    return (T) ret;
                }

                @Override
                public String toString(T value) {
                    return ((ParamInput) value).data;
                }
            };
        }
        return null;
    }
}
