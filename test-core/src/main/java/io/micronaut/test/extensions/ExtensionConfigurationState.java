package io.micronaut.test.extensions;


import io.micronaut.context.ApplicationContextBuilder;
import io.micronaut.context.env.PropertySource;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.test.annotation.MicronautTestValue;

import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * This class represents possible configuration states for a test class. When
 * the configuration of two test classes is considered identical, then it is
 * possible to reuse the application context and not setup/tear down a new one.
 */
class ExtensionConfigurationState {
    Class<? extends ApplicationContextBuilder> applicationContextBuilderClass;
    Package testClassPackage; // needs to be added for scanning
    Map<String, Object> testProperties;
    List<PropertySource> propertySources;
    Map<String, String> testPropertyProviderProperties;
    MicronautTestValue micronautTestValue;

    /**
     * determines if a test can run in another test's configuration
     * @param other another tests configuration
     * @return true when they are similar enough to reuse
     */
    boolean canReuse(ExtensionConfigurationState other) {
        if (other == null)
            return false;

        if (applicationContextBuilderClass != other.applicationContextBuilderClass)
            return false;

        if (!Objects.equals(testClassPackage.getName(),
                other.testClassPackage.getName()))
            return false;

        if (mapsDiffer(testProperties, other.testProperties))
            return false;

        if (!Objects.equals(propertySources.size(), other.propertySources.size()))
            return false;

        // if (propertySources.)

        return true;
    }

    private static boolean mapsDiffer(Map<String, Object> a, Map<String, Object> b) {
        if (a == null && b == null)
            return false;
        if (a == null)
            return true;
        if (b == null)
            return true;

        if (!a.keySet().equals(b.keySet())) {
            return true;
        }

        for (Map.Entry<String, Object> entry : a.entrySet()) {
            if (!Objects.equals(entry.getValue(), b.get(entry.getKey()))) {
                return true;
            }
        }

        return false;
    }
}
