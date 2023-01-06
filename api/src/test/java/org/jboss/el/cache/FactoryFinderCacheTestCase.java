package org.jboss.el.cache;

import org.junit.Assert;
import org.junit.Test;

public class FactoryFinderCacheTestCase {

    @Test
    public void testServiceFileWithComment() {
        String className = FactoryFinderCache.loadImplementationClassName("org.jboss.el.cache.TestService-comment",
                this.getClass().getClassLoader());
        Assert.assertEquals("org.jboss.el.cache.TestServiceImpl", className);
    }

    @Test
    public void testEmptyServiceFile() {
        String className = FactoryFinderCache.loadImplementationClassName("org.jboss.el.cache.TestService-empty",
                this.getClass().getClassLoader());
        Assert.assertNull(className);
    }

    @Test
    public void testServiceFile() {
        String className = FactoryFinderCache.loadImplementationClassName("org.jboss.el.cache.TestService",
                this.getClass().getClassLoader());
        Assert.assertEquals("org.jboss.el.cache.TestServiceImpl", className);
    }

    @Test
    public void testSanitizeMethod() {
        Assert.assertEquals("ClassName", FactoryFinderCache.sanitize("ClassName"));
        Assert.assertEquals("ClassName", FactoryFinderCache.sanitize(" ClassName "));
        Assert.assertEquals("ClassName", FactoryFinderCache.sanitize("\tClassName\t"));
        Assert.assertEquals("ClassName", FactoryFinderCache.sanitize(" \tClassName\t # comment..."));
        Assert.assertEquals("", FactoryFinderCache.sanitize("# only comment..."));
        Assert.assertEquals("", FactoryFinderCache.sanitize(""));
        Assert.assertNull(FactoryFinderCache.sanitize(null));
    }
}
