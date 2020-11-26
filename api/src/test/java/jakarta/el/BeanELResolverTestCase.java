/*
 * Copyright (c) 2015, 2020 Oracle and/or its affiliates. All rights reserved.
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

package jakarta.el;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author <a href="mailto:cdewolf@redhat.com">Carlo de Wolf</a>
 */
public class BeanELResolverTestCase {
    public static class Greeter {
        public String greet(final String... args) {
            return "Hello " + Arrays.toString(args);
        }
        public String greet2(final String prefix, final String... args) {
            return "Hello " + prefix + ": " + Arrays.toString(args);
        }
    }

    @Test
    public void testVarArgs() {
        final BeanELResolver resolver = new BeanELResolver();
        final ELContext context = new StandardELContext(ExpressionFactory.newInstance());
        final Greeter base = new Greeter();
        final String method = "greet";
        final Class[] paramTypes = new Class[] { String[].class };
        final Object[] params = new Object[] { new String[] { "testVarArgs" } };
        final String result = (String) resolver.invoke(context, base, method, paramTypes, params);
        assertEquals("Hello [testVarArgs]", result);
    }

    @Test
    public void testVarArgs2() {
        final BeanELResolver resolver = new BeanELResolver();
        final ELContext context = new StandardELContext(ExpressionFactory.newInstance());
        final Greeter base = new Greeter();
        final String method = "greet2";
        final Class[] paramTypes = new Class[] { String.class, String[].class };
        final Object[] params = new Object[] { "prefix", new String[] { "testVarArgs2" } };
        final String result = (String) resolver.invoke(context, base, method, paramTypes, params);
        assertEquals("Hello prefix: [testVarArgs2]", result);
    }
    
    /**
     * original test from the bugfix
    */
    @Test
    public void testBug56425() {
        ELProcessor processor = new ELProcessor();
        processor.defineBean("string", "a-b-c-d");
        assertEquals("a_b_c_d", processor.eval("string.replace(\"-\",\"_\")"));
    }
    
    /**
     * test the bugfix following the pattern of other tests
     */
    @Test
    public void testBug56425_2() {
        final BeanELResolver resolver = new BeanELResolver();
        final ELContext context = new StandardELContext(ExpressionFactory.newInstance());
        final String base = "a-b-c-d";
        final String method = "replace";
        final Class[] paramTypes = new Class[] { String.class, String.class };
        final Object[] params = new Object[] { "-", "_" };
        final String result = (String) resolver.invoke(context, base, method, paramTypes, params);
        assertEquals("a_b_c_d", result);
	}
}
