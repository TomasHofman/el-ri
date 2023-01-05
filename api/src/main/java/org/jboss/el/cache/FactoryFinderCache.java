/*
 * Copyright (c) 2012, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.jboss.el.cache;

import java.lang.Class;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ServiceLoader;

/**
 * @author Stuart Douglas
 */
public class FactoryFinderCache {

    private static final Map<CacheKey, String> CLASS_CACHE = new ConcurrentHashMap<CacheKey, String>();

    /**
     * Called by the container at deployment time to set the name of a given factory, to remove the need for the
     * implementation to look it up on every call.
     *
     * @param classLoader      The deployments class loader
     * @param factoryId        The type of factory that is being recorded (at this stage only jakarta.el.ExpressionFactory has any effect
     * @param factoryClassName The name of the factory class that is present in the deployment, or null if none is present
     */
    public static void addCacheEntry(final ClassLoader classLoader, final String factoryId, final String factoryClassName) {
        if (factoryClassName == null) {
            CLASS_CACHE.put(new CacheKey(classLoader, factoryId), "");
        } else {
            CLASS_CACHE.put(new CacheKey(classLoader, factoryId), factoryClassName);
        }
    }

    /**
     * This should be called by the container on undeploy to remove all references to the given class loader
     * from the cache.
     *
     * @param classLoader The class loader to remove
     */
    public static void clearClassLoader(final ClassLoader classLoader) {
        BeanPropertiesCache.clear(classLoader);
        final Iterator<Map.Entry<CacheKey, String>> it = CLASS_CACHE.entrySet().iterator();
        while (it.hasNext()) {
            final CacheKey key = it.next().getKey();
            if (key.loader == classLoader) {
                it.remove();
            }
        }
    }

    public static String loadImplementationClassName(final String factoryId, final ClassLoader classLoader) {

        final Map<CacheKey, String> classCache = CLASS_CACHE;
        if (classCache != null) {
            final String value = classCache.get(new CacheKey(classLoader, factoryId));
            if (value != null) {
                if (value.equals("")) {
                    return null;
                }
                return value;
            }
        }

        Object result = null;
        // try to find services in CLASSPATH
        try {
            Class factoryClass = Class.forName(factoryId);
            ServiceLoader<Object> serviceLoader = ServiceLoader.load(factoryClass, classLoader);
            Iterator<Object> iter = serviceLoader.iterator();
            while (result == null && iter.hasNext()) {
                result = iter.next();
            }

            if (result != null) {
                if (classCache != null) {
                    classCache.put(new CacheKey(classLoader, factoryId), result.getClass().getName());
                }
                return result.getClass().getName();
            }
        } catch (Exception ex) {
        }
        if (classCache != null) {
            classCache.put(new CacheKey(classLoader, factoryId), "");
        }
        return null;
    }

    private static class CacheKey {
        private final ClassLoader loader;
        private final String className;

        private CacheKey(final ClassLoader loader, final String className) {
            this.loader = loader;
            this.className = className;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final CacheKey cacheKey = (CacheKey) o;

            if (className != null ? !className.equals(cacheKey.className) : cacheKey.className != null) return false;
            if (loader != null ? !loader.equals(cacheKey.loader) : cacheKey.loader != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = loader != null ? loader.hashCode() : 0;
            result = 31 * result + (className != null ? className.hashCode() : 0);
            return result;
        }
    }


}
