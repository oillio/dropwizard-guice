package com.hubspot.dropwizard.guice.ConfigData;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import io.dropwizard.Configuration;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static com.google.common.base.Throwables.propagate;
import static java.lang.String.format;

/**
 * Binds fields in the configurationClasses.  Names field using the
 * @Config qualifier.
 * @param <T>
 */
public class ConfigDataModule<T extends Configuration> extends AbstractModule {
    private final T configuration;
    private final String[] configurationPackages;

	public ConfigDataModule(T configuration,
                            String[] configurationPackages) {
        this.configuration = Preconditions.checkNotNull(configuration);
        Preconditions.checkNotNull(configurationPackages);
        this.configurationPackages = ensureTypeInPackages(configuration.getClass(), configurationPackages);
	}

    private String[] ensureTypeInPackages(Class<?> type, String[] packages) {
        String configName = type.getName();
        for(String pack : packages) {
            if(configName.startsWith(pack)) return packages;
        }
        return ArrayUtils.add(packages, configName);
    }

	@Override
	protected void configure() {
        bindConfigs();
	}

    private void bindConfigs() {
        HashMap<Class, String[]> roots = new HashMap<>();
        roots.put(void.class, new String[0]);
        bindConfigs(configuration.getClass(), roots, Lists.<Class<?>>newArrayList());
    }
    @SuppressWarnings("unchecked")
    private void bindConfigs(Class<?> config, Map<Class,String[]> roots, List<Class<?>> visited) {
        List<Class<?>> classes = Lists.newArrayList(ClassUtils.getAllSuperclasses(config));
        classes.add(config);
        for(Class<?> cls: classes) {
            //Only ever use a given class as a root once.  Additional uses will have conflicting paths.
            boolean useAsRoot = false;
            if(!visited.contains(cls)) {
                useAsRoot = true;
                visited.add(cls);
            }
            for(Field field: cls.getDeclaredFields()) {
                Class<?> type = field.getType();
                final String name = field.getName();

                Map<Class, String[]> newRoots = Maps.newHashMap(Maps.transformValues(roots, new Function<String[], String[]>() {
                    @Override
                    public String[] apply(String[] path) {
                        String[] subpath = new String[path.length + 1];
                        System.arraycopy(path, 0, subpath, 0, path.length);
                        subpath[path.length] = name;
                        return subpath;
                    }
                }));
                if(useAsRoot) newRoots.put(cls, new String[]{ name });
                ConfigElementProvider provider = new ConfigElementProvider(newRoots.get(void.class));

                for (Entry<Class, String[]> root : newRoots.entrySet()) {
                    bind(type)
                            .annotatedWith(new ConfigImpl(root.getKey(), Joiner.on(".").join(root.getValue())))
                            .toProvider(provider);
                }

                if(!type.isEnum() && isInConfigPackage(type))
                    bindConfigs(type, newRoots, visited);
            }
        }
    }

    private boolean isInConfigPackage(Class<?> type) {
        String name = type.getName();
        if(name == null) return false;

        for(String pack : configurationPackages) {
            if(name.startsWith(pack)) return true;
        }
        return false;
    }

    private class ConfigElementProvider<U> implements Provider<U> {
        private final Field[] path;

        public ConfigElementProvider(String[] path) {
            this.path = new Field[path.length];

            Class<?> cls = configuration.getClass();
            for(int i=0; i<path.length; i++) {
                this.path[i] = findField(cls, path[i]);
                cls = this.path[i].getType();
            }
        }

        private Field findField(final Class<?> cls, String name) {
            Field f;
            Class<?> search = cls;
            do {
                f = FieldUtils.getDeclaredField(search, name, true);
                if(f != null)
                    return f;
                else
                    search = search.getSuperclass();

            } while(!search.equals(Object.class));

            throw new IllegalStateException(format("Unable to find field %s on %s", name, cls.getName()));
        }

        @Override
        public U get() {
            Object obj = configuration;
            for(Field field: path) {
                try {
                    obj = field.get(obj);
                    if (obj == null) {
                        return null; // Should cause an injection exception
                    }

                } catch(IllegalAccessException e) {
                    throw propagate(e);
                }
            }

            return (U) obj;
        }
    }
}
