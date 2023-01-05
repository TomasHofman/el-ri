package org.jboss.el.cache;

import org.junit.Assert;
import org.junit.Test;

public class FactoryFinderCacheTestCase {

    @Test
    public void testServiceFileWithComment() {
        String className = FactoryFinderCache.loadImplementationClassName("org.jboss.el.cache.TestService",
                this.getClass().getClassLoader());
        Assert.assertEquals("org.jboss.el.cache.TestServiceImpl", className);
    }
}
