package com.hubspot.dropwizard.guice.doubleinject;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Collections2;
import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.Annotations;
import com.google.inject.internal.Errors;
import com.google.inject.internal.ErrorsException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Utils {
    /**
     * Used with {@link com.hubspot.dropwizard.guice.doubleinject.GuiceCommand}.  Finds a method with the {@link Run} command, injects, and runs it.
     */
    public static void runRunnable(Object obj, final Injector injector) throws Exception {
        Optional<Method> oRun = findRunable(obj.getClass());
        if(!oRun.isPresent()) throw new IllegalStateException("No runnable method found.  @Run annotation must be applied to a method.");
        Method run = oRun.get();

        Errors errors = new Errors(run);
        List<Key<?>> keys = getMethodKeys(run, errors);
        errors.throwConfigurationExceptionIfErrorsExist();

        run.invoke(obj, Collections2.transform(keys, new Function<Key<?>, Object>() {
            @Override
            public Object apply(Key<?> input) {
                return injector.getInstance(input);
            }
        }).toArray());
    }

    private static Optional<Method> findRunable(Class<?> klass) {
        if(klass == Object.class) return Optional.absent();
        for (Method method : klass.getMethods()) {
            if (method.getAnnotation(Run.class) != null)
                return Optional.of(method);
        }
        return findRunable(klass.getSuperclass());
    }

    //Lifted from Jukito: https://github.com/ArcBees/Jukito/blob/master/jukito/src/main/java/org/jukito/GuiceUtils.java
    private static List<Key<?>> getMethodKeys(Method method, Errors errors) {
        Annotation allParameterAnnotations[][] = method.getParameterAnnotations();
        List<Key<?>> result = new ArrayList<>(allParameterAnnotations.length);
        Iterator<Annotation[]> annotationsIterator = Arrays.asList(allParameterAnnotations).iterator();
        TypeLiteral<?> type = TypeLiteral.get(method.getDeclaringClass());
        for (TypeLiteral<?> parameterType : type.getParameterTypes(method)) {
            try {
                Annotation[] parameterAnnotations = annotationsIterator.next();
                result.add(Annotations.getKey(parameterType, method, parameterAnnotations, errors));
            } catch (ConfigurationException e) {
                errors.merge(e.getErrorMessages());
            } catch (ErrorsException e) {
                errors.merge(e.getErrors());
            }
        }
        return result;
    }
}
