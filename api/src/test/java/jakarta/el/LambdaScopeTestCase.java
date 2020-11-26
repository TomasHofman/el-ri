/*
 * Copyright (c) 2020 Oracle and/or its affiliates. All rights reserved.
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

import org.junit.Assert;
import org.junit.Test;

/**
 * testcase for https://issues.jboss.org/browse/JBEE-158
 *
 * @author Tomaz Cerar (c) 2015 Red Hat Inc.
 */

public class LambdaScopeTestCase {

    @Test
    public void testScope() {
        ELProcessor processor = new ELProcessor();
        processor.defineBean("x", null);
        try {
            processor.eval("(x -> x.bug ()) ('bug')");
        } catch (RuntimeException exception) {
            // This is expected, there is no method bug() on strings.

        }
        Assert.assertNull(processor.eval("x")); // This must evaluate to null, but instead evaluates to "bug".

    }
}
