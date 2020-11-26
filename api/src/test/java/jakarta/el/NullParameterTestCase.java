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

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

/**
 * Testcase for https://issues.jboss.org/browse/WFLY-3456
 *
 * @author Trond G. Ziarkowski
 */
public class NullParameterTestCase {

    public static interface SimpleDataTableHandler<T> {
        public Date getLastUpdate(T item);
    }
  
    public static class SimpleStringHandler implements SimpleDataTableHandler<String> {
        @Override
        public Date getLastUpdate(String item) {
            return new Date();
        }
    }

    @Test
    public void testAmbiguousMethodCall() {
        ELProcessor processor = new ELProcessor();
        processor.defineBean("handler", new SimpleStringHandler());
        processor.eval("date = handler.getLastUpdate(null)");

        Assert.assertNotNull(processor.eval("date"));
    }
}
